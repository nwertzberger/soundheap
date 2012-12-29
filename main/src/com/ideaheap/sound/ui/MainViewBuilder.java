package com.ideaheap.sound.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ideaheap.sound.R;

public class MainViewBuilder {

	private final SherlockFragmentActivity activity;

	public MainViewBuilder(SherlockFragmentActivity activity) {
		this.activity = activity;
	}
	
	public void build() {
		ActionBar bar = activity.getSupportActionBar();
		bar.addTab(bar.newTab().setText(R.string.record_title));
	}

}
