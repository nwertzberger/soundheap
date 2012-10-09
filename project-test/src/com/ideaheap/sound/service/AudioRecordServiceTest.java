package com.ideaheap.sound.service;

import java.io.IOException;

import android.os.AsyncTask;
import android.test.AndroidTestCase;

import com.ideaheap.io.AudioOutputStream;

public class AudioRecordServiceTest extends AndroidTestCase {
	Boolean closed = false;
	Boolean written = false;
	Boolean updated = false;
	
	AudioRecordService service = new AudioRecordService();
	
	/**
	 * This is a full test run. Didn't break this down any further.
	 * @throws Exception
	 */
	public void testRun() throws Exception {
		new AsyncTask<Void, Void, Void> () {
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				service.setAudioUpdateListener(new AudioUpdateListener() {
					@Override
					public void onUpdate(int trackLocation) {
						updated = true;
					}
					
				});
				service.startRecording(new AudioOutputStream() {
					@Override
					public int write(short[] buffer, int offset, int length)
							throws IOException {
						written = true;
						return 0;
					}
		
					@Override
					public void close() throws IOException {
						closed = true;
					}
				});
				return null;
			}
		}.execute();
		
		Thread.sleep(500);
		service.stopRecording();
		while(service.isRecording()) {
			Thread.sleep(100);
		}
		
		assertEquals((Boolean)true, written);
		assertEquals((Boolean)true, updated);
		assertEquals((Boolean)true, closed);
	}
}
