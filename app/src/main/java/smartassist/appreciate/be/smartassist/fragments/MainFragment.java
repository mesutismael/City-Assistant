package smartassist.appreciate.be.smartassist.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.contentproviders.ChatMessageContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ModuleContentProvider;
import smartassist.appreciate.be.smartassist.database.ChatMessageTable;
import smartassist.appreciate.be.smartassist.model.LayoutModule;
import smartassist.appreciate.be.smartassist.model.Module;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;
import smartassist.appreciate.be.smartassist.utils.ModuleHelper;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;
import smartassist.appreciate.be.smartassist.views.TileLayout;

/**
 * Created by Inneke on 26/01/2015.
 */
public class MainFragment extends Fragment implements TileLayout.OnTileClickListener, LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener
{
    private TileLayout layoutTiles;
    public static final String TAG = "MainFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        this.layoutTiles = (TileLayout) view.findViewById(R.id.layout_tiles);

        this.layoutTiles.setOnTileClickListener(this);
        this.layoutTiles.setOrientation(this.getResources().getConfiguration().orientation);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_MODULES, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_CHAT, null, this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(this.getActivity() != null && !this.getActivity().isFinishing())
        {
            this.layoutTiles.setNotifications(ModuleHelper.getModuleId(Module.NEWS), PreferencesHelper.getUnreadNews(this.getActivity()));
            PreferencesHelper.getPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onPause()
    {
        if(this.getActivity() != null && !this.getActivity().isFinishing())
        {
            PreferencesHelper.getPreferences(this.getActivity()).unregisterOnSharedPreferenceChangeListener(this);
        }
        super.onPause();
    }

    @Override
    public void onTileClick(Module module, int moduleId)
    {
        Intent intent = new Intent(this.getActivity(), module.getActivity());
        this.startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_MODULES:
                return new CursorLoader(this.getView().getContext(), ModuleContentProvider.CONTENT_URI, null, null, null, null);

            case Constants.LOADER_CHAT:
                int ownContactId = ContactHelper.getOwnContactId(this.getView().getContext());
                String selection = ChatMessageTable.TABLE_NAME + "." + ChatMessageTable.COLUMN_READ + " = 0"
                        + " AND " + ChatMessageTable.TABLE_NAME + "." + ChatMessageTable.COLUMN_CONTACT_ID + " != " + ownContactId;
                return new CursorLoader(this.getView().getContext(), ChatMessageContentProvider.CONTENT_URI, null, selection, null, null);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_MODULES:
                List<LayoutModule> layoutModules = new ArrayList<>();
                while (data.moveToNext())
                    layoutModules.add(LayoutModule.constructFromCursor(data));
                this.layoutTiles.setTiles(layoutModules);
                break;

            case Constants.LOADER_CHAT:
                int unreadMessageCount = data != null ? data.getCount() : 0;
                this.layoutTiles.setNotifications(ModuleHelper.getModuleId(Module.CHAT), unreadMessageCount);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        this.layoutTiles.setTiles(null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        this.layoutTiles.setOrientation(newConfig.orientation);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(PreferencesHelper.PREFERENCE_UNREAD_NEWS.equals(key))
        {
            int newsNotifications = sharedPreferences.getInt(key, 0);
            this.layoutTiles.setNotifications(ModuleHelper.getModuleId(Module.NEWS), newsNotifications);
        }
    }
}
