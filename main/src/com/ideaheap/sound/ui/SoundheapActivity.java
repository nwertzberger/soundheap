package com.ideaheap.sound.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.ideaheap.sound.R;
import com.ideaheap.sound.context.SoundheapContext;

/**
 * The main ui of the simple audio recorder / playback / project selector.
 * This is the entry point for everything else.
 * 
 * This class is responsible for interfacing with UI elements, and calling the
 * appropriate hooks into services.
 * @author nwertzberger
 *
 */
public class SoundheapActivity extends SherlockFragmentActivity {
	
    private static final String TAG = SoundheapActivity.class.toString();
	private SoundheapContext context;
	private final SherlockFragmentActivity thisActivity = this;
	
	/** 
	 * Ensures presence of sound repository folder. It also sets up the UI.
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Starting Up");
        setContentView(R.layout.main);
        
        ActionBar bar = this.getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab record 	= bar.newTab().setText(R.string.record_title);
        ActionBar.Tab playback 	= bar.newTab().setText(R.string.playback_title);
        ActionBar.Tab project 	= bar.newTab().setText(R.string.project_title);
        
        Fragment recordFragment 	= new RecordFragment();
        Fragment playbackFragment 	= new PlaybackFragment();
        Fragment projectFragment 	= new ProjectFragment();
        
        record.setTabListener(new MyTabsListener(recordFragment));
        playback.setTabListener(new MyTabsListener(playbackFragment));
        project.setTabListener(new MyTabsListener(projectFragment));
        
        bar.addTab(record);
        bar.addTab(playback);
        bar.addTab(project);
    }
    
    class MyTabsListener implements ActionBar.TabListener {
        public Fragment fragment;

        public MyTabsListener(Fragment fragment) {
        this.fragment = fragment;
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub
            if (fragment == null) {
                Log.v(TAG, "fragment is null");
            }

            if (ft == null) {
                Log.v(TAG, "fragment TRANSACTION is null");
            }

            ft.replace(R.id.fragment_container, fragment);          
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub
            Toast.makeText(thisActivity.getApplicationContext(), "Reselected!", Toast.LENGTH_LONG).show();
        }

    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = this.getSupportMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
		return true;
    }

}