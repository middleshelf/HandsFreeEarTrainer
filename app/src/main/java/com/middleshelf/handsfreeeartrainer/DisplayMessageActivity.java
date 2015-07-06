package com.middleshelf.handsfreeeartrainer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.EditText;
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
    HandsFreeEarTrainerUtilities HFUtilities = new HandsFreeEarTrainerUtilities();
    private String file = "exampleout.mid";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String answer;
    private TextView answerText = null;
    private FileDescriptor fileDescriptor = new FileDescriptor();
    private int startingNote = 0;
    private int octaveRange = 1;

    // Middle C is integer 60
    private static int[] majorPentatoic = {60,62,64,67,69,72};
    private static int[] minorPentatoic = {60,63,65,67,70,72};
    private static int[] major = {60,62,64,65,67,69,71,72};
    private static int[] melodicMinor = {60,62,63,65,67,68,70,72};
    private static int[] harmonicMinor = {60,62,63,65,67,69,71,72};
    private static int[] chromatic = {60,61,62,63,64,65,66,67,68,69,70,71,72};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        //Get intents from user selections
	    Intent intent = getIntent();
        String scale = intent.getStringExtra(MainActivity.SCALE);
        String tempo = intent.getStringExtra(MainActivity.TEMPO);
        String melodyLength = intent.getStringExtra(MainActivity.MELODY_LENGTH);
        try{
            octaveRange = Integer.parseInt(intent.getStringExtra(MainActivity.OCTAVE_RANGE));
            startingNote = Integer.parseInt(intent.getStringExtra(MainActivity.STARTING_NOTE));
        } catch(Exception e){
            e.printStackTrace();
        }

	    // Create the text view
        answerText = (TextView) findViewById(R.id.answerText);
        answerText.setTextSize(30);
	    //textView.setText(message);

        //Play ogg files
        /*sequencer = new Sequencer(this, 4, 8);
        sequencer.setSample(0, R.raw.bass);
        //Putting Kick on the beat
        sequencer.enableCell(0, 0);
        sequencer.enableCell(0,2);
        sequencer.enableCell(0,4);
        sequencer.enableCell(0,6);
        sequencer.play();*/

        // Create blank MidiTracks
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();

        // Add events to the tracks
        // Track 0 is typically the tempo map
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        //Set Tempo
        Tempo t = new Tempo();
        t.setBpm(Integer.parseInt(tempo));
        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(t);

        //Create Melody
        int[] melody = {};

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
        int mLength = Integer.parseInt(melodyLength);
        int[] randMelody = new int[mLength];
        Random generator = new Random();

        for (int i = 0; i < mLength; i++){
            if (i==0 ){
                randMelody[i] = 60 + startingNote;
                answer = HFUtilities.getNoteName(randMelody[i]) + " ";
            } else {
                randMelody[i] = generator.nextInt(octaveRange)*12 + melody[generator.nextInt(melody.length)];
                answer = answer + (HFUtilities.getNoteName(randMelody[i])) + " ";
            }
        }

        melody = randMelody.clone();

        // Create Midi Track
        for(int i = 0; i < melody.length; i++)
        {
            int channel = 0, pitch = melody[i], velocity = 100;
            NoteOn on = new NoteOn(480 * i, channel, pitch, velocity);
            NoteOff off = new NoteOff(i * 480 + 240, channel, pitch, 0);

            noteTrack.insertEvent(on);
            noteTrack.insertEvent(off);

            // There is also a utility function for notes that you should use
            // instead of the above.
            noteTrack.insertNote(channel, pitch, velocity, i * 480, 120);
        }

        // It's best not to manually insert EndOfTrack events; MidiTrack will
        // call closeTrack() on itself before writing itself to a file

        // Create a MidiFile with the tracks we created
        ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);
        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

        // Write the MIDI data to a file
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



    public void submitAnswer(View view){
        EditText userAnswerText = (EditText) findViewById(R.id.submitAnswerText);
        String userAnswer = userAnswerText.getText().toString();
        userAnswer.replaceAll("\\s+","");

        //show correct
        //Removing all the whitespaces and comparing
            try{
                if (userAnswer.replaceAll("\\s+", "").equals(answer.replaceAll("\\s+",""))) {
                    answerText.setText("Correct!");
                }else {
                    answerText.setText("Try again");
                }
            } catch(Exception e){
                e.printStackTrace();
            }
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
