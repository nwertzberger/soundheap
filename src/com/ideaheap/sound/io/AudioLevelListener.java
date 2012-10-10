package com.ideaheap.sound.io;

import com.ideaheap.io.AudioOutputStream;

public interface AudioLevelListener {
	/**
	 * This offers an opportunity to either return a new output stream,
	 * do something to an existing output stream, or jsut do nothing.
	 * @param stream
	 * @return
	 */
	public AudioOutputStream onQuiet(AudioOutputStream stream);
	public AudioOutputStream onLoud(AudioOutputStream stream);
}
