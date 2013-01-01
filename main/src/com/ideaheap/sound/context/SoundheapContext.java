package com.ideaheap.sound.context;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.TabActivity;
import android.content.res.Resources;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ideaheap.sound.R;
import com.ideaheap.sound.control.MainController;
import com.ideaheap.sound.control.PlaybackController;
import com.ideaheap.sound.control.ProjectController;
import com.ideaheap.sound.control.RecordController;
import com.ideaheap.sound.control.TabController;
import com.ideaheap.sound.control.TabListener;
import com.ideaheap.sound.service.AudioPlayService;
import com.ideaheap.sound.service.AudioRecordService;
import com.ideaheap.sound.service.RepositoryService;
import com.ideaheap.sound.ui.PlaybackFragment;
import com.ideaheap.sound.ui.ProjectFragment;
import com.ideaheap.sound.ui.RecordFragment;
import com.ideaheap.sound.ui.SoundheapActivity;
import com.ideaheap.sound.ui.tabs.PlaybackTab;
import com.ideaheap.sound.ui.tabs.ProjectTab;
import com.ideaheap.sound.ui.tabs.RecordTab;

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

	// Singletons
	public final AudioRecordService recorder;
	public final AudioPlayService player;
	public final RepositoryService repository;
	
	// Request
	public MainController mainController;

	/**
	 * This is a Singleton class, so we can initialize singleton scope in the
	 * constructor.
	 * @param activity
	 */
	private SoundheapContext(SherlockFragmentActivity activity) {
		recorder = new AudioRecordService();
		player = new AudioPlayService();

		// Wire up our base repository
		Resources res = activity.getResources();
		String REPOSITORY = res.getString(R.string.repository);
		File repo = new File(REPOSITORY);
		repository = new RepositoryService(repo);
		
	}

	/**
	 *  Things that should be re-generated every time the context is
	 *  re-initialized
	 * @param activity
	 */
	private void requestScope(SherlockFragmentActivity activity) {
		List<TabController> tabs = new LinkedList<TabController>();
		
		tabs.add(new RecordController(
			activity,
			new TabListener(
				new RecordFragment(),
				R.id.fragment_container
			)
		));
		tabs.add(new PlaybackController(
			activity,
			new TabListener(
				new PlaybackFragment(),
				R.id.fragment_container
			)
		));
		tabs.add(new ProjectController(
			activity,
			new TabListener(
				new ProjectFragment(),
				R.id.fragment_container
			)
		));
		
		// Build the ui views
		mainController = new MainController(activity, tabs);
	}
	
	public static SoundheapContext getContext(SherlockFragmentActivity activity) {
		if (ctx == null) {
			ctx = new SoundheapContext(activity);
		}
		ctx.requestScope(activity);
		return ctx;
	}

}
