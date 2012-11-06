package com.ideaheap.sound.context;

import java.io.File;
import java.util.Arrays;

import android.app.Activity;
import android.app.TabActivity;
import android.content.res.Resources;
import android.widget.TabHost;

import com.ideaheap.sound.R;
import com.ideaheap.sound.service.AudioPlayService;
import com.ideaheap.sound.service.AudioRecordService;
import com.ideaheap.sound.service.RepositoryService;
import com.ideaheap.sound.ui.MainView;
import com.ideaheap.sound.ui.tabs.PlaybackTab;
import com.ideaheap.sound.ui.tabs.ProjectTab;
import com.ideaheap.sound.ui.tabs.RecordTab;
import com.ideaheap.sound.ui.tabs.TabBuilder;

/**
 * Instead of using Spring or some other IOC framework, I have decided to
 * experiment with doing all of this wiring myself, as i prefer doing the whole
 * constructor arguments thing, and switching to this context is relatively
 * simple.
 * 
 * @author nwertzberger
 * 
 */
public class SoundheapContext {
	private static SoundheapContext ctx = null;
	/*
	 * SINGLETONS
	 */
	private Resources resources = null;
	private TabBuilder builder;
	
	/*
	 * REQUEST SCOPE
	 */
	private MainView mainView = null;
	private RepositoryService repository;
	private ProjectTab projects;
	private AudioRecordService recorder;
	private AudioPlayService player;
	
	// This is a Singleton class, so we can initialize singleton scope in the constructor.
	public SoundheapContext(Activity activity) {
		this.singletonScope(activity);
	}
	
	public static SoundheapContext getContext(TabActivity activity) {
		if (ctx == null) {
			ctx = new SoundheapContext(activity);
		}
		ctx.requestScope(activity);
		return ctx;
	}
	
	private void singletonScope(Activity activity) {
		recorder = new AudioRecordService();
		player = new AudioPlayService();
		
		// Wire up our base repository
		resources = activity.getResources();
		String REPOSITORY = resources.getString(R.string.repository);
		File repo = new File(REPOSITORY);
		repository = new RepositoryService(repo);
	}
	
	// Things that should be re-generated every time the context is re-initialized
	private void requestScope(TabActivity activity) {
		Resources res = activity.getResources();
		TabHost tabHost = activity.getTabHost();

		PlaybackTab playback = new PlaybackTab(activity, tabHost, res, repository, player);
		RecordTab record = new RecordTab(activity, tabHost, res, recorder, projects, playback, repository);
		projects = new ProjectTab(activity, tabHost, res, repository);
		
		// Build the ui views
		mainView = new MainView(
			activity,
			Arrays.asList(
				record,
				playback,
				projects
			),
			RecordTab.RECORD_TAB
		);
	}

	public ProjectTab getProjectTab() {
		return projects;
	}
}
