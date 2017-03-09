package net.bohush.geometricprogressview.figure.factories;

import net.bohush.geometricprogressview.figure.GeometricStarFigure;

/**
 * Created by Christian Ringshofer on 10.03.2017.
 */
public class GeometricStarFigureFactory extends GeometricFigureFactory<GeometricStarFigure> {

    @Override
    public GeometricStarFigure create() {
        return new GeometricStarFigure();
    }

}
