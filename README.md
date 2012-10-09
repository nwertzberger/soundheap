soundheap
=========

Android Sound Recording ApplicaitonPRACTICEMATE
============

Architecture
------------

Practicemate decouples the UI from the services that run the tool as much as possible.

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

AudioRecordService: 		Interface for how recording should work.

AudioPlaybackService:		Interface for how playing sound back should work.

AudioMetadataService:		Interface for reading and writing Metadata.

PracticemateProject:		The data type for PracticeMate.  Since we don't
							have the ability to encode in anything other than
							PCM if we want to do real-time analysis of a sound stream,
							We save off the data into a ZipFile, which offers some compression.
							The project metadata, audiofiles, and locations are stored in
							XML. BECAUSE FUCK YOU!
							
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
							  - NNN.wav
							  - ...
							  - NNN.wav
							  