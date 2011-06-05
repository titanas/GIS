package gis;

import java.util.Vector;
import org.gdal.ogr.Feature;
import org.opengis.feature.simple.SimpleFeature;


public class SelectedFeaturesCollection {

    public Vector<SimpleFeature> features = new Vector<SimpleFeature>();

    public SelectedFeaturesCollection () {
    }

    public void printCount() {
        System.out.println("Feaetures count: " + features.size());
    }
}
