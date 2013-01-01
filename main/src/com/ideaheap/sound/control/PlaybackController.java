package com.ideaheap.sound.control;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ideaheap.sound.R;

public class PlaybackController extends TabController {

	public PlaybackController(SherlockFragmentActivity activity, TabListener listener) {
		super(activity, R.string.playback_title, listener);
	}

}
