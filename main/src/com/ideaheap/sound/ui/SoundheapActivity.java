package com.ideaheap.sound.ui;

import android.app.TabActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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
public class SoundheapActivity extends TabActivity {
    private static final String TAG = "SoundActivity";
	private SoundheapContext context;
	
	/** 
	 * Ensures presence of sound repository folder. It also sets up the UI.
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Starting Up");
        setContentView(R.layout.main);
        context = SoundheapContext.getContext(this);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.project_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	super.onContextItemSelected(item);
    	return context.getProjectTab().selectTrack(item);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }
}