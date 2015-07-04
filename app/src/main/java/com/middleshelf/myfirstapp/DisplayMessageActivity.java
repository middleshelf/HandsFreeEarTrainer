package com.middleshelf.myfirstapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;
import org.androidaalto.soundfuse.sequencer.Sequencer;
import com.leff.midi.*;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileDescriptor;
import java.io.FileInputStream;


public class DisplayMessageActivity extends Activity {
    Sequencer sequencer;
    PlaySound playSound;
    private String file = "exampleout.mid";
    private MediaPlayer mediaPlayer = new MediaPlayer();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the message from the intent
	    Intent intent = getIntent();
	    String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

	    // Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    textView.setText(message);

        //Play ogg files
        /*sequencer = new Sequencer(this, 4, 8);
        sequencer.setSample(0, R.raw.bass);
        sequencer.enableCell(0, 0);
        sequencer.enableCell(0,2);
        sequencer.enableCell(0,4);
        sequencer.enableCell(0,6);
        sequencer.play();*/

        /*//Generate midi frequencies
        playSound = new PlaySound(880,1);
        playSound.genTone();
        playSound.playSound();
        playSound = new PlaySound(440,1);
        playSound.genTone();
        playSound.playSound();*/

        // 1. Create some MidiTracks
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();

        // 2. Add events to the tracks
        // 2a. Track 0 is typically the tempo map
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo t = new Tempo();
        t.setBpm(228);

        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(t);

        // 2b. Track 1 will have some notes in it
        for(int i = 0; i < 80; i++)
        {
            int channel = 0, pitch = 1 + i, velocity = 100;
            NoteOn on = new NoteOn(i * 480, channel, pitch, velocity);
            NoteOff off = new NoteOff(i * 480 + 120, channel, pitch, 0);

            noteTrack.insertEvent(on);
            noteTrack.insertEvent(off);

            // There is also a utility function for notes that you should use
            // instead of the above.
            noteTrack.insertNote(channel, pitch + 2, velocity, i * 480, 120);
        }

        // It's best not to manually insert EndOfTrack events; MidiTrack will
        // call closeTrack() on itself before writing itself to a file

        // 3. Create a MidiFile with the tracks we created
        ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);
        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

        // 4. Write the MIDI data to a file
        File midiFile = new File(getFilesDir() + "/" +file);
        try
        {
            midi.writeToFile(midiFile);
        }
        catch(IOException e)
        {
            System.err.println(e);
        }


        //Play midi file
        try {
            String filename = getFilesDir() + "/" + file;
            File midifile = new File(filename);
            FileInputStream inputStream = new FileInputStream(midifile);
            FileDescriptor fileDescriptor = inputStream.getFD();
            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fileDescriptor);
            inputStream.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

	    // Set the text view as the activity layout
	    setContentView(textView);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
            sequencer.stop();
            mediaPlayer.stop();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
