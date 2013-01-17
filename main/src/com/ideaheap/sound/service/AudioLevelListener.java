package com.ideaheap.sound.service;

public interface AudioLevelListener {
	/*
	 * An int between 0 and 255
	 */
	public void onLevelChange(int level); 
}
