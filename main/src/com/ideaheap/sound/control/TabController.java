package com.ideaheap.sound.control;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TabController {
	
	private final TabListener listener;
	private final SherlockFragmentActivity activity;
	private final int titleId;

	public TabController(SherlockFragmentActivity activity, int titleId, TabListener listener) {
		this.activity = activity;
		this.titleId = titleId;
		this.listener = listener;
	}

	public void buildTab() {
		ActionBar bar = activity.getSupportActionBar();
        ActionBar.Tab project = bar.newTab().setText(titleId);
        project.setTabListener(listener);
        bar.addTab(project);
	}
}
