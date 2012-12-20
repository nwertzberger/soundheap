package com.ideaheap.sound.ui;

import java.util.List;

import android.app.TabActivity;
import android.widget.TabHost;

import com.ideaheap.sound.R;
import com.ideaheap.sound.ui.tabs.TabBuilder;

public class MainView {

	public MainView(TabActivity activity, List<TabBuilder> builders, String activeTab) {
		activity.setContentView(R.layout.main);
		
		// add our tabs
		TabHost tabHost = activity.getTabHost();
		
		for (TabBuilder tabBuilder : builders) {
			tabBuilder.buildTab();
		}
		
        activity.getTabHost().setCurrentTabByTag(activeTab);
	}

}
