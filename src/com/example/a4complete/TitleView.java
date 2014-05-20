/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4complete;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.TextView;

import com.example.a4complete.R;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

/*
 * View to display the Title, and Score
 * Score currently just increments every time we get an update
 * from the model (i.e. a new fruit is added).
 */
public class TitleView extends TextView implements Observer {
    private Timer timer = new Timer();
    private boolean running = true;
    private int curMin = 0;
    private int curSec = 0;
    private String min = "";
    private String sec = "";
    private int curScore = 0;
    private boolean gameOver = false;

    // Constructor requires model reference
    public TitleView(Context context, Model model) {
        super(context);

        // set width, height of this view
        this.setHeight(235);
        this.setWidth(MainActivity.displaySize.x);

        // register with model so that we get updates
        model.addObserver(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO BEGIN CS349
        // add high score, anything else you want to display in the title
        // TODO END CS349
        setBackgroundColor(Color.BLUE);
        setTextSize(28);
        if (this.gameOver) {
			setText("GAME OVER!!!");
		} else {
			setText("score:"+curScore+"          "+"time "+min + " : " + sec);
		}
        
        //setText(getResources().getString(R.string.app_title) + " " + count);
    }

    
   
    // Update from model
    // ONLY useful for testing that the view notifications work
    @Override
    public void update(Observable observable, Object data) {
        // TODO BEGIN CS349
        // do something more meaningful here
        // TODO END CS349
        invalidate();
    }
    
    public void calcTime() {
  	    if (curSec >= 60) {
			curSec = 0;
			curMin += 1;
		}
  	    String min = null, sec = null;
	  	if (curMin < 10) {
	  		min = "0"+Integer.toString(curMin);
	  	} else {
	  		min = Integer.toString(curMin);
	  	}
	  	if (curSec < 10) {
	  		sec = "0"+Integer.toString(curSec);
	  	}else {
	  		sec = Integer.toString(curSec);
	  	}
	  	this.min = min;
	  	this.sec = sec;
	  	curSec += 1;
    }
    
    public void resetTitle() {
    	curMin = 0;
    	curSec = 0;
    	curScore = 0;
    	gameOver = false;
    }
    
    public void gamOverTitle() {
    	this.gameOver = true;
    }
    
    public void getScore(int score) {
    	curScore += score;
    }
}
