package smartassist.appreciate.be.smartassist.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.Result;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.api.FlatId;
import smartassist.appreciate.be.smartassist.services.DataService;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;
import smartassist.appreciate.be.smartassist.views.QRScannerView;

/**
 * Created by Inneke on 9/02/2015.
 */
public class ScanActivity extends Activity implements QRScannerView.ResultHandler
{
    private QRScannerView scannerView;
    private MaterialDialog dialogScan;
    private MaterialDialog dialogError;
    private MaterialDialog dialogLoadError;
    private MaterialDialog dialogProgress;

    private boolean resumed;
    private boolean serviceRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_scan);

        this.resumed = false;
        this.serviceRunning = false;

        LocalBroadcastManager.getInstance(this).registerReceiver(this.apiDataResultReceiver, new IntentFilter(DataService.BROADCAST_ACTION_REFRESH));

        if(this.isFirstRun())
        {
            LinearLayout layoutScanner = (LinearLayout) this.findViewById(R.id.layout_scanner);

            this.scannerView = new QRScannerView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutScanner.addView(this.scannerView, params);

            this.dialogScan = new MaterialDialog.Builder(this)
                    .title(R.string.scan_dialog_title)
                    .content(R.string.scan_dialog_message)
                    .positiveText(R.string.scan_dialog_confirm)
                    .callback(new MaterialDialog.ButtonCallback()
                    {
                        @Override
                        public void onPositive(MaterialDialog dialog)
                        {
                            ScanActivity.this.scannerView.setScanning(true);
                        }
                    })
                    .cancelable(false)
                    .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                    .build();

            this.dialogError = new MaterialDialog.Builder(this)
                    .title(R.string.scan_dialog_error_title)
                    .content(R.string.scan_dialog_error_message)
                    .positiveText(R.string.scan_dialog_error_confirm)
                    .callback(new MaterialDialog.ButtonCallback()
                    {
                        @Override
                        public void onPositive(MaterialDialog dialog)
                        {
                            ScanActivity.this.scannerView.setScanning(true);
                            ScanActivity.this.scannerView.startCamera();
                        }
                    })
                    .cancelable(false)
                    .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                    .build();

            this.dialogLoadError = new MaterialDialog.Builder(this)
                    .title(R.string.scan_dialog_load_error_title)
                    .content(R.string.scan_dialog_load_error_message)
                    .positiveText(R.string.scan_dialog_load_error_confirm)
                    .callback(new MaterialDialog.ButtonCallback()
                    {
                        @Override
                        public void onPositive(MaterialDialog dialog)
                        {
                            ScanActivity.this.scannerView.setScanning(true);
                            ScanActivity.this.scannerView.startCamera();
                        }
                    })
                    .cancelable(false)
                    .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                    .build();

            this.dialogProgress = new MaterialDialog.Builder(this)
                    .title(R.string.scan_dialog_progress_title)
                    .content(R.string.scan_dialog_progress_message)
                    .progress(true, 0)
                    .accentColorRes(R.color.dialog_text)
                    .cancelable(false)
                    .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                    .build();
        }
        else
        {
            this.startMainActivity();
            this.overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onDestroy()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.apiDataResultReceiver);

        super.onDestroy();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        this.resumed = true;

        if(!this.serviceRunning)
        {
            if(this.scannerView != null)
            {
                this.scannerView.setResultHandler(this);
                this.scannerView.startCamera();
            }

            if(this.dialogScan != null)
            {
                this.dialogScan.show();
            }
        }
        else if(this.dialogProgress != null)
        {
            this.dialogProgress.show();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        this.resumed = false;

        if(this.scannerView != null)
            this.scannerView.stopCamera();

        if(this.dialogScan != null)
            this.dialogScan.dismiss();

        if(this.dialogError != null)
            this.dialogError.dismiss();

        if(this.dialogLoadError != null)
            this.dialogLoadError.dismiss();

        if(this.dialogProgress != null)
            this.dialogProgress.dismiss();
    }

    @Override
    public void handleResult(Result rawResult)
    {
        try
        {
            FlatId flatId = new Gson().fromJson(rawResult.getText(), FlatId.class);
            if (flatId != null && flatId.getFlatId() != 0 && !TextUtils.isEmpty(flatId.getHash()))
            {
                PreferencesHelper.saveFlatId(this, flatId.getFlatId());
                PreferencesHelper.saveHash(this, flatId.getHash());
                this.startConfigApiCall();
            } else
            {
                this.dialogError.show();
            }
        }
        catch (JsonSyntaxException e)
        {
            this.dialogError.show();
        }
    }

    private boolean isFirstRun()
    {
        int id = PreferencesHelper.getFlatId(this);
        String hash = PreferencesHelper.getHash(this);
        long timestamp = PreferencesHelper.getTimestamp(this);
        return timestamp == 0 || id == 0 || TextUtils.isEmpty(hash);
    }

    private void startConfigApiCall()
    {
        this.dialogProgress.show();
        this.serviceRunning = true;
        Intent intent = new Intent(this, DataService.class);
        intent.putExtra(DataService.EXTRA_ACTION, DataService.ACTION_REFRESH);
        intent.putExtra(DataService.EXTRA_SEND_RESULT, true);
        this.startService(intent);
    }

    private void startMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    private BroadcastReceiver apiDataResultReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            ScanActivity.this.serviceRunning = false;
            ScanActivity.this.dialogProgress.dismiss();

            if(intent.getBooleanExtra(DataService.KEY_SUCCESS, false))
            {
                ScanActivity.this.startMainActivity();
            }
            else
            {
                PreferencesHelper.clearConfiguration(ScanActivity.this);
                if(ScanActivity.this.resumed)
                    ScanActivity.this.dialogLoadError.show();
            }
        }
    };
}
