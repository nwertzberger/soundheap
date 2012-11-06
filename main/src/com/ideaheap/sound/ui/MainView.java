package com.ideaheap.sound.ui;

import java.util.List;

import android.app.TabActivity;

import com.ideaheap.sound.R;
import com.ideaheap.sound.ui.tabs.RecordTab;
import com.ideaheap.sound.ui.tabs.TabBuilder;

public class MainView {

	public MainView(TabActivity activity, List<TabBuilder> builders, String activeTab) {
		activity.setContentView(R.layout.main);
		// add our tabs
		for (TabBuilder tabBuilder : builders) {
			tabBuilder.addTab();
		}
        activity.getTabHost().setCurrentTabByTag(activeTab);
	}

}
