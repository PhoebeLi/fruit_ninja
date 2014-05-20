/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4complete;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.*;



/*
 * View of the main game area.
 * Displays pieces of fruit, and allows players to slice them.
 */
@SuppressLint("DrawAllocation")
public class MainView extends View implements Observer {
    private final Model model;
    private TitleView titleview;
    private final MouseDrag drag = new MouseDrag();
    private Paint paint = new Paint();
    private int height = 0;
    private int slicedCount = 0;
    private int missingCount = 0;
    private Bitmap[] bmaps;


    // Constructor
    MainView(Context context, Model m, TitleView t, int h, Bitmap[] bms) {
        super(context);

        // register this view with the model
        model = m;
        titleview = t;
        model.addObserver(this);
        this.height = h;
        this.bmaps = bms;
        // TODO BEGIN CS349
        // test fruit, take this out before handing in!
        spawnFruits();
        // TODO END CS349
        

        // add controller
        // capture touch movement, and determine if we intersect a shape
        setOnTouchListener(new OnTouchListener() {
        	boolean result = true;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Log.d(getResources().getString(R.string.app_name), "Touch down");
                        drag.start(event.getX(), event.getY());
                        result = checkArea(drag.getStart());
                        break;

                    case MotionEvent.ACTION_UP: 	
                        // Log.d(getResources().getString(R.string.app_name), "Touch release");
                        drag.stop(event.getX(), event.getY());
                        result = checkArea(drag.getEnd());
                        // find intersected shapes
                        Iterator<Fruit> i = model.getShapes().iterator();
                        System.out.println("there are "+model.getShapes().size()+ " fruits");
                        while(i.hasNext()) {
                            Fruit s = i.next();
                            s.getPoints(drag.getStart(), drag.getEnd());
                            if (s.hasParts()) continue;
	                        if (s.intersects(drag.getStart(), drag.getEnd())) {
	                        	slicedCount ++;
	                            try {
	                                Fruit[] newFruits = s.split(drag.getStart(), drag.getEnd());
	                                // TODO BEGIN CS349
	                                //newFruits[0].setFillColor(Color.RED);
	                                //newFruits[1].setFillColor(Color.RED);
	                                //model.remove(s);
	                                //model.add(newFruits[0]);
	                                //model.add(newFruits[1]);
	                                //s.setDeltaDistance(s.initDelta);
	                                for (Fruit f : newFruits) {
	                                	f.setFillColor(Color.RED); // if sliced, set color to red
	        							s.addPart(f);
	        						}
	                                // TODO END CS349
	                            } catch (Exception ex) {
	                                Log.e("fruit_ninja", "Error: " + ex.getMessage());
	                            }
	                        } else {
	                            s.setFillColor(Color.GREEN);
	                        }
	                        
	                        invalidate();
	                    }
	                    int score = slicedCount*4;
	                    if (slicedCount >= 3) score += 10;
	                    titleview.getScore(score);
	                    slicedCount = 0;
	                    break;   
                }
                return result;
            }
        });
    }

   
    
    // inner class to track mouse drag
    // a better solution *might* be to dynamically track touch movement
    // in the controller above
    class MouseDrag {
        private float startx, starty;
        private float endx, endy;

        protected PointF getStart() { return new PointF(startx, starty); }
        protected PointF getEnd() { return new PointF(endx, endy); }

        protected void start(float x, float y) {
            this.startx = x;
            this.starty = y;
        }

        protected void stop(float x, float y) {
            this.endx = x;
            this.endy = y;
        }
    }
    

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw all pieces of fruit
        for (Fruit s : model.getShapes()) {
        	if (!s.timerGoesOff()) {
        		s.decrementTimer();
        	} else {
        		ArrayList<Fruit>drawList = new ArrayList<Fruit>();
        		if (s.hasParts()) {
        			drawList.addAll(s.getParts());
        		} 
        		drawList.add(s);
        		
           		for (Fruit fruit : drawList) {
        			float deltaX = (float) (250.0*Math.pow(1.0/Model.FPS, 2.0));
    	    		fruit.setDeltaDistance((fruit.getDeltaDistance() + deltaX));
    	        	if (fruit.getDeltaDistance() >= -fruit.initDelta+1) { // fallen off the screen	            		
    	        		if (!fruit.hasParts() && !fruit.getIsPart()) {
	            			//System.out.println("hasParts: " + fruit.hasParts() + "getIsPart: " + fruit.getIsPart());
	            			missingCount++;
	            			System.out.println("missed!" + missingCount);
	            		}
	            		if (missingCount >= 5) {
	            			this.gameOver();
	            			titleview.gamOverTitle();
	            			missingCount = 0;
	            		}
    	        		
    	        		fruit.setDeltaDistance(fruit.initDelta);
    	        		fruit.clearParts();
    	        		fruit.resetTimer();
    	    		} else {
    	    			fruit.translate(0,fruit.getDeltaDistance());
    	    		} // if
    	        	if (fruit != s || !fruit.hasParts()) {
    	        		fruit.draw(canvas);
    	        	}
    	        	
        		}
	    		
        	}
        }
    }

    @Override
    public void update(Observable observable, Object data) {
    	invalidate(); // redraw based on new data
    }
    
    
    private void spawnFruits() { // create 6 fruits
    	Fruit f1 = new Fruit(new float[] {0, 30, 30, 0, 60, 0, 90, 30, 90, 60, 60, 90, 30, 90, 0, 60});
        f1.translate(100, this.height-170); // put at the bottom of display window
        model.add(f1);

        Fruit f2 = new Fruit(new float[] {0, 30, 30, 0, 60, 0, 90, 30, 90, 60, 60, 90, 30, 90, 0, 60});
        f2.translate(200, this.height-170);
        model.add(f2);
        
        Fruit f3 = new Fruit(new float[] {0, 30, 30, 0, 60, 0, 90, 30, 90, 60, 60, 90, 30, 90, 0, 60});
        f3.translate(300, this.height-170); // put at the bottom of display window
        model.add(f3);

        Fruit f4 = new Fruit(new float[] {0, 30, 30, 0, 60, 0, 90, 30, 90, 60, 60, 90, 30, 90, 0, 60});
        f4.translate(400, this.height-170);
        model.add(f4);
        
        Fruit f5 = new Fruit(new float[] {0, 30, 30, 0, 60, 0, 90, 30, 90, 60, 60, 90, 30, 90, 0, 60});
        f5.translate(500, this.height-170); // put at the bottom of display window
        model.add(f5);

        Fruit f6 = new Fruit(new float[] {0, 30, 30, 0, 60, 0, 90, 30, 90, 60, 60, 90, 30, 90, 0, 60});
        f6.translate(600, this.height-170);
        model.add(f6);

    } 
    
    public void resetFruits() {
    	missingCount = 0;
    	slicedCount = 0;
    	model.removeAll();
    	spawnFruits();
    }
    
    private void gameOver() {
    	missingCount = 0;
    	slicedCount = 0;
    	model.removeAll();
    }
    private boolean checkArea(PointF p) {
    	if (p.y > 0.8*height) return false; // pass to children	
    	else return true; // handle by itself
    }
    
    public void getHeight(int h) {
    	this.height = h;
    }
}
