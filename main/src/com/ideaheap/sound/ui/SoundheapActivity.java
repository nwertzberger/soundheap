package com.ideaheap.sound.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ideaheap.io.AudioOutputStream;
import com.ideaheap.io.VorbisFileInputStream;
import com.ideaheap.io.VorbisFileOutputStream;
import com.ideaheap.io.VorbisInfo;
import com.ideaheap.sound.Constants;
import com.ideaheap.sound.R;
import com.ideaheap.sound.context.SoundheapContext;
import com.ideaheap.sound.io.AudioLevelListener;
import com.ideaheap.sound.io.LevelActivatedOutputStream;
import com.ideaheap.sound.service.AudioPlayService;
import com.ideaheap.sound.service.AudioRecordService;
import com.ideaheap.sound.service.AudioUpdateListener;
import com.ideaheap.sound.ui.tabs.RecordTab;

import android.app.TabActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

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
    private String playbackFile = null; // the file to playback
    
	private SoundheapContext context;
	private Object projectTab;
	
	/** 
	 * Ensures presence of sound repository folder. It also sets up the UI.
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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