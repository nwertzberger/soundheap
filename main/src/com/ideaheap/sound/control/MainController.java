package com.ideaheap.sound.control;

import java.util.List;

import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.ideaheap.sound.R;

public class MainController {
	private static final String TAG = MainController.class.toString();
	public Fragment fragment;
	private final SherlockFragmentActivity activity;
	private final List<TabController> tabs;

	public MainController(SherlockFragmentActivity activity, List<TabController> tabs) {
		this.activity = activity;
		this.tabs = tabs;
	}
	
	public void setup() {
        activity.getSupportActionBar()
        	.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (TabController tab : tabs) {
			tab.buildTab();
		}
	}

	public void inflateMenu(Menu menu) {
    	MenuInflater inflater = activity.getSupportMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
	}
}
