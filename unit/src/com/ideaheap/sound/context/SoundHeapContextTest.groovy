package com.ideaheap.sound.context

import static org.junit.Assert.*
import static org.mockito.Mockito.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import android.app.TabActivity
import android.content.res.Resources
import android.view.View
import android.widget.TabHost
import android.widget.TabHost.TabSpec

class SoundHeapContextTest {
	
	@Mock
	TabActivity activity
	@Mock
	View view
	@Mock
	Resources res
	@Mock
	TabHost host
	@Mock
	TabSpec spec
	
	SoundheapContext ctx
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this)
		when(activity.getResources()).thenReturn(res)
		when(activity.getTabHost()).thenReturn(host)
		when(activity.findViewById(anyInt())).thenReturn(view)
		when(res.getString(anyInt())).thenReturn("junk")
		when(host.newTabSpec(anyString())).thenReturn(spec)
		ctx = SoundheapContext.generateContext(activity)
	}
	
	@Test
	public void loadsUpWithoutErrors() throws Exception {
		ctx.requestScope(activity);
	}
}
