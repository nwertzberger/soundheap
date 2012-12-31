package com.ideaheap.sound.ui.tabs;

import java.io.IOException;

import android.app.Activity;
import android.app.TabActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

import com.ideaheap.io.VorbisFileInputStream;
import com.ideaheap.sound.R;
import com.ideaheap.sound.service.AudioPlayService;
import com.ideaheap.sound.service.AudioUpdateListener;
import com.ideaheap.sound.service.RepositoryService;

public class PlaybackTab implements TabBuilder {
	public static final String PLAYBACK_TAB = "play";
	private static final String TAG = "PlaybackTab";

	private final Resources res;

	private final Activity activity;

	private final AudioPlayService player;
	private final RepositoryService repository;
	private final TabHost tabHost;

	public PlaybackTab(TabActivity activity, RepositoryService repository, AudioPlayService player) {
		this.activity = activity;
		this.tabHost = activity.getTabHost();
		this.res = activity.getResources();
		this.repository = repository;
		this.player = player;
	}
	
	@Override
	public void buildTab() {
        // Tab for Videos
        TabSpec spec = tabHost.newTabSpec(PLAYBACK_TAB);
        try { // TODO: For unit testing (until we refactor this)
	        Intent intent = new Intent(activity, PlaybackTab.class);
	        spec.setContent(intent);
        } catch(RuntimeException e) {
        	// Don't really care TBH
        }
        	
        tabHost.addTab(spec);
        activity.findViewById(R.id.PlaybackButton).setOnClickListener(listener);
        
		// Share Me! Button
			new OnClickListener() {
				@Override
				public void onClick(View parent) {
					if (repository.getActiveTrack() != null) {
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("audio/vorbis");
						intent.putExtra(Intent.EXTRA_STREAM,
								repository.getActiveTrackUri());
						try {
							activity.startActivity(Intent.createChooser(
									intent,
									activity.getText(R.string.send_action)));
						} catch (ActivityNotFoundException ex) {
							Toast.makeText(parent.getContext(),
									R.string.no_intent_to_send,
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			};
	} 

	public void setPlaybackFile(String file) {

	}

	// Playback control
	private OnClickListener listener = new OnClickListener() {
		public void onClick(View parent) {
			if (player.isPlaying()) {
				Log.d(TAG, "Stopping");
				player.stopPlaying();
			} else {
				Log.d(TAG, "Starting");
				AsyncTask<String, Integer, Void> playback = new AsyncTask<String, Integer, Void>() {
					@Override
					protected Void doInBackground(String... filename) {
						try {
							VorbisFileInputStream stream = new VorbisFileInputStream(
									filename[0]);
							player.setAudioUpdateListener(new AudioUpdateListener() {
								@Override
								public void onUpdate(int trackLocation) {
									publishProgress(trackLocation);
								}
							});
							player.startPlaying(stream);
						} catch (IOException e) {
							Log.e(TAG,
									"Can't play back "
											+ repository.getActiveTrack(), e);
						}
						return null;
					}

					@Override
					protected void onProgressUpdate(Integer... progress) {
						updatePlaybackButton(progress[0]);
					}

					private void updatePlaybackButton(Integer integer) {
						Log.d(TAG,"Forgot what i shoud do here..." + integer);
					}
				};
				playback.execute();
			}
		}
	};

}
