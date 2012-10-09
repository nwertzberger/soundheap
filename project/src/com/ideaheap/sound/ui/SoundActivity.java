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
import com.ideaheap.sound.io.AudioLevelListener;
import com.ideaheap.sound.io.LevelActivatedOutputStream;
import com.ideaheap.sound.service.AudioPlayService;
import com.ideaheap.sound.service.AudioRecordService;
import com.ideaheap.sound.service.AudioUpdateListener;

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
 * 
 * This class is responsible for interfacing with UI elements, and calling the
 * appropriate hooks into services.
 * @author nwertzberger
 *
 */
public class SoundActivity extends TabActivity {
    public static final String RECORD_TAB = "rec";
    public static final String PLAYBACK_TAB = "play";
    public static final String PROJECT_TAB = "proj";
    private static final String TAG = "SoundActivity";

	private AudioRecordService recorder = new AudioRecordService();
    private AudioPlayService   player   = new AudioPlayService();
    
    private String playbackFile = null; // the file to playback

	/** 
	 * Ensures presence of sound repository folder. It also sets up the UI.
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*
         * Make sure background stuff is set up.
         */
	    File mainDir = new File(Constants.REPOSITORY);
        if (!mainDir.exists()) {
        	mainDir.mkdirs();
        }
        
        /*********************************************************************
         * Set up the main layout.
         */
        
        this.setContentView(R.layout.main);
        
        Resources 		res 	= this.getResources();
        TabHost 		tabHost = this.getTabHost();
        
        // Add the Recording Intent
        tabHost.addTab(createTab(
        	R.id.record, 
        	RECORD_TAB,
        	res.getText(R.string.record),
        	R.drawable.ic_btn_speak_now
        ));
        
        // Add the Playback Intent
        tabHost.addTab(createTab(
        	R.id.playback,
        	PLAYBACK_TAB,
        	res.getText(R.string.playback),
        	R.drawable.ic_menu_equalizer
        ));
        
        // Add the Projects Intent
        tabHost.addTab(createTab(
        	R.id.projects,
        	PROJECT_TAB,
        	res.getText(R.string.projects),
        	R.drawable.ic_menu_cloud
        ));
        
        /**********************************************************************
         * Set up the recording tab layout.
         */
        findViewById(R.id.RecordButton).setOnClickListener(new OnClickListener() {
			public String newTrack = null;
 			public void onClick(View parent) {
 				if (recorder.isRecording()) {
 			    	Log.d(TAG, "Stopping");
 					recorder.stopRecording();
 					updateProjects();
 				}
 				else {
 					// Calculate the new filename.
					Calendar c = Calendar.getInstance();
					String timestamp = new SimpleDateFormat("yy.MM.dd-HH.mm.ss").format(c.getTime());
					newTrack = "session-" + timestamp;
 			    	Log.d(TAG, "Starting");
 			    	createRecordingTask().execute(newTrack);
 				}
 			}
         });
		
        /**********************************************************************
         * Set up the playback tab.
         */
        // Share Me! Button
        findViewById(R.id.ShareButton).setOnClickListener(new OnClickListener () {
        	@Override
        	public void onClick(View parent) {
        		if (playbackFile != null) {
	        		Intent intent = new Intent(Intent.ACTION_SEND);
	        		intent.setType("audio/vorbis");
	        		intent.putExtra(Intent.EXTRA_STREAM,
        				Uri.parse("file:///" + Constants.REPOSITORY + playbackFile)
        			);
	        		try {
	        			startActivity(Intent.createChooser(intent, getText(R.string.send_proj)));
	        		}
	        		catch (ActivityNotFoundException ex) {
	        			Toast.makeText(parent.getContext(), R.string.no_intent_to_send, Toast.LENGTH_SHORT);
	        		}
	        	}
        	}
        });
        // Playback control
        findViewById(R.id.PlaybackButton).setOnClickListener(new OnClickListener() {
			public void onClick(View parent) {
				if (player.isPlaying()) {
			    	Log.d(TAG, "Stopping");
					player.stopPlaying();
				}
				else {
			    	Log.d(TAG, "Starting");
			    	AsyncTask<String, Integer, Void> playback = new AsyncTask<String, Integer, Void>() {
						@Override
						protected Void doInBackground(String... filename) {
							try {
								VorbisFileInputStream stream = new VorbisFileInputStream(filename[0]);
								player.setAudioUpdateListener(new AudioUpdateListener() {
									@Override
									public void onUpdate(int trackLocation) {
										publishProgress(trackLocation);
									}
								});
								player.startPlaying(stream);
							} catch (IOException e) {
								Log.e(TAG,"Can't play back " + playbackFile, e);
							}
							return null;
						}
						
						@Override
						protected void onProgressUpdate(Integer ... progress) {
							updatePlaybackButton(progress[0]);
						}
			    	};
			    	playback.execute(Constants.REPOSITORY + playbackFile);
				}
			}
        });
       
		/*********************************************************************
		 * Set up the Projects tab.
		 */
		
		/*********************************************************************
		 * Finish Up.
		 */
        //  Open up with a recorder.
        tabHost.setCurrentTabByTag(RECORD_TAB);
    }
    
    /**
     * This builds the AsyncTask for recording.
     * TODO: Break this out into another file.
     * @return
     */
 	private AsyncTask<String, Integer, Void> createRecordingTask() {
 		return new AsyncTask<String, Integer, Void>() {
			final VorbisInfo info = new VorbisInfo();
			VorbisFileOutputStream fileOut;
			@Override
			protected Void doInBackground(String... params) {
				final String newTrack = params[0];
				recorder.setAudioUpdateListener(new AudioUpdateListener() {
					@Override
					public void onUpdate(int trackLocation) {
						publishProgress(trackLocation);
					}
				});
				try {
					info.sampleRate = AudioRecordService.getSampleRateHz();
 					playbackFile = newTrack + "-take1" + ".ogg";
					fileOut = new VorbisFileOutputStream(
						Constants.REPOSITORY + playbackFile,
						info
					);
					LevelActivatedOutputStream stream = new LevelActivatedOutputStream(fileOut, 1000);
					
					// TODO: get this right.
					stream.setLevelListener(new AudioLevelListener() {
						@Override
						public AudioOutputStream onQuiet(AudioOutputStream stream) {
							return null;
						}

						@Override
						public AudioOutputStream onLoud(AudioOutputStream stream) {
							// TODO Auto-generated method stub
							return null;
						}
					});
 					recorder.startRecording(stream);
 					
				} catch (IOException e) {
					Log.e(TAG, "Can't create vorbis stream", e);
				}
				return null;
			}
			@Override
			protected void onProgressUpdate(Integer ... progress) {
				updateRecordButton(progress[0]);
			}
	 	};
 	}
     
	private void updatePlaybackButton(int trackLocation) {
		ImageButton butt = (ImageButton) findViewById(R.id.PlaybackButton);
		if (trackLocation < 0) {
			butt.setImageResource(R.drawable.big_ic_media_play);
		}
		else {
			butt.setImageResource(R.drawable.big_ic_media_pause);
		}
	}
	
    private void updateRecordButton(int trackLocation) {
		ImageButton butt = (ImageButton) findViewById(R.id.RecordButton);
		if (trackLocation < 0) {
			butt.setImageResource(R.drawable.big_ic_mic);
		}
		else {
			butt.setImageResource(R.drawable.big_ic_mic_on);
		}
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
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		String file = ((TextView)info.targetView).getText().toString();
        switch (item.getItemId()) {
            case R.id.open_project:
				Toast.makeText(getApplicationContext(), "Loading " + file, Toast.LENGTH_SHORT).show();
				playbackFile = file;
				getTabHost().setCurrentTabByTag(PLAYBACK_TAB);
                return true;
            case R.id.delete_project:
				if (new File(Constants.REPOSITORY + file).delete()) {
					Toast.makeText(getApplicationContext(), "Deleted " + file, Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(), "Error Deleting " + file, Toast.LENGTH_SHORT).show();
				}
				this.updateProjects();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	this.recorder.stopRecording();	
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	this.updateProjects();
    }
    
    /**
     * Updates the project tab with the latest Projects.
     */
	private void updateProjects() {
	    String[] recordFiles = new File(Constants.REPOSITORY).list();
        if (recordFiles != null) {
	        ListView lv = (ListView) findViewById(R.id.RecordList);
	        lv.setAdapter(new ArrayAdapter<String>(this,R.layout.project_item, recordFiles));
	        lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String file = ((TextView) view).getText().toString();
					Toast.makeText(getApplicationContext(), "Loading " + file, Toast.LENGTH_SHORT).show();
					playbackFile = file;
					getTabHost().setCurrentTabByTag(PLAYBACK_TAB);
				}
	        });
	        registerForContextMenu(findViewById(R.id.RecordList));
        }
    }
	
    /**
     * Creates a tab with text and an image.
     * @param tabId
     * @param tag
     * @param charSequence
     * @param drawable
     * @return
     */
    private TabSpec createTab(
    		final int tabId,
    		final String tag, 
            final CharSequence charSequence,
            final int drawable)
    {
        final View tab = LayoutInflater.from(getTabHost().getContext()).
            inflate(R.layout.tab, null);
        ((TextView)tab.findViewById(R.id.tab_text)).setText(charSequence);
        ((ImageView)tab.findViewById(R.id.tab_icon)).setImageResource(drawable);
        return getTabHost().newTabSpec(tag).setIndicator(tab).setContent(tabId);
    } 
}