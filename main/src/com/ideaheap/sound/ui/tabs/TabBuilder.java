package com.ideaheap.sound.ui.tabs;

import android.app.TabActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.ideaheap.sound.R;

public abstract class TabBuilder {
	private final TabHost tabHost;
	public TabBuilder(TabHost tabHost) {
		this.tabHost = tabHost;
	}
	
	public abstract void addTab();
	
	
	/**
	 * Creates a tab with text and an image.
	 * 
	 * @param tabId
	 * @param tag
	 * @param charSequence
	 * @param drawable
	 * @return
	 */
	public void createTab(
			final int tabId,
			final String tag,
			final CharSequence charSequence,
			final int drawable) {
		final View tab = LayoutInflater.from(tabHost.getContext())
				.inflate(R.layout.tab, null);
		((TextView) tab.findViewById(R.id.tab_text))
			.setText(charSequence);
		((ImageView) tab.findViewById(R.id.tab_icon))
			.setImageResource(drawable);
		tabHost.addTab(tabHost.newTabSpec(tag)
				.setIndicator(tab)
				.setContent(tabId)
			);
	}

}
