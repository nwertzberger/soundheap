package com.ideaheap.sound.control;

import java.io.IOException;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ideaheap.sound.R;
import com.ideaheap.sound.service.AudioRecordService;
import com.ideaheap.sound.service.RepositoryService;

public class RecordController extends TabController {

	private final AudioRecordService recorder;
	private final RepositoryService repo;
	private String startingText = "soundheap";

	public RecordController(
			SherlockFragmentActivity activity,
			TabListener listener,
			AudioRecordService recorder,
			RepositoryService repo) {
		super(activity, R.string.record_title, listener);
		this.recorder = recorder;
		this.repo = repo;
	}
	
	public void setupView(View view) {
		updateRecordInfo(view, recorder.isRecording());
	}
	
	public void setStartingText(String text) {
		this.startingText = text;
	}

	private void updateRecordInfo(View v, boolean recording) {
		ImageButton recButton = (ImageButton)v.findViewById(R.id.RecordButton);
		TextView    recText = (TextView)v.findViewById(R.id.RecordText);
		if (recording) {
			recButton.setImageResource(R.drawable.device_access_mic);
			recText.setText(R.string.recording);
			toggleInteractiveNaming(v, true);
		}
		else {
			recButton.setImageResource(R.drawable.device_access_mic_muted);
			recText.setText(R.string.ready);
			toggleInteractiveNaming(v, false);
		}
	}

	private void toggleInteractiveNaming(View v, boolean lock) {
		int interactiveVisibility = View.VISIBLE;
		int lockedinVisibility = View.VISIBLE;
		
		if (lock) interactiveVisibility = View.INVISIBLE;
		else lockedinVisibility = View.INVISIBLE;
		
		TextView    actualFileToSave = (TextView)v.findViewById(R.id.RecordFileName);
		TextView    saveFileEnding = (TextView)v.findViewById(R.id.RecordFilePostFix);
		EditText    saveFilePrefix = (EditText)v.findViewById(R.id.RecordFilePrefix);
		
		actualFileToSave.setText(startingText + ".ogg");
		
		actualFileToSave.setVisibility(lockedinVisibility);
		saveFileEnding.setVisibility(interactiveVisibility);
		saveFilePrefix.setVisibility(interactiveVisibility);
	}

	public void toggleRecord(View v) {
		final EditText filePrefix = (EditText)
				v.findViewById(R.id.RecordFilePrefix);
		
		startingText = filePrefix.getText().toString();
		
		if (recorder.isRecording()) {
			recorder.stopRecording();
			updateRecordInfo(v, false);
		}
		else {
			new Thread() {
				public void run() {
					try {
						recorder.startRecording(repo.createNewVorbis(startingText + ".ogg"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			updateRecordInfo(v, true);
		}
	}

	public void setIgnoreSilence(boolean isChecked) {
		
	}
}
