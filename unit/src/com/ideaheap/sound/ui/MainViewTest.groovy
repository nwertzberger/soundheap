package com.ideaheap.sound.ui;

import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ideaheap.sound.ui.MainViewBuilder;
import com.ideaheap.sound.ui.tabs.TabBuilder;

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
		when(activity.getTabHost()).thenReturn(host);
		when(activity.getResources()).thenReturn(resources);
	}
	
	@Test
	public void generatesTabView() {
		new MainViewBuilder(activity, [builder], "FAKE");
		verify(activity, atLeastOnce()).getTabHost();
		verify(builder).buildTab();
		verify(host).setCurrentTabByTag("FAKE");
	}
}
