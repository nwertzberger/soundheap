package com.ideaheap.sound.service;

import java.io.File;

public class RepositoryService {
	
	public RepositoryService(File repository) {
        /*
         * Make sure background stuff is set up.
         */
    	repository.mkdirs();
	}
}
