package com.ideaheap.sound.io;

import java.io.IOException;

import com.ideaheap.io.AudioOutputStream;

import android.util.Log;

public class LevelActivatedOutputStream extends AudioOutputStream {
	
	private static final String TAG = "LevelActivatedOutputStream";

	private int quietLevel = 0x0ffffff;	// bigger than a short
	private int loudLevel = 0;
	
	private int waitFrames;
	private int waitTime;
	
	private int framesWaited = 0;
	
	private AudioOutputStream outStream;

	private AudioLevelListener levelListener;
	
	public LevelActivatedOutputStream(AudioOutputStream out, int pauseTimeMillis) {
		Log.d(TAG, "Creating a new stream");
		this.outStream = out;
		
		waitTime = pauseTimeMillis;
		waitFrames = waitTime * out.getSampleRate() / 1000;
	}
	
	@Override
	public int write(final short[] buffer, final int offset, final int length) throws IOException {
		short maxLevel = 0, minLevel = 0;
		int frames = 0;
		
		// Determine the maximum amplitude.
		final int lastidx = offset + length;
		for (int i = offset; i < lastidx; i++) {
			if (buffer[i] < minLevel) {
				minLevel = buffer[i];
			}
			else if (buffer[i] > maxLevel) {
				maxLevel = buffer[i];
			}
		}
		
		// Update levels
		final int amplitude = maxLevel - minLevel;
		if ( amplitude > loudLevel) {
			loudLevel = amplitude;
		}
		if (amplitude  < quietLevel && amplitude > 0) {
			quietLevel = amplitude;
		}
		
		// Determine if we're activated.
		if (amplitude > (loudLevel + quietLevel) >> 1 + (loudLevel + quietLevel) >> 2) {
			framesWaited = waitFrames;
		}
		
		// We're active
		if (framesWaited > 0) {
			frames = outStream.write(buffer, offset, length);
			framesWaited -= length;
		}
		
		return frames;
	}

	@Override
	public void close() throws IOException {
		outStream.close();
	}

	@Override
	public int getSampleRate() {
		return outStream.getSampleRate();
	}

	public void setLevelListener(AudioLevelListener audioLevelListener) {
		this.levelListener = audioLevelListener;
	}
}
