package com.example.workoutappdemo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WorkoutActivity extends Activity {
	
	private Button backButton_;
	MediaPlayer ourSong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_workout);
		//backButton_ = (Button) findViewById(R.id.buttonBacktoMain);
		backButton_ = (Button) findViewById(R.id.buttonBacktoMain);
		ourSong = MediaPlayer.create(WorkoutActivity.this, R.raw.song);
		//ourSong.start();
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = {0, 100, 10000, 100, 20000};
		v.vibrate(pattern,0);
		
		//String print = getIntent().getStringExtra("wolist");
		//Toast.makeText(WorkoutActivity.this, print , Toast.LENGTH_LONG).show();
		BacktoMainButton();
		
	}

	public void onToggleClicked(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	    	ourSong.start();
	    } else {
	    	ourSong.pause();
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.second, menu);
		getMenuInflater().inflate(R.menu.workout, menu);
		return true;
	}

	private void BacktoMainButton() {
		backButton_.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Cancel your NetworkTask here!
				ourSong.release();
				finish();
				
			}
		});
	}
	
}

