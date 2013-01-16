package com.ideaheap.sound.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.ideaheap.sound.R;

public class ProjectFragment extends SherlockFragment {
	
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		return inflater.inflate(R.layout.projects, container, false);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
}
