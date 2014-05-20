package com.example.a4complete;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StartActivity extends Activity {
	private int requestCode = 12;
	public static Point displaySize;
	
	private StartModel startModel;
	private StartView startView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setTitle("CS349 A4 Start page");

		// set user interface layout
		setContentView(R.layout.activity_start);
		
		// save display size
        Display display = getWindowManager().getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);
		
        //init model
        startModel = new StartModel();
        
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.main_3, new PlaceholderFragment()).commit();
		}
	}

	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
	
		startView = new StartView(this.getApplicationContext(), startModel);
        ViewGroup v3 = (ViewGroup) findViewById(R.id.main_3);
        v3.addView(startView);
        
        startModel.initObservers();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_start,
					container, false);
			return rootView;
		}
	}
	
	public void startGame(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		//startActivity(intent);
		startActivityForResult(intent, requestCode);
	}
	
	public void exitGame(View view) {
        finish(); // finish this activity
	}

}
