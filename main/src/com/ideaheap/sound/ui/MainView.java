package com.ideaheap.sound.ui;

import android.app.TabActivity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.ideaheap.sound.R;

public class MainView {
	public static final String RECORD_TAB = "rec";
	public static final String PLAYBACK_TAB = "play";
	public static final String PROJECT_TAB = "proj";

	public MainView(TabActivity activity, TabBuilder tabBuilder) {
		activity.setContentView(R.layout.main);
		
		Resources res = activity.getResources();
		TabHost tabHost = activity.getTabHost();

		// Add the Recording Intent
		tabBuilder.createTab(tabHost,
				R.id.record,
				RECORD_TAB,
				res.getText(R.string.record),
				R.drawable.ic_btn_speak_now);

		// Add the Playback Intent
		tabBuilder.createTab(tabHost,
				R.id.playback,
				PLAYBACK_TAB,
				res.getText(R.string.playback),
				R.drawable.ic_menu_equalizer);

		// Add the Projects Intent
		tabBuilder.createTab(tabHost,
				R.id.projects,
				PROJECT_TAB,
				res.getText(R.string.projects),
				R.drawable.ic_menu_cloud);
	}

}
