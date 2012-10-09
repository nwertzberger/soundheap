package com.ideaheap.sound.service;

import java.io.IOException;

import com.ideaheap.io.AudioInputStream;

import android.os.AsyncTask;
import android.test.AndroidTestCase;

public class AudioPlayServiceTest extends AndroidTestCase {
	Boolean updated = false;
	Boolean closed = false;
	Boolean read = false;
	
	AudioPlayService player = new AudioPlayService();
	
	public void testRun() throws Exception {
    	new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... filename) {
				try {
					player.setAudioUpdateListener(new AudioUpdateListener() {
						public void onUpdate(int trackLocation) {
							updated = true;
						}
					});
					player.startPlaying(new AudioInputStream() {
						@Override
						public void close() throws IOException {
							closed = true;
						}

						@Override
						public int read(short[] pcmBuffer, int offset,
								int length) throws IOException {
							read = true;
							return 0;
						}
					});
				} catch (Exception e) {
					
				}
				return null;
			}
    	}.execute();	
    	
    	Thread.sleep(500);
    	player.stopPlaying();
    	while(player.isPlaying()) {
    		Thread.sleep(100);
    	}
    	
    	assertEquals((Boolean)true, updated);
    	assertEquals((Boolean)true, closed);
    	assertEquals((Boolean)true, read);
	}
}
