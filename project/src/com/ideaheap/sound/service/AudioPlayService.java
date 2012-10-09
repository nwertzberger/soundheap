package com.ideaheap.sound.service;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.ideaheap.io.AudioInputStream;

public class AudioPlayService {
	private static final String TAG = "AudioPlayService";
	
	// The many states of Playing a track
	private static final int PLAYING = 0;
	private static final int STOPPING = 1;
	private static final int STOPPED = 2;
	
	private final short buffer[];
	private Integer playState = STOPPED;
	private AudioUpdateListener audioUpdateListener = null;
	
	private static final int sampleRateHz = 44100;
	private static final int trackBufferSize;
	
	static {
		trackBufferSize = AudioTrack.getMinBufferSize(
				sampleRateHz,
				AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
	}
	
	public AudioPlayService() {
		if (trackBufferSize < 0) {
			Log.e(TAG,"track Buffer less than zero! WTF!?");
			buffer = null;
		}
		else {
			buffer = new short[trackBufferSize];
		}
	}

	public boolean isPlaying() {
		return this.playState != STOPPED;
	}

	public void stopPlaying() {
		synchronized(this.playState) {
			if (this.playState  == PLAYING) {
				this.playState = STOPPING;
			}
		}
	}

	public void startPlaying(AudioInputStream vin) {
		
		final int state;
		synchronized(playState) {
			state = playState;
		}
		
		if (state == STOPPED)
		{
			playState = PLAYING;
			try {
				AudioTrack track = new AudioTrack(
						AudioManager.STREAM_MUSIC,
						sampleRateHz,
						AudioFormat.CHANNEL_OUT_MONO,
						AudioFormat.ENCODING_PCM_16BIT,
						trackBufferSize*2,
						AudioTrack.MODE_STREAM);
			
				Log.d(TAG,"Starting Playback");
				
				track.play();
				
				int totalFramesRead = 0;
				while (playState == PLAYING) {
					audioUpdateListener.onUpdate(totalFramesRead);
					int framesRead = vin.read(buffer);
					if (framesRead < 0) break;
					track.write(buffer, 0, framesRead);
					totalFramesRead += framesRead;
				}
				
				track.flush();
				Log.d(TAG, "Finished.");
				
				if (playState == STOPPING) {
					track.stop();
				}
				track.release();
				Log.d(TAG,"Stopping Playback");
				
				vin.close();
			}
			catch(Throwable x) {
				Log.w(TAG, "Error Playing!", x);
			}
			finally {
				audioUpdateListener.onUpdate(-1);
			}
			playState = STOPPED;
		}	
	}

	public void setAudioUpdateListener(AudioUpdateListener updateListener) {
		this.audioUpdateListener = updateListener;
	}
}
