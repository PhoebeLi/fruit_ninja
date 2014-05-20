package com.example.a4complete;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

public class StartView extends View implements Observer{
	private final StartModel startModel;
	Button startButton;
	Button exitButton;
	 public StartView(Context context, StartModel m) {
	        super(context);
	        startModel = m;
	        startModel.addObserver(this);
	      //capture buttons
	        //startButton = (Button) findViewById(R.id.buttonStart);
	        //exitButton = (Button) findViewById(R.id.buttonExit);
	        
	        // add background picture
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		//startButton.draw(canvas);
		//exitButton.draw(canvas);
	}

}
