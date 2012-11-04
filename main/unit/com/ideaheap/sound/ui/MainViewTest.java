package com.ideaheap.sound.ui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ideaheap.sound.ui.MainView;

import android.app.TabActivity;
import android.content.res.Resources;
import android.widget.TabHost;

public class MainViewTest {
	
	@Mock
	TabActivity activity;
	
	@Mock
	TabHost host;
	
	@Mock
	Resources resources;
	
	@Mock
	TabBuilder builder;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(activity.getTabHost()).thenReturn(host);
		Mockito.when(activity.getResources()).thenReturn(resources);
	}
	
	@Test
	public void generatesTabView() {
		MainView mainView = new MainView(activity, builder);
		Mockito.verify(activity).getTabHost();
	}
}
