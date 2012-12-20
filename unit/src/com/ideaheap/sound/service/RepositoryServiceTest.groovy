package com.ideaheap.sound.service;

import java.io.File;

import org.junit.Test;
import org.mockito.Mockito;

public class RepositoryServiceTest {
	
	@Test
	public void makesDirectoriesOnNewInstallation() {
		File repo = Mockito.mock(File.class);
		Mockito.when(repo.exists()).thenReturn(false);
		new RepositoryService(repo);
		Mockito.verify(repo).mkdirs();
	}
	
}
