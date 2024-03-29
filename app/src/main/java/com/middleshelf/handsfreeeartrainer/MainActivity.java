package com.middleshelf.handsfreeeartrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.middleshelf.handsfreeeartrainer.HandsFreeEarTrainerUtilities;


public class MainActivity extends Activity {

    public final static String SCALE = "com.middleshelf.handsfreeeartrainer.SCALE";
    public final static String TEMPO = "com.middleshelf.handsfreeeartrainer.TEMPO";
    public final static String MELODY_LENGTH = "com.middleshelf.handsfreeeartrainer.MELODY_LENGTH";
    public final static String OCTAVE_RANGE = "com.middleshelf.handsfreeeartrainer.OCTAVE_RANGE";
    public final static String STARTING_NOTE = "com.middleshelf.handsfreeeartrainer.STARTING_NOTE";

    private HandsFreeEarTrainerUtilities HFUtilities = new HandsFreeEarTrainerUtilities();
    private SeekBar tempoControl = null;
    private TextView textView = null;
    private SeekBar melodyLengthSeekBar = null;
    private TextView melodyLengthTextView = null;
    private SeekBar octaveRangeSeekBar = null;
    private TextView octaveRangeTextView = null;
    private SeekBar startingNoteSeekBar = null;
    private TextView startingNoteTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();

        // Initialize the SeekBars with starting values
        textView.setText("Tempo: " + tempoControl.getProgress());
        melodyLengthTextView.setText("Melody Length: " + melodyLengthSeekBar.getProgress());
        octaveRangeTextView.setText("Octave Range: " + octaveRangeSeekBar.getProgress());
        startingNoteTextView.setText("Starting Note: " + HFUtilities.getNoteName(startingNoteSeekBar.getProgress()));

        tempoControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Tempo: " + progress);
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });

        melodyLengthSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                melodyLengthTextView.setText("Melody Length: " + progress);
            }
        });

        octaveRangeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue + 1;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                octaveRangeTextView.setText("Octave Range: " + progress);
            }
        });

        startingNoteSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String noteName = HFUtilities.getNoteName(progress);
                startingNoteTextView.setText("Starting Note: " + noteName);
            }
        });
    }

    // A private method to help us initialize our variables.
    private void initializeVariables() {
        Intent intent = getIntent();
        if(tempoControl == null) {
            tempoControl = (SeekBar) findViewById(R.id.tempoBar);
            //Attempt to load existing user values
            //TODO: Need to find a way to make this work with a global variable
            try {
                tempoControl.setProgress(Integer.parseInt(intent.getStringExtra(TEMPO)));
            }catch (Exception e){
                e.printStackTrace();
            }
        } if(textView == null) {
            textView = (TextView) findViewById(R.id.textView);
        } if(melodyLengthSeekBar == null) {
            melodyLengthSeekBar = (SeekBar) findViewById(R.id.melodyLengthSeekBar);
        } if(melodyLengthTextView == null) {
            melodyLengthTextView = (TextView) findViewById(R.id.melodyLengthTextView);
        } if(octaveRangeSeekBar == null) {
            octaveRangeSeekBar = (SeekBar) findViewById(R.id.octaveRangeSeekBar);
        } if(octaveRangeTextView == null) {
            octaveRangeTextView = (TextView) findViewById(R.id.octaveRangeTextView);
        } if(startingNoteSeekBar == null) {
            startingNoteSeekBar = (SeekBar) findViewById(R.id.startingNoteSeekBar);
        } if(startingNoteTextView == null) {
            startingNoteTextView = (TextView) findViewById(R.id.startingNoteTextView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*************************************************************
     * Button Selection
     ************************************************************/

    /** Called when the user clicks the Major Pentatonic button */
    public void startMajorPentatonicTest(View view) {
        Button buttonClicked = (Button) findViewById(R.id.mpbutton);
        getUserSelections(buttonClicked);
    }
    /** Called when the user clicks the Minor Pentatonic button */
    public void startMinorPentatonicTest(View view) {
        Button buttonClicked = (Button) findViewById(R.id.mipbutton);
        getUserSelections(buttonClicked);
    }
    /** Called when the user clicks the Major button */
    public void startMajorTest(View view) {
        Button buttonClicked = (Button) findViewById(R.id.majorbutton);
        getUserSelections(buttonClicked);
    }
    /** Called when the user clicks the Melodic Minor button */
    public void startMelodicMinorTest(View view) {
        Button buttonClicked = (Button) findViewById(R.id.mmbutton);
        getUserSelections(buttonClicked);
    }
    /** Called when the user clicks the Harmonic Minor button */
    public void startHarmonicMinorTest(View view) {
        Button buttonClicked = (Button) findViewById(R.id.hmbutton);
        getUserSelections(buttonClicked);
    }
    /** Called when the user clicks the Chromatic button */
    public void startChromaticTest(View view) {
        Button buttonClicked = (Button) findViewById(R.id.chromaticbutton);
        getUserSelections(buttonClicked);
    }

    private void getUserSelections(Button selectedButton){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String scale = selectedButton.getText().toString();
        intent.putExtra(SCALE, scale);
        String tempo = Integer.toString(tempoControl.getProgress());
        intent.putExtra(TEMPO, tempo);
        String melodyLength = Integer.toString(melodyLengthSeekBar.getProgress());
        intent.putExtra(MELODY_LENGTH, melodyLength);
        intent.putExtra(OCTAVE_RANGE, Integer.toString(octaveRangeSeekBar.getProgress()));
        intent.putExtra(STARTING_NOTE, Integer.toString(startingNoteSeekBar.getProgress()));
        startActivity(intent);
    }
}
