package net.bohush.geometricprogressview.figure.factories;

import net.bohush.geometricprogressview.figure.GeometricFigure;

/**
 * Created by Christian Ringshofer on 10.03.2017.
 */
public abstract class GeometricFigureFactory<T extends GeometricFigure> {

    public abstract T create();

}
