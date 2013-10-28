package com.example.workoutappdemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ThirdActivity extends Activity implements SensorEventListener {
	//super.foo();

	private SensorManager sensorMan;
	private Sensor accelerometer;

	private float[] mGravity;
	private float mAccel;
	private float mAccelCurrent;
	private float mAccelLast;
	
	private Button back3Button_;
	private Button seturl3Button_;
	private EditText edit3TextURL_;
	private EditText edit3TextPort_;
	TextView statusText_;
	private static String tag = "Concurrency_Exercise";
	Helper helper_ = new Helper();
	
	TextView repText_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		repText_ = (TextView) findViewById(R.id.repinfo);
		back3Button_ = (Button) findViewById(R.id.buttonBacktoMain);
		seturl3Button_ = (Button) findViewById(R.id.button_SetURL);
		edit3TextURL_ =  (EditText) findViewById(R.id.editTextURL);
		edit3TextPort_ =  (EditText) findViewById(R.id.editTextPort);
		statusText_ = (TextView) findViewById(R.id.textViewStatus);
		
		sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
		accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;
		
		BacktoMainButton();
	//	setUrlButton();
		doConcurrencyWithAsyncTaskSetURL();
	}

	 @Override
	 public void onResume() {
	     super.onResume();
	     sensorMan.registerListener(this, accelerometer,
	         SensorManager.SENSOR_DELAY_UI);
	 }

	 @Override
	 protected void onPause() {
	     super.onPause();
	     sensorMan.unregisterListener(this);
	 }

	     public void onSensorChanged(SensorEvent event) {
	     if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
	         mGravity = event.values.clone();
	         // Shake detection
	         float x = mGravity[0];
	         float y = mGravity[1];
	         float z = mGravity[2];
	         mAccelLast = mAccelCurrent;
	         mAccelCurrent = FloatMath.sqrt(x*x + y*y + z*z);
	         float delta = mAccelCurrent - mAccelLast;
	         mAccel = mAccel * 0.9f + delta;
	             // Make this higher or lower according to how much
	             // motion you want to detect
	         if(mAccel > 3){ 
	        	 //finish();
	        	 //Random ran = new Random();
	        	 int ranNum = 5 + (int)(Math.random() * ((20 - 3) + 1));
	        	 repText_.setText("Do " + ranNum + " reps!!!" );
	        	 //Toast.makeText(ThirdActivity.this, ranNum, Toast.LENGTH_LONG).show();
	        	 
	        	//Toast.makeText(ThirdActivity.this, "10", Toast.LENGTH_LONG).show();
	         }
	     }

	 }

	 public void onAccuracyChanged(Sensor sensor, int accuracy) {
	     // required method
	 }
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.third, menu);
		return true;
	}

	private void BacktoMainButton() {
		back3Button_.setOnClickListener(new OnClickListener() {

		@Override
			public void onClick(View v) {
				// Cancel your NetworkTask here!
				
				finish();
			
			}
		});
	}
	
	////////////////////////////////////////////////////////////
	public void doConcurrencyWithAsyncTaskSetURL() {
	////////////////////////////////////////////////////////////

		class NetworkTask extends AsyncTask<String, Void, String> {
			// <your member variables>
			Helper helper_;
			NetworkTask(Helper h) {
				helper_ = h;
			}
			// pass a "URL and Port number" to the web server
			protected String doInBackground(String... urlportnum) {
				helper_.doPotentiallyLongRunningBackgroundOperation(urlportnum[0]);
				try {
					//***************************************************
			/*						
					HttpClient client = new DefaultHttpClient();  
										HttpPost request = new HttpPost("http://10.0.2.2:8080/data");
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("UrlNum", "test1")); //urlportnum[0]
					nameValuePairs.add(new BasicNameValuePair("WorkoutPlanNum", "test2"));
					
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
	                //return " ";  */
					HttpClient client = new DefaultHttpClient();  
					HttpPost request = new HttpPost("http://10.0.2.2:8080/data");

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("WorkoutPlanNum", "0"));
					//nameValuePairs.add(new BasicNameValuePair("WorkoutType1", "1"));
					//nameValuePairs.add(new BasicNameValuePair("WorkoutType2", "1"));
					//nameValuePairs.add(new BasicNameValuePair("WorkoutType3", "1")); 
					//nameValuePairs.add(new BasicNameValuePair("WorkoutType4", "1"));
					//nameValuePairs.add(new BasicNameValuePair("WorkoutType5", "1"));
					nameValuePairs.add(new BasicNameValuePair("UrlNum", "test1")); //urlportnum[0]
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
	            } catch (IOException e) {
	                return "Unable to retrieve web page. URL may be invalid.";
	            }  
			}
			
			protected void onPostExecute(String htmlResult) {
				helper_.updateUserInterfaceWithResultFromNetwork();
				
				statusText_.setText("URL/Port is set." );
				
			}
		}

		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get the location from the UI
				CharSequence urlText_ = edit3TextURL_.getText();
				CharSequence portText_ = edit3TextPort_.getText();
				String urlPort_ = urlText_.toString() + ":" + portText_.toString();
				//String radioButtonSelected = "";
				Log.d(tag, "URL/Port = " + urlPort_); 
								
				// Define your NetworkTask here!
				final NetworkTask myNetTask = new NetworkTask(helper_);

				
				// Update the UI with the response
				statusText_.setText("URL/Port is being set." );

				// Execute your NetworkTask here
				myNetTask.execute(urlPort_);
			}
		}; // end OnClickListener()

		seturl3Button_.setOnClickListener(listener);
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
	