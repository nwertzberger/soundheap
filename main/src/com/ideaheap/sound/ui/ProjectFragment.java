package com.ideaheap.sound.ui;

import com.actionbarsherlock.app.SherlockFragment;
import com.ideaheap.sound.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProjectFragment extends SherlockFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		return inflater.inflate(R.layout.projects, container, false);
	}
}
