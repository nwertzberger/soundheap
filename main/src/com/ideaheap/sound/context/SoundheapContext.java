package com.ideaheap.sound.context;

import java.io.File;

import android.app.Activity;
import android.app.TabActivity;
import android.content.res.Resources;

import com.ideaheap.sound.R;
import com.ideaheap.sound.service.AudioPlayService;
import com.ideaheap.sound.service.AudioRecordService;
import com.ideaheap.sound.service.RepositoryService;
import com.ideaheap.sound.ui.MainView;
import com.ideaheap.sound.ui.TabBuilder;

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
		// Initialize the various services we use.
		AudioRecordService recorder = new AudioRecordService();
		AudioPlayService player = new AudioPlayService();
		
		// Wire up our base repository
		resources = activity.getResources();
		String REPOSITORY = resources.getString(R.string.repository);
		File repo = new File(REPOSITORY);
		RepositoryService repository = new RepositoryService(repo);
		
		// Initialize our tabBuilder
		builder = new TabBuilder();
	}
	
	// Things that should be re-generated every time the context is re-initialized
	private void requestScope(TabActivity activity) {
		// Build the ui views
		mainView = new MainView(activity, builder);
	}
}
