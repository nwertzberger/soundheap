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
        setContentView(R.layout.main);
        Log.d(TAG, "Starting Up");
        context = SoundheapContext.generateContext(this);
        context.mainController.setup();
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
    	context.mainController.inflateMenu(menu);
		return super.onCreateOptionsMenu(menu);
    }
}