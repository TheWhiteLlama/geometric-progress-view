package net.bohush.geometricprogressview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;

/**
 * Created by Christian Ringshofer on 09.03.2017.
 */
abstract class GeometricFigure {

    @NonNull
    private Path mPath;

    @NonNull
    private Paint mPaint;

    float mRadius = 0f;
    float mAngle = 0f;
    private float mRotation = 0f;
    float mDistanceFromCenter = 0;

    private float mCenterX = 0f, mCenterY = 0f;
    private float mOffsetX = 0f, mOffsetY = 0f;

    private float sn, cs;

    GeometricFigure() {
        mPath = new Path();
        mPaint = new Paint();
        initPaint();
    }

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
    }

    GeometricFigure withCenter(float centerX, float centerY) {
        mCenterX = centerX;
        mCenterY = centerY;
        return this;
    }

    GeometricFigure withRadius(float radius) {
        mRadius = radius;
        return this;
    }

    GeometricFigure withDistanceFrom(float distanceFromCenter) {
        mDistanceFromCenter = distanceFromCenter;
        return this;
    }

    GeometricFigure withAngles(float angle, float rotation) {
        mAngle = angle;
        mRotation = rotation + geAngleOffset();
        sn = (float) Math.sin(Math.toRadians(mRotation));
        cs = (float) Math.cos(Math.toRadians(mRotation));
        return this;
    }

    abstract float geAngleOffset();

    GeometricFigure withColor(int color) {
        mPaint.setColor(color);
        return this;
    }

    GeometricFigure withAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        return this;
    }

    void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    void offset(float x, float y) {
        mOffsetX = x;
        mOffsetY = y;
    }

    void start(float x, float y) {
        pathTo(x + mOffsetX, y + mOffsetY, true);
    }

    void end() {
        mPath.close();
    }

    void move(float x, float y) {
        pathTo(x + mOffsetX, y + mOffsetY, false);
    }

    void move(float x, float y, float angle) {
        final float sn = (float) Math.sin(Math.toRadians(angle));
        final float cs = (float) Math.cos(Math.toRadians(angle));
        pathTo(x * cs - y * sn + mOffsetX, x * sn + y * cs + mOffsetY, false);
    }

    private void pathTo(float x, float y, boolean move) {
        final float rotatedX = x * cs - y * sn;
        final float rotatedY = x * sn + y * cs;
        if (move) {
            mPath.moveTo(
                    mCenterX + rotatedX,
                    mCenterY + rotatedY
            );
        } else {
            mPath.lineTo(
                    mCenterX + rotatedX,
                    mCenterY + rotatedY
            );
        }
    }

    abstract GeometricFigure build();

}
