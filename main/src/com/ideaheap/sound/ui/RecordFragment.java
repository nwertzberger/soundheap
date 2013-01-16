package com.ideaheap.sound.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.ideaheap.sound.R;
import com.ideaheap.sound.context.SoundheapContext;
import com.ideaheap.sound.context.SoundheapException;
import com.ideaheap.sound.service.AudioRecordService;

public class RecordFragment extends SherlockFragment {
	private static final String TAG = RecordFragment.class.toString();
	
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View v = inflater.inflate(R.layout.record, container, false);
		try {
			SoundheapContext.getContext().recordController.setupView(v);
		} catch (SoundheapException e) {
			e.printStackTrace();
		}
		return v;
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
}
