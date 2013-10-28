package com.example.workoutappdemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
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
	CheckBox resultChkBox4_, resultChkBox5_;
	private Button stopButton_;
	private Button addButton_;
	private Button clearButton_;
	private Button storeButton_;
	private Button getButton_;
	private Button startwoButton_; //start WorkOut Button
	private Button seturlButton_; //set URL Port Button
	private static String tag = "Concurrency_Exercise";
	private CharSequence data;
	Helper helper_ = new Helper();
	protected Runnable myUiUpdate;
	private EditText webLocation_;
	String wolist = "10";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		input_ = (RadioGroup) findViewById(R.id.radioGroup1);
		goButton_ = (Button) findViewById(R.id.btn_find);
		resultText_ = (TextView) findViewById(R.id.workout_info);
		webLocation_ =  (EditText) findViewById(R.id.webLocation);
		myWorkoutList_ = (TextView) findViewById(R.id.textView1);
		stopButton_ = (Button) findViewById(R.id.btn_cancel);
		addButton_ = (Button) findViewById(R.id.btn_Show);
		storeButton_ = (Button) findViewById(R.id.btn_store);
		getButton_ = (Button) findViewById(R.id.btn_get);
		startwoButton_ = (Button) findViewById(R.id.btn_startwo);
		seturlButton_ = (Button) findViewById(R.id.btn_maint);
		clearButton_ = (Button) findViewById(R.id.btn_clear);
		resultChkBox1_ = (CheckBox) findViewById(R.id.checkBox1);
		resultChkBox2_ = (CheckBox) findViewById(R.id.checkBox2);
		resultChkBox3_ = (CheckBox) findViewById(R.id.checkBox3);
		resultChkBox4_ = (CheckBox) findViewById(R.id.checkBox4);
		resultChkBox5_ = (CheckBox) findViewById(R.id.checkBox5);
			
		stopButton_.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(tag, "Stop button was clicked");
				resultText_.setText("Cancelled");
			}
		});
		// Call doConcurrencyWithAsyncTask
		doConcurrencyWithAsyncTaskStartWorkOut();  // start workout button with sensor
		doConcurrencyWithAsyncTaskStoretoWebserver();  // store button to Web Server
		doConcurrencyWithAsyncTaskGetfromWebserver();  // get button from Web Server
		doConcurrencyWithAsyncTasksetURLPort();  // set URL and Port for the Web Server
		doConcurrencyWithAsyncTaskWWW(); // find button with WWW
	}
	/////////////////////////////////////////////////////////////////
	public void doConcurrencyWithAsyncTaskStartWorkOut() {
	/////////////////////////////////////////////////////////////////	

		class NetworkTask extends AsyncTask<String, Void, String> {
			// <your member variables>
			Helper helper_;
			NetworkTask(Helper h) {
				helper_ = h;
			}
			// pass a "WorkOut list" to a new Start Workout screen playing music , ...
			protected String doInBackground(String... wolist) {
				helper_.doPotentiallyLongRunningBackgroundOperation(wolist[0]);
					//***************************************************
				data = myWorkoutList_.getText();
				String[] wodata_ = data.toString().split("\n");
				String[] temp = {" ", " ", " ", " ", " ", " "};
				for (int i =0; i < wodata_.length ; i++)
					{
					temp[i] = wodata_[i];
					Log.d(tag, "Workout List: " + temp[i]);
					}
				////////////////////////////////////////////
				// Step 2: to call playing music sensor here
				 Log.d(tag, "in startWO step 2 ...");
				 //add here
				
				
				
					
				return " ";
			}
			
			protected void onPostExecute(String htmlResult) {
				helper_.updateUserInterfaceWithResultFromNetwork();
				
				resultText_.setText("Playing music on next screen ..." );
			}
		}

		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				///////////////////////////////////////////////////
				// Step 1: to do Pop up Start Workout XML here
				 Log.d(tag, "in startWO step 1 ...");
				 
				
				
				
				startActivity(new Intent(MainActivity.this, WorkoutActivity.class).putExtra(wolist, myWorkoutList_.getText()));
				
				CharSequence locText_ = webLocation_.getText();
				Log.d(tag, "locText_ = " + locText_); 
								
				// Define your NetworkTask here!
				final NetworkTask myNetTask = new NetworkTask(helper_);

				// Execute your NetworkTask here
				myNetTask.execute(locText_.toString());
			}
		}; // end OnClickListener()

		startwoButton_.setOnClickListener(listener);
	}
	////////////////////////////////////////////////////////////
	public void doConcurrencyWithAsyncTaskStoretoWebserver() {
	////////////////////////////////////////////////////////////

		class NetworkTask extends AsyncTask<String, Void, String> {
			// <your member variables>
			Helper helper_;
			NetworkTask(Helper h) {
				helper_ = h;
			}
			// pass a "work out plan number" woplannum parameter to the web server
			protected String doInBackground(String... woplannum) {
				helper_.doPotentiallyLongRunningBackgroundOperation(woplannum[0]);
				try {
					//***************************************************
					data = myWorkoutList_.getText();
					String[] wodata_ = data.toString().split("\n");
					String[] temp = {" ", " ", " ", " ", " ", " "};
					for (int i =0; i < wodata_.length ; i++)
						{
						temp[i] = wodata_[i];
						Log.d(tag, " temp" + temp[i]);
						}
					//String urls_ = urls[0];
					
					HttpClient client = new DefaultHttpClient();  
										HttpPost request = new HttpPost("http://10.0.2.2:8080/data");
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("WorkoutPlanNum", woplannum[0]));
					nameValuePairs.add(new BasicNameValuePair("WorkoutType1", temp[1]));
					nameValuePairs.add(new BasicNameValuePair("WorkoutType2", temp[2]));
					nameValuePairs.add(new BasicNameValuePair("WorkoutType3", temp[3])); 
					nameValuePairs.add(new BasicNameValuePair("WorkoutType4", temp[4]));
					nameValuePairs.add(new BasicNameValuePair("WorkoutType5", temp[5]));
					UrlEncodedFormEntity entity;
					entity = new UrlEncodedFormEntity(nameValuePairs);
					request.setEntity(entity);
					HttpResponse response = client.execute(request);
				//do something with the response!
					HttpEntity ent=response.getEntity();
					InputStream postis=ent.getContent();
					String contentAsString = readIt(postis, 150); //len = 150
		
					Log.d(tag, "The response from Data Servlet : " + contentAsString);	
					return "";
	                //return downloadUrl(urls[0]);
	            } catch (IOException e) {
	                return "Unable to retrieve web page. URL may be invalid.";
	            }
			}
			
			protected void onPostExecute(String htmlResult) {
				helper_.updateUserInterfaceWithResultFromNetwork();
				
				resultText_.setText("Storing in Web Server is completed!" );
			}
		}

		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get the location from the UI
				CharSequence locText_ = webLocation_.getText();
				//String radioButtonSelected = "";
				Log.d(tag, "locText_ = " + locText_); 
								
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
				
				clearButton_.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							myWorkoutList_.setText("");
					}
				});
				// Update the UI with the response
				resultText_.setText("My workout plan " + locText_ + " is storing." );

				// Execute your NetworkTask here
				myNetTask.execute(locText_.toString());
			}
		}; // end OnClickListener()

		storeButton_.setOnClickListener(listener);
	}
	
	////////////////////////////////////////////////////////////
	public void doConcurrencyWithAsyncTaskGetfromWebserver() {
	////////////////////////////////////////////////////////////

		class NetworkTask extends AsyncTask<String, Void, String> {
			// <your member variables>
			Helper helper_;
			NetworkTask(Helper h) {
				helper_ = h;
			}
			// pass a "work out plan number" woplannum parameter to the web server
			protected String doInBackground(String... woplannum) {
				helper_.doPotentiallyLongRunningBackgroundOperation(woplannum[0]);
				Log.d(tag, " testing ");
				try {
					//***************************************************
				
					HttpClient client = new DefaultHttpClient();  
					HttpGet request = new HttpGet("http://10.0.2.2:8080/data?WorkoutPlanNum="+woplannum[0]);
					
				
					HttpResponse response = client.execute(request);
					
					//UrlEncodedFormEntity entity;
					//entity = new UrlEncodedFormEntity(nameValuePairs);
					//request.getEntity(entity);
					
				   //do something with the response!
					HttpEntity ent=response.getEntity();
					InputStream getis=ent.getContent();
					String contentAsString1 = readIt(getis, 500); //len 
		
					Log.d(tag, "Get from Data Servlet : " + contentAsString1);	
					return contentAsString1;
	                //return downloadUrl(urls[0]);
	            } catch (IOException e) {
	                return "Unable to retrieve web page. URL may be invalid.";
	            } 
			}
			
			protected void onPostExecute(String htmlResult) {
				helper_.updateUserInterfaceWithResultFromNetwork();
				myWorkoutList_.setText(" ");
				
				//*****Parsing XML data*********
				int startOfSpan = -1;
				int i = 1;
				String subHtml = "";
				String extractedData = ""; 
				//myWorkoutList_.setText(htmlResult);
				
				while ( (startOfSpan = htmlResult.indexOf("<WorkoutType>")) != -1) {
					subHtml = htmlResult.substring(startOfSpan + "<WorkoutType>".length());
					extractedData = subHtml.substring(0, subHtml.indexOf("</WorkoutType>"));
					
					myWorkoutList_.setText(myWorkoutList_.getText() + "\n" +extractedData);
					
					htmlResult = subHtml;
				} 	
				resultText_.setText("Getting from Web Server is completed!" );
				
			}
		}

		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get the location from the UI
				myWorkoutList_.setText(""); 
				CharSequence locText_ = webLocation_.getText();
				//String radioButtonSelected = "";
				Log.d(tag, "locText_ = " + locText_); 
								
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
				
				clearButton_.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							myWorkoutList_.setText("");
					}
				});
				// Update the UI with the response
				resultText_.setText("My workout plan " + locText_ + " is storing." );

				// Execute your NetworkTask here
				myNetTask.execute(locText_.toString());
			}
		}; // end OnClickListener()

		getButton_.setOnClickListener(listener);
	}
	/////////////////////////////////////////////////////////////////
	public void doConcurrencyWithAsyncTasksetURLPort() {
	/////////////////////////////////////////////////////////////////	

	/*	class NetworkTask extends AsyncTask<String, Void, String> {
			// <your member variables>
			Helper helper_;
			NetworkTask(Helper h) {
				helper_ = h;
			}
			// pass a "WorkOut list" to a new Start Workout screen playing music , ...
			protected String doInBackground(String... wolist) {
				helper_.doPotentiallyLongRunningBackgroundOperation(wolist[0]);
					//***************************************************
				data = myWorkoutList_.getText();
				String[] wodata_ = data.toString().split("\n");
				String[] temp = {" ", " ", " ", " ", " ", " "};
				for (int i =0; i < wodata_.length ; i++)
					{
					temp[i] = wodata_[i];
					Log.d(tag, "Workout List: " + temp[i]);
					}
				////////////////////////////////////////////
				// Step 2: to call playing music sensor here
				 Log.d(tag, "in startWO step 2 ...");
				 //add here
				
				
				
					
				return " ";
			}
			
			protected void onPostExecute(String htmlResult) {
				helper_.updateUserInterfaceWithResultFromNetwork();
				
				resultText_.setText("Setting URL and Port on next screen ..." );
			}
		}
*/
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				///////////////////////////////////////////////////
				// Step 1: to do Pop up Start Workout XML here
				 Log.d(tag, "in start seturl step 1 ...");
				 
					
				startActivity(new Intent(MainActivity.this, ThirdActivity.class));
				
				CharSequence locText_ = webLocation_.getText();
				Log.d(tag, "locText_ = " + locText_); 
								
				// Define your NetworkTask here!
				//final NetworkTask myNetTask = new NetworkTask(helper_);

				// Execute your NetworkTask here
				//myNetTask.execute(locText_.toString());
			}
		}; // end OnClickListener()

		seturlButton_.setOnClickListener(listener);
	}		
	////////////////////////////////////////////////////////////////////	
	public void doConcurrencyWithAsyncTaskWWW() {
	////////////////////////////////////////////////////////////////////

		class NetworkTask extends AsyncTask<String, Void, String> {
			// <your member variables>
			Helper helper_;
			NetworkTask(Helper h) {
				helper_ = h;
			}

			protected String doInBackground(String... urls) {
				helper_.doPotentiallyLongRunningBackgroundOperation(urls[0]);
				try {
					//***************************************************
				
	                return downloadUrl(urls[0]);
	            } catch (IOException e) {
	                return "Unable to retrieve web page. URL may be invalid.";
	            }
			}
			
			protected void onPostExecute(String htmlResult) {
				helper_.updateUserInterfaceWithResultFromNetwork();
				
				//*****Parsing XML data**********
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
	    	//myurl = "data";
	        URL url = new URL("http", "www.bodybuilding.com", 80, "/exercises/list/muscle/selected/"+myurl ); 
	        //URL url = new URL("http", "www.bodybuilding.com", 80, "/exercises" ); 
	        //URL url = new URL("http://10.0.2.2:8080/data" ); 
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        Log.d(tag, "before connecting ... " );
	        conn.connect();
	        Log.d(tag, "after connecting ... " );
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