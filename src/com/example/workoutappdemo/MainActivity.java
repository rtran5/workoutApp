package com.example.workoutappdemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button goButton_;
	private RadioGroup input_;
	TextView resultText_;
	//TextView myWorkoutList_Label;
	TextView myWorkoutList_;
	CheckBox resultChkBox1_, resultChkBox2_, resultChkBox3_;
	CheckBox resultChkBox4_, resultChkBox5_, resultChkBox6_;
	private Button stopButton_;
	private Button addButton_;
	private Button clearButton_;
	private static String tag = "Concurrency_Exercise";

	Helper helper_ = new Helper();
	protected Runnable myUiUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		input_ = (RadioGroup) findViewById(R.id.radioGroup1);
		goButton_ = (Button) findViewById(R.id.btn_find);
		resultText_ = (TextView) findViewById(R.id.workout_info);
		//myWorkoutList_Label = (TextView) findViewById(R.id.textView2);
		myWorkoutList_ = (TextView) findViewById(R.id.textView1);
		//findViewById(R.id.ListView);resultText_ = (TextView) findViewById(R.id.workout_info);
		stopButton_ = (Button) findViewById(R.id.btn_cancel);
		addButton_ = (Button) findViewById(R.id.btn_Show);
		clearButton_ = (Button) findViewById(R.id.btn_clear);
		resultChkBox1_ = (CheckBox) findViewById(R.id.checkBox1);
		resultChkBox2_ = (CheckBox) findViewById(R.id.checkBox2);
		resultChkBox3_ = (CheckBox) findViewById(R.id.checkBox3);
		resultChkBox4_ = (CheckBox) findViewById(R.id.checkBox4);
		resultChkBox5_ = (CheckBox) findViewById(R.id.checkBox5);
		resultChkBox6_ = (CheckBox) findViewById(R.id.checkBox6);
		
		stopButton_.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(tag, "Stop button was clicked");
				resultText_.setText("Cancelled");
			}
		});
		// Call doConcurrencyWithAsyncTask
		doConcurrencyWithAsyncTask();
	}
	//***************************************************
	public void doConcurrencyWithAsyncTask() {

		class NetworkTask extends AsyncTask<String, Void, String> {
			// <your member variables>
			Helper helper_;
			NetworkTask(Helper h) {
				helper_ = h;
			}

			protected String doInBackground(String... urls) {
				helper_.doPotentiallyLongRunningBackgroundOperation(urls[0]);
				try {
					Log.d(tag, "before downloading .. " + urls[0] );
	                return downloadUrl(urls[0]);
	            } catch (IOException e) {
	                return "Unable to retrieve web page. URL may be invalid.";
	            }
			}
			
			protected void onPostExecute(String htmlResult) {
				helper_.updateUserInterfaceWithResultFromNetwork();
				
				//*****Parsing HTML data**********
				int startOfSpan = -1;
				int i = 1;
				String subHtml = "";
				String extractedData = "";
				while ( (startOfSpan = htmlResult.indexOf("<span class='summary' style='display:none;'>")) != -1) {
					subHtml = htmlResult.substring(startOfSpan + "<span class='summary' style='display:none;'>".length());
					extractedData = subHtml.substring(0, subHtml.indexOf("</span>"));
					//resultText_.setText(resultText_.getText() + "\n" + extractedData);
					
					switch (i) {
					  case 1 : resultChkBox1_.setText(extractedData);
					                   	        break;
					  case 2 : resultChkBox2_.setText(extractedData);
							                    break;
					  case 3 : resultChkBox3_.setText(extractedData);
							                    break;
					  case 4 : resultChkBox4_.setText(extractedData);
           	          							break;
					  case 5 : resultChkBox5_.setText(extractedData);
                      							break;
					  case 6 : resultChkBox6_.setText(extractedData);
                      							break;	                      
					}
					
					i++;
					
					htmlResult = subHtml;
				}
				//*****End Passing HTML data*******
			}
		}

		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get the time from the UI
				int checkedRadioButton = input_.getCheckedRadioButtonId();
				String radioButtonSelected = "";
				 
				switch (checkedRadioButton) {
				  case R.id.radio0 : radioButtonSelected = "abdominals";
				                   	          break;
				  case R.id.radio1 : radioButtonSelected = "biceps";
						                      break;
				  case R.id.radio2 : radioButtonSelected = "triceps";
						                      break;
				}
				
				//Clear the result checked box before finding workout
				resultChkBox1_.setChecked(false);
				resultChkBox1_.setText("");
				resultChkBox2_.setChecked(false);
				resultChkBox2_.setText("");
				resultChkBox3_.setChecked(false);
				resultChkBox3_.setText("");
				resultChkBox4_.setChecked(false);
				resultChkBox4_.setText("");
				resultChkBox5_.setChecked(false);
				resultChkBox5_.setText("");
				resultChkBox6_.setChecked(false);
				resultChkBox6_.setText("");
				
				// Define your NetworkTask here!
				final NetworkTask myNetTask = new NetworkTask(helper_);

				stopButton_.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// Cancel your NetworkTask here!
						myNetTask.cancel(true);
						resultText_.setText("Cancelled");
					}
				});
				
				addButton_.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(resultChkBox1_.isChecked() == true)
							myWorkoutList_.setText(myWorkoutList_.getText() + "\n" +resultChkBox1_.getText());
						if(resultChkBox2_.isChecked() == true)
							myWorkoutList_.setText(myWorkoutList_.getText() + "\n" +resultChkBox2_.getText());
						if(resultChkBox3_.isChecked() == true)
							myWorkoutList_.setText(myWorkoutList_.getText() + "\n" +resultChkBox3_.getText());
						if(resultChkBox4_.isChecked() == true)
							myWorkoutList_.setText(myWorkoutList_.getText() + "\n" +resultChkBox4_.getText());
						if(resultChkBox5_.isChecked() == true)
							myWorkoutList_.setText(myWorkoutList_.getText() + "\n" +resultChkBox5_.getText());
						if(resultChkBox6_.isChecked() == true)
							myWorkoutList_.setText(myWorkoutList_.getText() + "\n" +resultChkBox6_.getText());
					}
				});
				
				clearButton_.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							myWorkoutList_.setText("");
					}
				});
				// Update the UI with the response
				resultText_.setText("Below are muscle workout types for " + radioButtonSelected + ":" );

				// Execute your NetworkTask here
				myNetTask.execute(radioButtonSelected);
			}
		}; // end OnClickListener()

		goButton_.setOnClickListener(listener);
	}

	// Don't remove this, used by testing
	public void setHelper(Helper h) {
		helper_ = h;
	}
	
	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	private String downloadUrl(String myurl) throws IOException {
	    InputStream is = null;
	    // Only display the first 500 characters of the retrieved
	    // web page content.
	    int len = 500000;
	        
	    try {
	    	Log.d(tag, "start connecting the Url ... " );
	        //URL url = new URL("http", "www.bodybuilding.com", 80, "/exercises/list/muscle/selected/abdominals" ); 
	        URL url = new URL("http", "www.bodybuilding.com", 80, "/exercises/list/muscle/selected/"+myurl ); 
	    	
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        int response = conn.getResponseCode();
	        Log.d(tag, "The response is: " + response);
	        
	        is = conn.getInputStream();

	        // Convert the InputStream into a string
	        String contentAsString = readIt(is, len);
	        Log.d(tag, "download content: \n " + contentAsString );
	        
	        return contentAsString;
	        
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	}
	
	// Reads an InputStream and converts it to a String.
	public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");        
	    char[] buffer = new char[len];
	    reader.read(buffer);
	    return new String(buffer);
	}

}

//