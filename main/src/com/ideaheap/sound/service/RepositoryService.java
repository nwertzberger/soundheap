package com.ideaheap.sound.service;

import java.io.File;
import java.io.IOException;

import android.net.Uri;

import com.ideaheap.io.VorbisFileOutputStream;
import com.ideaheap.io.VorbisInfo;

public class RepositoryService {
	
	private final File repository;
	private VorbisInfo info;
	private String playbackFile = null;

	public RepositoryService(File repository) {
		this.repository = repository;
        /*
         * Make sure background stuff is set up.
         */
    	repository.mkdirs();
	}

	public String[] listRecordFiles() {
		return repository.list();
	}

	public VorbisFileOutputStream createNewVorbis(String playbackFile) throws IOException {
		return new VorbisFileOutputStream(repository.getPath() + "/" + playbackFile, info);
	}

	public void setActiveTrack(String file) {
		playbackFile = file;
	}

	public boolean deleteTrack(String file) {
		return new File(repository.getPath() + "/" + file).delete();
	}

	public String getActiveTrack() {
		return playbackFile;
	}

	public Uri getActiveTrackUri() {
		return Uri.parse("file:///" + repository.getAbsolutePath() + "/" + playbackFile);
	}
}
