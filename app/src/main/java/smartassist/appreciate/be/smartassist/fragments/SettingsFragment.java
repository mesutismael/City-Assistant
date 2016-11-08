package smartassist.appreciate.be.smartassist.fragments;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.afollestad.materialdialogs.MaterialDialog;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.ScanActivity;
import smartassist.appreciate.be.smartassist.receivers.DeviceAdminRcvr;
import smartassist.appreciate.be.smartassist.services.ChatService;
import smartassist.appreciate.be.smartassist.services.DataService;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 29/01/2015.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener
{
    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminRcvr;
    private CheckBox checkBoxPinEnabled;
    private MaterialDialog dialogProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button buttonEnterKiosk = (Button) view.findViewById(R.id.button_enterKiosk);
        Button buttonExitKiosk = (Button) view.findViewById(R.id.button_exitKiosk);
        Button buttonReloadTiles = (Button) view.findViewById(R.id.button_reloadTiles);
        Button buttonClearData = (Button) view.findViewById(R.id.button_clearData);
        TextView textViewResidence = (TextView) view.findViewById(R.id.textView_residence);
        this.checkBoxPinEnabled = (CheckBox) view.findViewById(R.id.checkbox_pinEnabled);

        buttonEnterKiosk.setOnClickListener(this);
        buttonExitKiosk.setOnClickListener(this);
        buttonReloadTiles.setOnClickListener(this);
        buttonClearData.setOnClickListener(this);
        this.checkBoxPinEnabled.setOnClickListener(this);

        String residence = PreferencesHelper.getResidenceName(view.getContext());
        if(TextUtils.isEmpty(residence))
            residence = "/";
        String flat = PreferencesHelper.getFlatNumber(view.getContext());
        if(TextUtils.isEmpty(flat))
            flat = "/";
        textViewResidence.setText(this.getString(R.string.settings_residence, residence, flat));

        this.checkBoxPinEnabled.setChecked(PreferencesHelper.isPinEnabled(view.getContext()));

        this.mDPM = (DevicePolicyManager) view.getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        this.mDeviceAdminRcvr = new ComponentName(view.getContext(), DeviceAdminRcvr.class);

        this.dialogProgress = new MaterialDialog.Builder(view.getContext())
                .title(R.string.settings_dialog_progress_title)
                .content(R.string.settings_dialog_progress_message)
                .progress(true, 0)
                .accentColorRes(R.color.dialog_text)
                .cancelable(false)
                .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                .build();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(this.getActivity() != null && !this.getActivity().isFinishing())
            LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(this.clearDatabaseResultReceiver, new IntentFilter(DataService.BROADCAST_ACTION_DELETE));
    }

    @Override
    public void onPause()
    {
        if(this.getActivity() != null && !this.getActivity().isFinishing())
            LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(this.clearDatabaseResultReceiver);

        if(this.dialogProgress != null)
            this.dialogProgress.dismiss();

        super.onPause();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_enterKiosk:
                this.enterKioskMode(v.getContext());
                break;

            case R.id.button_exitKiosk:
                this.exitKioskMode();
                break;

            case R.id.button_reloadTiles:
                if(this.getActivity() != null && !this.getActivity().isFinishing())
                {
                    Intent intent = new Intent(this.getActivity(), DataService.class);
                    intent.putExtra(DataService.EXTRA_ACTION, DataService.ACTION_REFRESH);
                    this.getActivity().startService(intent);
                }
                break;

            case R.id.button_clearData:
                if(this.getActivity() != null && !this.getActivity().isFinishing())
                {
                    this.dialogProgress.show();
                    Intent intent = new Intent(this.getActivity(), DataService.class);
                    intent.putExtra(DataService.EXTRA_ACTION, DataService.ACTION_DELETE);
                    intent.putExtra(DataService.EXTRA_SEND_RESULT, true);
                    this.getActivity().startService(intent);
                }
                break;

            case R.id.checkbox_pinEnabled:
                PreferencesHelper.savePinEnabled(v.getContext(), this.checkBoxPinEnabled.isChecked());
                break;
        }
    }

    private void enterKioskMode(Context context)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (this.mDPM.isDeviceOwnerApp(context.getPackageName()))
            {
                String[] packages = {context.getPackageName()};
                try
                {
                    this.mDPM.setLockTaskPackages(this.mDeviceAdminRcvr, packages);
                    this.mDPM.setKeyguardDisabledFeatures(this.mDeviceAdminRcvr, DevicePolicyManager.KEYGUARD_DISABLE_FEATURES_ALL);
                } catch (SecurityException e)
                {
                    Log.d("SettingsFragment", "Security exception: " + e.getMessage());
                }
            }

            if (this.mDPM.isLockTaskPermitted(context.getPackageName()))
            {
                this.getActivity().startLockTask();
            }
        }
    }

    private void exitKioskMode()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            try
            {
                this.getActivity().stopLockTask();
                this.mDPM.setKeyguardDisabledFeatures(this.mDeviceAdminRcvr, DevicePolicyManager.KEYGUARD_DISABLE_FEATURES_NONE);
            } catch (Exception e)
            {
                Log.d("SettingsFragment", "Unlock exception: " + e.getMessage());
            }
        }
    }

    private BroadcastReceiver clearDatabaseResultReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            SettingsFragment.this.dialogProgress.dismiss();

            Intent chatServiceIntent = new Intent(context, ChatService.class);
            context.stopService(chatServiceIntent);

            Intent scanIntent = new Intent(context, ScanActivity.class);
            scanIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(scanIntent);
        }
    };
}
