package com.ideaheap.sound.control;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ideaheap.sound.R;

public class ProjectController extends TabController {
	public ProjectController(SherlockFragmentActivity activity, TabListener listener) {
		super(activity, R.string.project_title, listener);
	}
}
