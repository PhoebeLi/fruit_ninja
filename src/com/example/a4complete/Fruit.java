/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery
 */
package com.example.a4complete;
import java.util.ArrayList;

import android.graphics.*;

/**
 * Class that represents a Fruit. Can be split into two separate fruits.
 */
public class Fruit {
    private Path path = new Path();
    private Bitmap fruit;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Matrix transform = new Matrix();
    public final float initDelta = -20;
    private float deltaDistance = initDelta;
    private boolean isPart = false;
    private int timer = 0;
    private PointF start = new PointF(), end = new PointF();
    private ArrayList<Fruit> parts = new ArrayList<Fruit>();

    
    /**
     * A fruit is represented as Path, typically populated 
     * by a series of points 
     */
    Fruit(float[] points) {
        init();
        this.path.reset();
        this.path.moveTo(points[0], points[1]);
        for (int i = 2; i < points.length; i += 2) {
            this.path.lineTo(points[i], points[i + 1]);
        }
        this.path.moveTo(points[0], points[1]);
        
    }

    Fruit(Region region) {
        init();
        this.path = region.getBoundaryPath();
    }

    Fruit(Path path) {
        init();
        this.path = path;
    }
    
    public Fruit(Bitmap bm) {
    	init();
		this.fruit = bm;
	}

    private void init() {
    	this.resetInitTimer();
        this.paint.setColor(Color.GREEN);
        this.paint.setStrokeWidth(5);
    }

    /**
     * The color used to paint the interior of the Fruit.
     */
    public int getFillColor() { return paint.getColor(); }
    public void setFillColor(int color) { paint.setColor(color); }

    /**
     * The width of the outline stroke used when painting.
     */
    public double getOutlineWidth() { return paint.getStrokeWidth(); }
    public void setOutlineWidth(float newWidth) { paint.setStrokeWidth(newWidth); }

    /**
     * Concatenates transforms to the Fruit's affine transform
     */
    public void rotate(float theta) { transform.postRotate(theta); }
    public void scale(float x, float y) { transform.postScale(x, y); }
    public void translate(float tx, float ty) { transform.postTranslate(tx, ty); }

    /**
     * Returns the Fruit's affine transform that is used when painting
     */
    public Matrix getTransform() { return transform; }

        
    /**
     * The path used to describe the fruit shape.
     */
    public Path getTransformedPath() {
        Path originalPath = new Path(path);
        Path transformedPath = new Path();
        originalPath.transform(transform, transformedPath);
        return transformedPath;
    }
    
    public Bitmap getTransformedBitmap(Matrix transform) {
    	//Log.d("ADebugTag", "Value: " + fruit.copy(Bitmap.Config.ARGB_8888, false));
    	Bitmap transformedFruit = fruit.copy(Bitmap.Config.ARGB_8888, false); // config?
    	Bitmap transformedBitmap = Bitmap.createBitmap(transformedFruit, 0, 0, fruit.getWidth(), fruit.getHeight(), transform, false);
    	return transformedBitmap;
    }

    /**
     * Paints the Fruit to the screen using its current affine
     * transform and paint settings (fill, outline)
     */
    public void draw(Canvas canvas) {
        // TODO BEGIN CS349
        // tell the shape to draw itself using the matrix and paint parameters
    	canvas.drawPath(getTransformedPath(), paint);
    	
    	canvas.drawLine(start.x, start.y, end.x, end.y, paint);
        // TODO END CS349
    }

    /**
     * Tests whether the line represented by the two points intersects
     * this Fruit.
     */
    public boolean intersects(PointF p1, PointF p2) {
        // TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in 
        // rotate path and create region for comparison
    	float x = 0, y = 0;
    	float p1x = p1.x, p2x = p2.x, p1y = p1.y, p2y = p2.y;
    	double theta = Math.toDegrees(Math.atan2(p1y - p2y,p1x - p2x)); // calculate degree of two points
    	if (theta > 90) { // (Math.PI)/2) = 90 degrees
			theta = (theta - 180);
		} else if (theta < -90) {
			theta = (180 + theta);
		}
    	if (p1x > p2x) {
			x = p2x;
			y = p2y;
		} else {
			x = p1x;
			y = p1y;
		}
    	RectF pathRect = new RectF(), lineRect = new RectF();
    	
    	Fruit curFruit = new Fruit(getTransformedPath());
    	curFruit.translate(-x, -y);
    	curFruit.rotate((float)-theta);
    	curFruit = new Fruit(curFruit.getTransformedPath());
        curFruit.path.computeBounds(pathRect, true);
    	
    	
        Path line = new Path();
    	line.reset();
    	line.moveTo(p1.x, p1.y);
    	line.lineTo(p2.x, p2.y);
    	line.moveTo(p2.x, p2.y);
    	Fruit lineFruit = new Fruit(line);
    	lineFruit.translate(-x, -y);
    	lineFruit.rotate((float)-theta);
    	lineFruit = new Fruit(lineFruit.getTransformedPath());
      	lineFruit.path.computeBounds(lineRect, true);
    	
        // TODO END CS349
    	return pathRect.intersect(lineRect);
    	//return false;
    }

    /**
     * Returns whether the given point is within the Fruit's shape.
     */
    public boolean contains(PointF p1) {
        Region region = new Region();
        boolean valid = region.setPath(getTransformedPath(), new Region());
        return valid && region.contains((int) p1.x, (int) p1.y);
    }

    /**
     * This method assumes that the line represented by the two points
     * intersects the fruit. If not, unpredictable results will occur.
     * Returns two new Fruits, split by the line represented by the
     * two points given.
     */
	public Fruit[] split(PointF p1, PointF p2) {
    	// TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in
        // rotate region
        // define region masks and use to split region into top and bottom
        // TODO END CS349
    	float x = 0, y = 0;
    	float p1x = p1.x, p2x = p2.x, p1y = p1.y, p2y = p2.y;
    	double theta = Math.toDegrees(Math.atan2(p1y - p2y,p1x - p2x)); // calculate degree of two points
    	if (theta > 90) { // (Math.PI)/2) = 90 degrees
			theta = (theta - 180);
		} else if (theta < -90) {
			theta = (180 + theta);
		}
    	if (p1x > p2x) {
			x = p2x;
			y = p2y;
		} else {
			x = p1x;
			y = p1y;
		}
    	
    	Fruit fruitCopy = new Fruit(this.getTransformedPath()); 
    	fruitCopy.translate(-x, -y);
    	fruitCopy.rotate((float)-theta);
    	Path temp = fruitCopy.getTransformedPath();
    	RectF bounds = new RectF();
    	temp.computeBounds(bounds, true); // get bounds of translated fruit
    	
    	//separate into top and bottom region
    	Region topRegion = new Region((int)bounds.left, (int)bounds.top, (int)bounds.right, 0);
    	Region bottomRegion = new Region((int)bounds.left, 0, (int)bounds.right, (int)bounds.bottom); 
    	
    	//set this to be top path and bottom path
    	topRegion.setPath(temp, topRegion);
    	bottomRegion.setPath(temp, bottomRegion);
    	
    	//simulate the explosion effect
        if (topRegion != null && bottomRegion != null) {
        	Fruit a = new Fruit(topRegion);
        	Fruit b = new Fruit(bottomRegion);
        	a.rotate((float)theta);
        	if (theta < 0) {
				a.translate(x-10, y-10);
			} else {
				a.translate(x+10, y-10);
			}
        	
        	a = new Fruit(a.getTransformedPath());
        	b.rotate((float)theta);
        	if (theta < 0) {
        		b.translate(x+10, y+10);
			} else {
				b.translate(x-10, y+10);
			}
        	
        	b = new Fruit(b.getTransformedPath());
        	float offset = this.getDeltaDistance(); // Used to offset new fruits
        	a.setDeltaDistance(offset);
        	b.setDeltaDistance(offset);
        	return new Fruit[] { a,b };
        }
        return new Fruit[0];
    }

    
    
    
    public float getDeltaDistance() {
    	return deltaDistance;
    }
    
    public void setDeltaDistance(float delta) {
    	this.deltaDistance = delta;
    }
    
    public void getPoints(PointF start, PointF end) {
    	this.start = start;
    	this.end = end;
    }
    
    public void resetInitTimer() {
    	this.timer = (int) (Math.random() * 200 + 30);
    }
    
    public void resetTimer() {
    	this.timer = (int) (Math.random() * 200 + 100);
    }
    
    public void decrementTimer() {
    	if (timer == 0) return;
    	this.timer--;
    }
    
    public int getTimer() {
    	return timer;
    }
    
    public boolean timerGoesOff() {
    	return timer == 0;
    }
    
    public void setIsPart(boolean part) {
    	this.isPart = part;
    }
    
    public boolean getIsPart() {
    	return isPart;
    }
    
    public void addPart(Fruit p) {
    	p.isPart = true;
    	this.parts.add(p);
    }
    
    public boolean hasParts() {
    	return this.parts.size() != 0;
    }
    
    public void clearParts() {
    	this.parts.clear();
    }
    
    public ArrayList<Fruit> getParts() {
    	return this.parts;
    }
}
