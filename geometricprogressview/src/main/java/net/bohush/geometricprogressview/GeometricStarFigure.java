package net.bohush.geometricprogressview;

/**
 * Created by Christian Ringshofer on 09.03.2017.
 */
public class GeometricStarFigure extends GeometricFigure {

    @Override
    float geAngleOffset() {
        return (180f - mAngle) * 0.5f;
    }

    @Override
    GeometricFigure build() {
        float cos = (float) Math.cos(Math.toRadians(mAngle * 0.5f)) * 0.65f;
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
