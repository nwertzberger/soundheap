package com.ideaheap.sound.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.ideaheap.sound.R;
import com.ideaheap.sound.context.SoundheapContext;
import com.ideaheap.sound.context.SoundheapException;
import com.ideaheap.sound.control.RecordController;

/**
 * This class is responsible for one thing: tying event handlers to the proper
 * function inside the recordController.
 * 
 * @author nwertzberger
 *
 */
public class RecordFragment extends SherlockFragment {
	private static final String TAG = RecordFragment.class.toString();
	
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstance) {
		
		final View v = inflater.inflate(R.layout.record, container, false);
		try {
			final SoundheapContext ctx = SoundheapContext.getContext();
			
			addUserInterfaceHooks(v, ctx.recordController);
			
			// Task: set any required info about the current state
			ctx.recordController.setupView(v);
		} catch (SoundheapException e) {
			e.printStackTrace();
		}
		return v;
	}

	private void addUserInterfaceHooks(
			final View v,
			final RecordController recordController) {
		
		// Task: attach any UI elements to listeners.
		final ImageButton recButton = (ImageButton)
				v.findViewById(R.id.RecordButton);
		final CheckBox ignoreSilenceBox = (CheckBox) 
				v.findViewById(R.id.IgnoreSilenceBox);
		
		recButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View parent) {
				recordController.toggleRecord(v);
			}
		});
		
		ignoreSilenceBox.setOnCheckedChangeListener(
				new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(
					CompoundButton buttonView,
					boolean isChecked) {
				recordController.setIgnoreSilence(isChecked);
			}
		});
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
}
