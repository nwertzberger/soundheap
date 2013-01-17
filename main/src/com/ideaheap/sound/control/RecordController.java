package com.ideaheap.sound.control;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ideaheap.sound.R;
import com.ideaheap.sound.service.AudioLevelListener;
import com.ideaheap.sound.service.AudioRecordService;
import com.ideaheap.sound.service.AudioUpdateListener;
import com.ideaheap.sound.service.RepositoryService;

public class RecordController extends TabController implements AudioLevelListener, AudioUpdateListener{

	private final AudioRecordService recorder;
	private final RepositoryService repo;
	private ImageButton recButton;
	private boolean ignoreSilence;
	private String saveFile = "soundheap.ogg";
	private final SherlockFragmentActivity activity;
	
	private Runnable getRecordSession(final String saveFile) {
		return new Runnable() {
			public void run() {
				try {
					if (ignoreSilence) {
						// TODO add the smarts back in here
						recorder.startRecording(repo.createNewVorbis(saveFile));
					}
					else {
						recorder.startRecording(repo.createNewVorbis(saveFile));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public RecordController(
			SherlockFragmentActivity activity,
			TabListener listener,
			AudioRecordService recorder,
			RepositoryService repo) {
		super(activity, R.string.record_title, listener);
		this.activity = activity;
		this.recorder = recorder;
		this.repo = repo;
		
		recorder.setAudioLevelListener(this);
	}
	
	public void setupView(View view) {
		updateRecordInfo(view, recorder.isRecording(), saveFile);
	}
	
	private void updateRecordInfo(View v, boolean recording, String saveFile) {
		recButton = (ImageButton)v.findViewById(R.id.RecordButton);
		TextView    recText = (TextView)v.findViewById(R.id.RecordText);
		if (recording) {
			recButton.setImageResource(R.drawable.device_access_mic);
			recButton.setBackgroundResource(R.drawable.circle_button_selected);
			recText.setText(R.string.recording);
			toggleInteractiveNaming(v, true, saveFile);
		}
		else {
			recButton.setImageResource(R.drawable.device_access_mic_muted);
			recButton.setBackgroundResource(R.drawable.circle_button);
			recText.setText(R.string.ready);
			toggleInteractiveNaming(v, false, saveFile);
		}
	}

	private void toggleInteractiveNaming(View v, boolean lock, String saveFile) {
		int interactiveVisibility = View.VISIBLE;
		int lockedinVisibility = View.VISIBLE;
		
		if (lock) interactiveVisibility = View.INVISIBLE;
		else lockedinVisibility = View.INVISIBLE;
		
		TextView    actualFileToSave = (TextView)v.findViewById(R.id.RecordFileName);
		TextView    saveFileEnding = (TextView)v.findViewById(R.id.RecordFilePostFix);
		EditText    saveFilePrefix = (EditText)v.findViewById(R.id.RecordFilePrefix);
		
		actualFileToSave.setText(saveFile);
		
		actualFileToSave.setVisibility(lockedinVisibility);
		saveFileEnding.setVisibility(interactiveVisibility);
		saveFilePrefix.setVisibility(interactiveVisibility);
	}

	public void toggleRecord(View v) {
		
		// Figure out the save file's name
		final EditText filePrefix = (EditText) v.findViewById(R.id.RecordFilePrefix);
		Calendar c = Calendar.getInstance();
		String timestamp = new SimpleDateFormat("yy.MM.dd-HH.mm.ss").format(c.getTime());	
		saveFile = filePrefix.getText().toString() + "." + timestamp + ".ogg";
		
		if (recorder.isRecording()) {
			recorder.stopRecording();
			updateRecordInfo(v, false, saveFile);
		}
		else {
			new Thread(getRecordSession(saveFile)).start();
			updateRecordInfo(v, true, saveFile);
		}
	}

	public void setIgnoreSilence(boolean ignoreSilence) {
		this.ignoreSilence = ignoreSilence;
	}

	@Override
	public void onLevelChange(final int level) {
		//TODO
	}

	@Override
	public void onUpdate(int trackLocation) {
		//TODO
		
	}
}
