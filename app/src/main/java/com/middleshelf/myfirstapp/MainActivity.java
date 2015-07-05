package com.middleshelf.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.middleshelf.myfirstapp.MESSAGE";
    public final static String SCALE = "com.middleshelf.myfirstapp.SCALE";
    private SeekBar tempoControl = null;
    private TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();

        tempoControl = (SeekBar) findViewById(R.id.tempoBar);
        textView = (TextView) findViewById(R.id.textView);

        // Initialize the textview with '0'.
        textView.setText("Covered: " + tempoControl.getProgress() + "/" + tempoControl.getMax());
        tempoControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;


            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Covered: " + progress + "/" + seekBar.getMax());
                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // A private method to help us initialize our variables.
    private void initializeVariables() {
        tempoControl = (SeekBar) findViewById(R.id.tempoBar);
        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    }

    /** Called when the user clicks the Send button */
    public void startMajorPentatonicTest(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        Button buttonClicked = (Button) findViewById(R.id.mpbutton);
        String scale = buttonClicked.getText().toString();
        intent.putExtra(SCALE, scale);
        startActivity(intent);
    }
    /** Called when the user clicks the Send button */
    public void startMinorPentatonicTest(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        Button buttonClicked = (Button) findViewById(R.id.mipbutton);
        String scale = buttonClicked.getText().toString();
        intent.putExtra(SCALE, scale);
        startActivity(intent);
    }
    /** Called when the user clicks the Send button */
    public void startMajorTest(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        Button buttonClicked = (Button) findViewById(R.id.majorbutton);
        String scale = buttonClicked.getText().toString();
        intent.putExtra(SCALE, scale);
        startActivity(intent);
    }
    /** Called when the user clicks the Send button */
    public void startMelodicMinorTest(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        Button buttonClicked = (Button) findViewById(R.id.mmbutton);
        String scale = buttonClicked.getText().toString();
        intent.putExtra(SCALE, scale);
        startActivity(intent);
    }
    /** Called when the user clicks the Send button */
    public void startHarmonicMinorTest(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        Button buttonClicked = (Button) findViewById(R.id.hmbutton);
        String scale = buttonClicked.getText().toString();
        intent.putExtra(SCALE, scale);
        startActivity(intent);
    }
    /** Called when the user clicks the Send button */
    public void startChromaticTest(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        Button buttonClicked = (Button) findViewById(R.id.chromaticbutton);
        String scale = buttonClicked.getText().toString();
        intent.putExtra(SCALE, scale);
        startActivity(intent);
    }
}
