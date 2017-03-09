package net.bohush.geometricprogressview.figure.factories;

import net.bohush.geometricprogressview.figure.GeometricKiteFigure;

/**
 * Created by Christian Ringshofer on 10.03.2017.
 */
public class GeometricKiteFigureFactory extends GeometricFigureFactory<GeometricKiteFigure> {

    @Override
    public GeometricKiteFigure create() {
        return new GeometricKiteFigure();
    }

}
