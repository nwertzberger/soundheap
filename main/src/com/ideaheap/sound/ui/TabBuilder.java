package com.ideaheap.sound.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.ideaheap.sound.R;

public class TabBuilder {

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
			final TabHost tabHost,
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
