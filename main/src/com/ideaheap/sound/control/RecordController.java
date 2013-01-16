package com.ideaheap.sound.control;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ideaheap.sound.R;
import com.ideaheap.sound.service.AudioRecordService;

public class RecordController extends TabController {

	private final SherlockFragmentActivity activity;
	private final TabListener listener;
	private final AudioRecordService recorder;

	public RecordController(SherlockFragmentActivity activity, TabListener listener, AudioRecordService recorder) {
		super(activity, R.string.record_title, listener);
		this.activity = activity;
		this.listener = listener;
		this.recorder = recorder;
	}
	
	@Override
	public void buildTab() {
		super.buildTab();
	}
	
	public void setupView(View view) {
		/**
		 * Add that record button.
		 */
		ImageButton recButton = (ImageButton) view.findViewById(R.id.RecordButton);
		recButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View parent) {
				recorder.toggle();
			}
		});
	}
}
