package com.ideaheap.sound.ui.tabs;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.ideaheap.sound.R;
import com.ideaheap.sound.service.RepositoryService;

public class ProjectTab implements TabBuilder {
	public static final String PROJECT_TAB = "proj";

	private final Resources res;

	private final Activity activity;

	private final RepositoryService repository;

	private final TabHost tabHost;

	public ProjectTab(Activity activity, TabHost tabHost, Resources res,
			RepositoryService repository) {
		super();
		this.activity = activity;
		this.tabHost = tabHost;
		this.res = res;
		this.repository = repository;
	}

	@Override
	public void buildTab() {
        // Tab for Videos
        TabSpec spec = tabHost.newTabSpec(PROJECT_TAB);
        spec.setIndicator(res.getString(R.id.projects_pane),
        		res.getDrawable(R.drawable.ic_menu_cloud));
        Intent intent = new Intent(activity, ProjectTab.class);
        spec.setContent(intent);
	} 
	
	/**
	 * Updates the project tab with the latest Projects.
	 */
	public void updateProjects() {
		String[] recordFiles = repository.listRecordFiles();
		if (recordFiles != null) {
			ListView lv = (ListView) activity.findViewById(R.id.RecordList);
			lv.setAdapter(new ArrayAdapter<String>(activity,
					R.layout.project_item, recordFiles));
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String file = ((TextView) view).getText().toString();
					Toast.makeText(activity.getApplicationContext(),
							"Loading " + file, Toast.LENGTH_SHORT).show();
					repository.setActiveTrack(file);
					tabHost.setCurrentTabByTag(PlaybackTab.PLAYBACK_TAB);
				}
			});
			activity.registerForContextMenu(activity
					.findViewById(R.id.RecordList));
		}
	}

	public boolean selectTrack(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		String file = ((TextView) info.targetView).getText().toString();
		switch (item.getItemId()) {
		case R.id.open_project:
			Toast.makeText(activity.getApplicationContext(), "Loading " + file,
					Toast.LENGTH_SHORT).show();
			repository.setActiveTrack(file);
			tabHost.setCurrentTabByTag(PlaybackTab.PLAYBACK_TAB);
			return true;
		case R.id.delete_project:
			if (repository.deleteTrack(file)) {
				Toast.makeText(activity.getApplicationContext(),
						"Deleted " + file, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity.getApplicationContext(),
						"Error Deleting " + file, Toast.LENGTH_SHORT).show();
			}
			this.updateProjects();
			return true;
		default:
			return activity.onContextItemSelected(item);
		}
	}
}
