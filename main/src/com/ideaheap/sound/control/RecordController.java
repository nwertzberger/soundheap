package com.ideaheap.sound.control;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ideaheap.io.AudioOutputStream;
import com.ideaheap.io.VorbisFileOutputStream;
import com.ideaheap.sound.R;
import com.ideaheap.sound.service.AudioLevelListener;
import com.ideaheap.sound.service.AudioRecordService;
import com.ideaheap.sound.service.AudioUpdateListener;
import com.ideaheap.sound.service.RepositoryService;

public class RecordController extends TabController {

	protected static final String TAG = null;
	private final AudioRecordService recorder;
	private final RepositoryService repo;
	private ImageButton recButton;
	private boolean ignoreSilence;
	private String saveFile = "soundheap.ogg";
	private final SherlockFragmentActivity activity;
	private View activeView;
	
	/**
	 * This builds the AsyncTask for recording. TODO: Break this out into
	 * another file.
	 * 
	 * @return
	 */
	private AsyncTask<String, Integer, Void> createRecordingTask() {
		return new AsyncTask<String, Integer, Void>() {
			@Override
			protected Void doInBackground(String ... params) {
				recorder.setAudioUpdateListener(new AudioUpdateListener() {
					@Override
					public void onUpdate(int trackLocation) {
						publishProgress(trackLocation);
					}
				});
				
				try {
					if (ignoreSilence) {
						// TODO add the smarts back in here
						recorder.startRecording(repo.createNewVorbis(saveFile));
					}
					else {
						recorder.startRecording(repo.createNewVorbis(saveFile));
					}
				} catch (IOException e) {
					Log.e(TAG, "Can't create vorbis stream", e);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... progress) {
				updateRecordButton(progress[0]);
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
		
		activeView = v;
		if (recorder.isRecording()) {
			recorder.stopRecording();
		}
		else {
			createRecordingTask().execute();
		}
	}

	protected void updateRecordButton(Integer trackPos) {
		// We only need to update on start and stop
		if (trackPos <= 0) {
			updateRecordInfo(activeView, recorder.isRecording(), saveFile);
		}
	}
	
	public void setIgnoreSilence(boolean ignoreSilence) {
		this.ignoreSilence = ignoreSilence;
	}

}
