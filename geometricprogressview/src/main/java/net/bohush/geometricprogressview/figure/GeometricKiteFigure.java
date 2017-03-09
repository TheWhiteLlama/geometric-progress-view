package net.bohush.geometricprogressview.figure;

/**
 * Created by Christian Ringshofer on 09.03.2017.
 */
public class GeometricKiteFigure extends GeometricFigure {

    @Override
    float geAngleOffset() {
        return (180f - mAngle) * 0.5f;
    }

    @Override
    public GeometricFigure build() {
        float cos = (float) Math.cos(Math.toRadians(mAngle * 0.5f));
        offset(mDistanceFromCenter, 0);
        start(0, 0);
        move(mRadius * cos, 0, -mAngle * 0.5f);
        move(mRadius, 0, 0f);
        move(mRadius * cos, 0, +mAngle * 0.5f);
        move(0, 0);
        end();
        return this;
    }

}