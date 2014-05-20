/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery
 */
package com.example.a4complete;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends Activity {
    private Model model;
    private MainView mainView;
    private TitleView titleView;
    private boolean running = true;    
    private Timer repainTimer = new Timer();
    private Timer titleTimer = new Timer();
    private int height;
    private int width;
    
    private Button pause;
    private Button restart;
    
    public static Point displaySize;
    DisplayMetrics metrics = new DisplayMetrics();
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setTitle("CS349 A4 Demo");

        // save display size
        Display display = getWindowManager().getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);

        // initialize model
        model = new Model();
        
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;
        System.out.println("height is "+height);

        // set view
        setContentView(R.layout.main);
        
        
    }

    
    
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // create the views buttons and add them to the main activity
        titleView = new TitleView(this.getApplicationContext(), model);
        ViewGroup v1 = (ViewGroup) findViewById(R.id.main_1);
        v1.addView(titleView);

        mainView = new MainView(this.getApplicationContext(), model, titleView, height, imageToBitmap());
        ViewGroup v2 = (ViewGroup) findViewById(R.id.main_2);
        v2.addView(mainView);
        
        initTimer();
        // notify all views
        model.initObservers();
        
	    
        
        pause = (Button)findViewById(R.id.buttonPause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            	if (running)  pause.setText("PLAY");
            	else pause.setText("PAUSE");
            	running = !running;
             }
        });
        
        restart = (Button)findViewById(R.id.buttonRestart);
        restart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopTimer();
                initTimer();
                running = true;
                mainView.resetFruits();
                titleView.resetTitle();
            }
        });
    }

    public void backToStart(View view) {
    	finish();
    }
    
    public void initTimer() {
    	//start timer
	    repainTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			 public void run() {
				MainActivity.this.runOnUiThread(new Runnable() {
				    public void run() {
				        if (running) {
				        	model.setChange();
					    	model.notifyObservers();
						}				 
				    }
				 });
			}
		}, 0, (long) (1000.0/Model.FPS));
	    
	    
	    titleTimer.scheduleAtFixedRate(new TimerTask() {
	    	
			  @Override
			  public void run() {
					MainActivity.this.runOnUiThread(new Runnable() {
					    public void run() {
					        if (running) {
					        	titleView.calcTime();
							}				 
					    }
					 });
				}
		}, 0, 1000);
        
    }
    
    public void stopTimer() {
		titleTimer.cancel();
		titleTimer.purge();
		repainTimer.cancel();
		repainTimer.purge();
		repainTimer = new Timer();
	    titleTimer = new Timer();

    }
    
    public Bitmap[] imageToBitmap() {
    	Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.apple);
    	bm1 = Bitmap.createScaledBitmap(bm1, 110, 110, true);
    	Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.orange);
    	bm2 = Bitmap.createScaledBitmap(bm2, 110, 110, true);
    	Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.watermelon);
    	bm3 = Bitmap.createScaledBitmap(bm3, 50, 50, true);
    	return new Bitmap[] {bm1, bm2, bm3};
     }
    
    
}
