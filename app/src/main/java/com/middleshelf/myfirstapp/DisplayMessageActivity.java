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
import android.view.View;

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
import java.util.Random;


public class DisplayMessageActivity extends Activity {
    Sequencer sequencer;
    PlaySound playSound;
    private String file = "exampleout.mid";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String answer;
    private TextView answerText = null;
    private FileDescriptor fileDescriptor = new FileDescriptor();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
		// Get the message from the intent
	    Intent intent = getIntent();
	    String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String scale = intent.getStringExtra(MainActivity.SCALE);
        String tempo = intent.getStringExtra(MainActivity.TEMPO);
        String melodyLength = intent.getStringExtra(MainActivity.MELODY_LENGTH);

	    // Create the text view
        answerText = (TextView) findViewById(R.id.answerText);
        answerText.setTextSize(30);
	    //textView.setText(message);

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
        t.setBpm(Integer.parseInt(tempo));

        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(t);

        // 2b. Track 1 will have some notes in it
        // Middle C is integer 60
        int[] majorPentatoic = {60,62,64,67,69,72};
        int[] minorPentatoic = {60,63,65,67,70,72};
        int[] major = {60,62,64,65,67,69,71,72};
        int[] melodicMinor = {60,62,63,65,67,68,70,72};
        int[] harmonicMinor = {60,62,63,65,67,69,71,72};
        int[] chromatic = {60,61,62,63,64,65,66,67,68,69,70,71,72};

        int[] melody = {};
        int mLength = Integer.parseInt(melodyLength);
        int[] randMelody = new int[mLength];
        Random generator = new Random();

        //Determine which button was pressed to play corresponding melody notes
        try {
            if (scale.equals("Major Pentatonic")) {
                melody = majorPentatoic.clone();
            } else if (scale.equals("Minor Pentatonic")) {
                melody = minorPentatoic.clone();
            } else if (scale.equals("Major")) {
                melody = major.clone();
            } else if (scale.equals("Melodic Minor")) {
                melody = melodicMinor.clone();
            } else if (scale.equals("Harmonic Minor")) {
                melody = harmonicMinor.clone();
            } else if (scale.equals("Chromatic")) {
                melody = chromatic.clone();
            } else {
                melody = chromatic.clone();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Randomize melody notes
        for (int i = 0; i < mLength; i++){
            if (i==0 ){
                randMelody[i] = 60;
                answer = getNoteName(randMelody[i]) + " ";
            } else {
                randMelody[i] = melody[generator.nextInt(melody.length)];
                answer = answer + (getNoteName(randMelody[i])) + " ";
            }
        }

        melody = randMelody.clone();

        for(int i = 0; i < melody.length; i++)
        {
            int channel = 0, pitch = melody[i], velocity = 100;
            NoteOn on = new NoteOn(480 * i, channel, pitch, velocity);
            NoteOff off = new NoteOff(i * 480 + 120, channel, pitch, 0);

            noteTrack.insertEvent(on);
            noteTrack.insertEvent(off);

            // There is also a utility function for notes that you should use
            // instead of the above.
            noteTrack.insertNote(channel, pitch, velocity, i * 480, 120);
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
        playMidiFile();

	}

    public void replayMelody(View view) {
            playMidiFile();
    }

    // need to set answer to textView
    public void showAnswer(View view){

        //show answer
        try{
            answerText.setText(answer);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    private void playMidiFile(){
        //Play midi file
        try {
            String filename = getFilesDir() + "/" + file;
            File midifile = new File(filename);
            FileInputStream inputStream = new FileInputStream(midifile);
            fileDescriptor = inputStream.getFD();
            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fileDescriptor);
            inputStream.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNoteName(int note){
        String noteName;

        switch(note){
            case(60):
                noteName = "C";
                break;
            case(61):
                noteName = "C#";
                break;
            case(62):
                noteName = "D";
                break;
            case(63):
                noteName = "Eb";
                break;
            case(64):
                noteName = "E";
                break;
            case(65):
                noteName = "F";
                break;
            case(66):
                noteName = "F#";
                break;
            case(67):
                noteName = "G";
                break;
            case(68):
                noteName = "G#";
                break;
            case(69):
                noteName = "A";
                break;
            case(70):
                noteName = "Bb";
                break;
            case(71):
                noteName = "B";
                break;
            case(72):
                noteName = "C";
                break;
            default:
                noteName = "C";
        }

        return noteName;
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
            try {
                if(sequencer != null) {
                    sequencer.stop();
                } else if(mediaPlayer != null){
                    mediaPlayer.stop();
                }
            } catch(Exception e){
                e.printStackTrace();
            }
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
