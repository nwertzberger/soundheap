package com.ideaheap.sound.control

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import com.actionbarsherlock.app.SherlockFragmentActivity
import com.ideaheap.sound.service.AudioRecordService

class RecordControllerTest {
	
	@Mock SherlockFragmentActivity activity
	@Mock TabListener listener
	@Mock AudioRecordService recorder
	
	@InjectMocks
	RecordController controller
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this)
	}

	@Test
	public void testName() throws Exception {
		
	}
}
