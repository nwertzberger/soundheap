soundheap
=========

Android Sound Recording Applicaiton

Link: https://play.google.com/store/apps/details?id=com.ideaheap.sound

Dependencies
------------

This project depends on libogg-vorbis-android and ActionBarSherlock. Both of
these need to be added as library projects. You will need to configure the
locations of these projects in main/project.properties.

Architecture
------------

SoundHeap decouples the UI from the services that run the tool as much as possible.

UI
--
The UI is what it is. It is not unit tested. Sorry...

- PracticeMateActivity: Responsible for controlling the specific views.
	- RecordActivity: 	Simple record view that notes the recording state of the
	    				device, and what the recording is being saved as.
	- PlaybackActivity:	Simply plays an item back. Also offers track metadata
						Editing (e.g. project, filename, filetype?)
	- ProjectsActivity: Allows you to choose between projects that are
						previously recorded.
						
Services
--------

Services are designed to be modular, amazing, and unit tested.

AudioRecordService: Interface for how recording should work.

AudioPlaybackService: Interface for how playing sound back should work.

AudioMetadataService: Interface for reading and writing Metadata.

SoundHeap Project: The potential data file for SoundHeap.
Zip Layout:
- project.xml
    - name
    - beat
    - meter
    - key
    - takes
        - fileName
        - startTime

  - picture.jpg
  - NNN.ogg
  - ...
  - NNN.ogg
  
