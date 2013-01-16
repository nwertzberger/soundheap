package com.ideaheap.sound.control;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;

public class TabListener implements ActionBar.TabListener {
	
	private static final String TAG = TabListener.class.toString();
	
	public final SherlockFragment fragment;

	private final int elementId;
	
	public TabListener(SherlockFragment fragment, int elementId) {
		this.fragment = fragment;
		this.elementId = elementId;
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (fragment == null) Log.v(TAG, "fragment is null");
		if (ft == null) Log.v(TAG, "fragment TRANSACTION is null");
		ft.replace(elementId, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}
}
