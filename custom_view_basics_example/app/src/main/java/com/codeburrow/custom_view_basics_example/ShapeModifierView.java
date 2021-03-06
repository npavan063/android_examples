package com.codeburrow.custom_view_basics_example;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jul/29/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class ShapeModifierView extends View {

    private static final String LOG_TAG = ShapeModifierView.class.getSimpleName();

    private Context mContext;

    private int mShapeColor;
    private String mShapeName;
    private boolean mDisplayShapeName;

    private int mShapeWidth = 100;
    private int mShapeHeight = 100;
    private int mTextXOffset = 0;
    private int mTextYOffset = 30;
    // The Paint class holds the style and color information about how to draw geometries, text and bitmaps.
    private Paint mPaint;

    private String[] mShapeNameValues = {"square", "circle", "triangle"};
    // This is the view state for the ShapeModifierView.
    private int mShapeNameIndex = 0;

    /**
     * Constructor that is called when inflating a view from XML file,
     * supplying attributes that were specified in the XML file.
     *
     * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public ShapeModifierView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        retrieveCustomAttributes(attrs);
        setUpPaint();
    }

    /**
     * Retrieve the Custom Attributes from the XML file.
     *
     * @param attrs The desired attributes to be retrieved.
     */
    private void retrieveCustomAttributes(AttributeSet attrs) {
        /*
         * TypedArray: Container for an array of values that were retrieved with obtainStyledAttributes(...).
         * Be sure to call recycle() when done with them.
         *
         * obtainStyledAttributes: Returns a TypedArray holding an array of the attribute values.
         */
        // Obtain a TypedArray of the attribute values.
        TypedArray attrsTypedArray = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.ShapeModifierView, 0, 0);
        try {
            // Retrieve the color value for the attribute at index: ShapeModifierView_shapeColor.
            mShapeColor = attrsTypedArray.getColor(R.styleable.ShapeModifierView_shapeColor, Color.BLACK);
            // Retrieve the string value for the attribute at index: ShapeModifierView_shapeName.
            mShapeName = attrsTypedArray.getString(R.styleable.ShapeModifierView_shapeName);
            // Retrieve the boolean value for the attribute at index: ShapeModifierView_displayShapeName.
            mDisplayShapeName = attrsTypedArray.getBoolean(R.styleable.ShapeModifierView_displayShapeName, false);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            /*
             * Recycle the TypedArray, to be re-used by a later caller.
             * After calling this function you must not ever touch the typed array again.
             */
            attrsTypedArray.recycle();
        }
    }

    private void setUpPaint() {
        // Create a new paint with default settings.
        mPaint = new Paint();
        // Set the paint's style, used for controlling how primitives's geometries are interpreted.
        mPaint.setStyle(Paint.Style.FILL);
        // Set the paint's color.
        mPaint.setColor(mShapeColor);
        // Set the paint's text size. This value must be > 0.
        mPaint.setTextSize(30);
    }

    /**
     * Measure the view and its content to determine the measured width and the measured height.
     * This method is invoked by measure(int, int) and should be overridden
     * by subclasses to provide accurate and efficient measurement of their contents.
     * <p/>
     * CONTRACT:
     * When overriding this method, you must call setMeasuredDimension(int, int) to store the measured width and height of this view.
     * Failure to do so will trigger an IllegalStateException, thrown by measure(int, int).
     * Calling the superclass onMeasure(int, int) is a valid use.
     *
     * @param widthMeasureSpec  Horizontal space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     * @param heightMeasureSpec Vertical space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Define the extra padding for the shapeName text.
        int textPadding = 10;

        // Resolve the width, with constraints imposed by a MeasureSpec.
        int minWidth = mShapeWidth + getPaddingLeft() + getPaddingRight();
        int width = resolveSizeAndState(minWidth, widthMeasureSpec, 0);

        // Resolve the height.
        int minHeight = mShapeHeight + getPaddingTop() + getPaddingBottom();
        if (mDisplayShapeName) {
            minHeight += mTextYOffset + textPadding;
        }
        int height = resolveSizeAndState(minHeight, heightMeasureSpec, 0);

        // Store the measured width and height. Failing to do so will trigger an exception at measurement time.
        setMeasuredDimension(width, height);
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas The Canvas on which the background will be drawn.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the specified shape(Rect, Circle, Path) using the specified paint.
        if (mShapeName.equals("square")) {
            canvas.drawRect(0, 0, mShapeWidth, mShapeHeight, mPaint);
        } else if (mShapeName.equals("circle")) {
            canvas.drawCircle(mShapeWidth / 2, mShapeHeight / 2, mShapeWidth / 2, mPaint);
        } else if (mShapeName.equals("triangle")) {
            canvas.drawPath(getTrianglePath(), mPaint);
        }

        if (isDisplayShapeName()) {
            // Draw the specified text.
            canvas.drawText(mShapeName, mShapeWidth + mTextXOffset, mShapeHeight + mTextYOffset, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mShapeNameIndex = (mShapeNameIndex == mShapeNameValues.length - 1) ? 0 : mShapeNameIndex + 1;
            mShapeName = mShapeNameValues[mShapeNameIndex];
            // Invalidate the View from a non-UI thread.
            postInvalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Hook allowing a view to generate a representation of its internal state
     * that can later be used to create a new instance with that same state.
     * <p/>
     * This state should only contain information that is not persistent
     * or can not be reconstructed later.
     * For example, you will never store your current position on screen because
     * that will be computed again when a new instance of the view is placed in its view hierarchy.
     *
     * @return A Parcelable object containing the view's current dynamic state, or null if there is nothing interesting to save.
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        // Construct a new, empty Bundle.
        Bundle bundle = new Bundle();
        // Insert a Parcelable value into the mapping of this bundle.
        // Save the base view state.
        bundle.putParcelable("instance_view_state", super.onSaveInstanceState());
        // Insert an Int value into the bundle.
        bundle.putInt("shape_name_index", mShapeNameIndex);
        // Return the parcelable bundle.
        return bundle;
    }

    /**
     * Hook allowing a view to  re-apply a representation of its internal state that had previously been generated by onSaveInstanceState().
     * This function will never be called with a null state.
     *
     * @param state The frozen state that had previously been returned by onSaveInstanceState()
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mShapeNameIndex = bundle.getInt("shape_name_index");
            mShapeName = mShapeNameValues[mShapeNameIndex];
            // Load the base view state.
            state = bundle.getParcelable("instance_view_state");
        }
        super.onRestoreInstanceState(state);
    }

    public void setShapeColor(int shapeColor) {
        this.mShapeColor = shapeColor;
        /*
         * invalidate: Invalidate the whole view.
         * If the view is visible, onDraw(android.graphics.Canvas) will be called at some point in the future.
         * This must be called from a UI thread.
         * To call from a non-UI thread, call postInvalidate().
         */
        // To force a view to draw, call invalidate().
        invalidate();
        // To initiate a layout, call requestLayout().
        // This method is typically called by a view on itself when it believes that it can no longer fit within its current bounds.
        requestLayout();
    }

    public void setShapeName(String shapeName) {
        this.mShapeName = shapeName;
        // Force a view to draw.
        invalidate();
        // Initiate the layout.
        requestLayout();
    }

    public void setDisplayShapeName(boolean displayShapeName) {
        this.mDisplayShapeName = displayShapeName;
        // Force a view to draw.
        invalidate();
        // Initiate the layout.
        requestLayout();
    }

    public int getShapeColor() {
        return this.mShapeColor;
    }

    public String getShapeName() {
        return this.mShapeName;
    }

    public boolean isDisplayShapeName() {
        return this.mDisplayShapeName;
    }

    public Path getTrianglePath() {
        Point p1 = new Point(0, mShapeHeight);
        Point p2 = new Point(p1.x + mShapeWidth, p1.y);
        Point p3 = new Point(p1.x + (mShapeWidth / 2), p1.y - mShapeHeight);
        Path trianglePath = new Path();
        trianglePath.moveTo(p1.x, p1.y);
        trianglePath.lineTo(p2.x, p2.y);
        trianglePath.lineTo(p3.x, p3.y);
        return trianglePath;
    }
}
