package com.example.asynctaskdemo.test;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.stubbing.OngoingStubbing.*;

import java.lang.reflect.Method;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.asynctaskdemo.Helper;
import com.example.asynctaskdemo.MainActivity;
import com.example.asynctaskdemo.R;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity activity_;
	private Class<? extends MainActivity> mainClass_;
	private Button goButton_;
	private EditText timeout_;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
    
    System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
    
		activity_ = getActivity();
		mainClass_ = activity_.getClass();
		goButton_ = (Button) activity_
				.findViewById(com.example.asynctaskdemo.R.id.btn_execute);
		timeout_ = (EditText) activity_.findViewById(R.id.timeout);
		
		// Force parseable value
		try {
			Long.parseLong(timeout_.getText().toString());
		} catch (NumberFormatException nfe) {
			activity_.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					timeout_.setText("5");
				}
			});
			
			Thread.sleep(1000);
		}
	}
	
	
	public void onPreTest() {
		// For every test, make sure they have not left any doXXX methods in their main
		
		// Either our helper was called already or the goButton has a click listener
	}

	public void testPredefinedMethodsStillExist() {

		String error = "Did you delete methods from the exercise?! Expected method not found: ";
		try {
			@SuppressWarnings("unused")
			Method m = mainClass_.getMethod("doAllNetworkingOnMainThread",
					new Class[0]);
			m = mainClass_
					.getMethod("doBackgroundOperationUsingThreadAndRunnable",
							new Class[0]);
			m = mainClass_.getMethod("doConcurrencyUsingThreadAndRunOnMain",
					new Class[0]);
			m = mainClass_.getMethod("doConcurrencyUsingThreadAndHandler",
					new Class[0]);
			m = mainClass_.getMethod(
					"doAdvancedConcurrencyUsingThreadAndHandler", new Class[0]);
			m = mainClass_
					.getMethod("doConcurrencyWithAsyncTask", new Class[0]);
		} catch (NoSuchMethodException nse) {
			nse.printStackTrace();
			fail(error + " " + nse.getMessage());
		}
	}

	public void testNetworkingOnMainThread() throws Exception {
		Helper helper = spy(new Helper());
		activity_.setHelper(helper);
		
		activity_.doAllNetworkingOnMainThread();
		
		TouchUtils.clickView(this, goButton_);

		verify(helper).doPotentiallyLongRunningBackgroundOperation(anyLong());
		verify(helper).updateUserInterfaceWithResultFromNetwork();

		verify(helper).networkOnMain();
		verify(helper).uiOnMain();
	}
	
	public void testThreadAndRunnable() throws Exception {
		Helper helper = spy(new Helper());
		activity_.setHelper(helper);
		
		activity_.doBackgroundOperationUsingThreadAndRunnable();
		
		// We have to sleep because the thread is in the background
		Thread.sleep(500);

		verify(helper).doPotentiallyLongRunningBackgroundOperation();
		verify(helper).updateUserInterfaceWithResultFromNetwork();

		verify(helper).networkOnBack();
		verify(helper).uiOnBack();
	}
	
	public void testThreadAndRunOnUi() throws Exception {
		Helper helper = spy(new Helper());
		activity_.setHelper(helper);
		
		activity_.doConcurrencyUsingThreadAndRunOnMain();		
		
		// We have to sleep because the thread is in the background
		Thread.sleep(500);

		verify(helper).doPotentiallyLongRunningBackgroundOperation();
		verify(helper).updateUserInterfaceWithResultFromNetwork();

		verify(helper).networkOnBack();
		verify(helper).uiOnMain();
	}

	public void testThreadAndHandler() throws Exception {
		Helper helper = spy(new Helper());
		activity_.setHelper(helper);
		
		activity_.doConcurrencyUsingThreadAndHandler();		
		
		// We have to sleep because the thread is in the background
		Thread.sleep(500);

		verify(helper).doPotentiallyLongRunningBackgroundOperation();
		verify(helper).updateUserInterfaceWithResultFromNetwork();

		verify(helper).networkOnBack();
		verify(helper).uiOnMain();
	}
	
	
	public void testAdvancedHandler() throws Exception {
		Helper helper = spy(new Helper());
		activity_.setHelper(helper);
		
		activity_.doAdvancedConcurrencyUsingThreadAndHandler();		
		
		// We have to sleep because the thread is in the background
		Thread.sleep(500);

		verify(helper).doPotentiallyLongRunningBackgroundOperation();
		verify(helper).updateUserInterfaceWithResultFromNetwork();

		verify(helper).networkOnBack();
		verify(helper).uiOnMain();
	}	
	
	
	public void testAsyncTask() throws Exception {
		Helper helper = spy(new Helper());
		activity_.setHelper(helper);
		
		activity_.doConcurrencyWithAsyncTask();		
		
		TouchUtils.clickView(this, goButton_);

		verify(helper).doPotentiallyLongRunningBackgroundOperation(anyLong());
		verify(helper).updateUserInterfaceWithResultFromNetwork();

		verify(helper).networkOnBack();
		verify(helper).uiOnMain();
	}	
}
