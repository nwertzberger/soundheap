package com.ideaheap.sound.control;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ideaheap.sound.R;

public class RecordController extends TabController {

	public RecordController(SherlockFragmentActivity activity, TabListener listener) {
		super(activity, R.string.record_title, listener);
	}

}
