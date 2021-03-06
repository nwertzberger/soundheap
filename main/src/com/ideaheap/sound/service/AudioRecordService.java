package com.ideaheap.sound.service;

import com.ideaheap.io.AudioOutputStream;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * The record service is the main control module for dynamic recording.
 * It is responsible for initializing the microphone and copying data to
 * buffers.
 * 
 * It is also responsible for having hooks that can be called from the UI
 * for recording.
 */
public class AudioRecordService {
	
	// Debugging tag.
	private static final String TAG = AudioRecordService.class.toString();
	
	// Microphone settings (sample rate / buffers)
	private static final int sampleRateHz = 44100;
	private static int recordBufferSize;
	
	// The many states of recording a track
	private static final int RECORDING 	= 0;
	private static final int STOPPING 	= 1;
	private static final int STOPPED 		= 2;
	private static final int EXTRA_SAMPLES = 5;
	
	// holds sound for recording.
	private final short buffer[];
	
	// Stores where we are in the recording process
	private Integer 			recordState 		= STOPPED;
	private AudioUpdateListener audioUpdateListener = null;

	private com.ideaheap.sound.service.AudioLevelListener levelListener;

	/**
	 * Calculate the buffer sizes based on out
	 */
	static {
		// For unit testing.
		try {
			recordBufferSize = AudioRecord.getMinBufferSize(
				sampleRateHz,
				AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT
			);
		} catch (RuntimeException e) {
			recordBufferSize = 0;
		}
	}
	
	public AudioRecordService() {
		if (recordBufferSize < 0) {
			Log.e(TAG, "buffer size less than zero! WTF!?");
			buffer = null;
		}
		else {
			buffer = new short[recordBufferSize];
		}
	}

	public void stopRecording() {
		synchronized(recordState) {
			if (this.recordState == RECORDING) {
				this.recordState = STOPPING;
			}
		}
	}

	public void startRecording(AudioOutputStream vout) {
		final Integer state;
		synchronized (recordState) {
			state = recordState;
		}
		if (state == STOPPED)
		{
			Log.d(TAG, "Received Record Request");
			recordState = RECORDING;
			AudioRecord recorder = null;
			try {
				int framesRead = 0;
				recorder = new AudioRecord(
					MediaRecorder.AudioSource.DEFAULT,
					sampleRateHz,
					AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT,
					recordBufferSize*4
				);
			
				Log.d(TAG,"Starting Recording");
				recorder.startRecording();
				
				audioUpdateListener.onUpdate(0);
				while (recordState == RECORDING) {
					framesRead += recorder.read(buffer, 0, buffer.length);
					vout.write(buffer, 0, buffer.length);
					if (audioUpdateListener != null) {
						audioUpdateListener.onUpdate(framesRead / sampleRateHz);
					}
					if (levelListener != null) {
						levelListener.onLevelChange(buffer[0] & 0xff);
					}
				}
				
				// HACK: It keeps cutting me off!
				for (int i=0; i < EXTRA_SAMPLES; i++) { 
					framesRead += recorder.read(buffer, 0, buffer.length);
					vout.write(buffer, 0, buffer.length);
					if (audioUpdateListener != null) {
						audioUpdateListener.onUpdate(framesRead / sampleRateHz);
					}
				}
				
				Log.d(TAG,"Stopping Recording");
				recorder.stop();
				recorder.release();
				
				Log.d(TAG, "Finished.");
				vout.close();
			}
			catch(Throwable x) {
				Log.w(TAG, "Error recording!", x);
			}
			finally {
				recordState = STOPPED;
				if (audioUpdateListener != null) {
					audioUpdateListener.onUpdate(-1);
				}
			}
		}
	}
	
	public boolean isRecording() {
		return recordState != STOPPED;
	}
	
	public void setAudioUpdateListener(AudioUpdateListener updateListener) {
		this.audioUpdateListener = updateListener;
	}
	
	public void setAudioLevelListener(AudioLevelListener levelListener) {
		this.levelListener = levelListener;
	}

	public static int getSampleRateHz() {
		return sampleRateHz;
	}

}
