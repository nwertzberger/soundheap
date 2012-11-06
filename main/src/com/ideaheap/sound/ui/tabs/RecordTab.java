package com.ideaheap.sound.ui.tabs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.ideaheap.io.AudioOutputStream;
import com.ideaheap.io.VorbisFileOutputStream;
import com.ideaheap.io.VorbisInfo;
import com.ideaheap.sound.R;
import com.ideaheap.sound.io.AudioLevelListener;
import com.ideaheap.sound.io.LevelActivatedOutputStream;
import com.ideaheap.sound.service.AudioRecordService;
import com.ideaheap.sound.service.AudioUpdateListener;
import com.ideaheap.sound.service.RepositoryService;

public class RecordTab extends TabBuilder {
	public static final String RECORD_TAB = "rec";
	private static final String TAG = "RecordTab";
	
	private final Resources res;
	private final AudioRecordService recorder;
	private final Activity activity;
	private final TabHost tabHost;
	private final ProjectTab projectTab;
	private final PlaybackTab playbackTab;
	private final RepositoryService repository;

	public RecordTab(
			Activity activity,
			TabHost tabHost,
			Resources res,
			AudioRecordService recorder,
			ProjectTab projectTab,
			PlaybackTab playbackTab,
			RepositoryService repository) {
		super(tabHost);
		this.activity = activity;
		this.tabHost = tabHost;
		this.res = res;
		this.recorder = recorder;
		this.projectTab = projectTab;
		this.playbackTab = playbackTab;
		this.repository = repository;
	}
	
	@Override
	public void addTab() {
		createTab(R.id.record,
				RECORD_TAB,
				res.getText(R.string.record),
				R.drawable.ic_btn_speak_now);	
		 
        /**********************************************************************
         * Set up the recording tab layout.
         */
        activity.findViewById(R.id.RecordButton).setOnClickListener(new OnClickListener() {
			public String newTrack = null;
 			public void onClick(View parent) {
 				if (recorder.isRecording()) {
 			    	Log.d(TAG, "Stopping");
 					recorder.stopRecording();
 					projectTab.updateProjects();
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
	}
	
    private void updateRecordButton(int trackLocation) {
		ImageButton butt = (ImageButton) activity.findViewById(R.id.RecordButton);
		if (trackLocation < 0) {
			butt.setImageResource(R.drawable.big_ic_mic);
		}
		else {
			butt.setImageResource(R.drawable.big_ic_mic_on);
		}
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
 					String playbackFile = newTrack + "-take1" + ".ogg";
 					
 					playbackTab.setPlaybackFile(playbackFile);
					fileOut = repository.createNewVorbis(playbackFile);
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
     
}