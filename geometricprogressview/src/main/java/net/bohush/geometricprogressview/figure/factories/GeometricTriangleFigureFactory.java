package net.bohush.geometricprogressview.figure.factories;

import net.bohush.geometricprogressview.figure.GeometricTriangleFigure;

/**
 * Created by Christian Ringshofer on 10.03.2017.
 */
public class GeometricTriangleFigureFactory extends GeometricFigureFactory<GeometricTriangleFigure> {

    @Override
    public GeometricTriangleFigure create() {
        return new GeometricTriangleFigure();
    }

}
