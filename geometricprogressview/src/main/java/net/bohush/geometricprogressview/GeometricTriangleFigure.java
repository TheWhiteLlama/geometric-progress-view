package net.bohush.geometricprogressview;

/**
 * Created by Christian Ringshofer on 09.03.2017.
 * <p/>
 * <p/>
 * This document is a part of the source code and related artifacts
 * for Wooltasia, an closed source android application made for generating crocheting patterns:
 * <p/>
 * https://www.wooltasia.com
 * <p/>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Christian Ringshofer <christian.ringshofer@gmail.com>, 09.03.2017
 */
public class GeometricTriangleFigure extends GeometricFigure {

    @Override
    float geAngleOffset() {
        return 90;
    }

    @Override
    GeometricFigure build() {
        offset(mDistanceFromCenter, 0);
        start(0, 0);
        move(mRadius, 0, -mAngle * 0.5f);
        move(mRadius, 0, +mAngle * 0.5f);
        move(0, 0);
        end();
        return this;
    }

}
