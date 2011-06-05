package gis;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.styling.*;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.ExceptionMonitor;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.opengis.filter.FilterFactory2;

public class StyleLib {


    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
    private static final float OPACITY = 0.7f;
    private static final float POINT_SIZE = 10.0f;
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);


    public StyleLib() {
    }

    public Style createStyle2(DStore store) {
        try {
            FeatureSource featureSource = store.fileDataStore.getFeatureSource();
            SimpleFeatureType schema = (SimpleFeatureType)featureSource.getSchema();
            Class geomType = schema.getGeometryDescriptor().getType().getBinding();
            if (Polygon.class.isAssignableFrom(geomType) || MultiPolygon.class.isAssignableFrom(geomType)) {
                return createPolygonStyle(store);
            } else if (LineString.class.isAssignableFrom(geomType) || MultiLineString.class.isAssignableFrom(geomType)) {
                        return createLineStyle(store);
            } else {
                return createPointStyle(store);
            }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        return null;
    }

    public Style createPolygonStyle(DStore store) {
        // create a partially opaque outline stroke
        RandomColor color = new RandomColor();
        Color temp;
        temp = color.randomColor();
        Stroke stroke = styleFactory.createStroke(
        filterFactory.literal(temp),        //Color.black
        filterFactory.literal(1),
        filterFactory.literal(OPACITY));
        store.setLineColor(temp);
        // create a partial opaque fill
        temp = Color.WHITE;
        Fill fill = styleFactory.createFill(
        filterFactory.literal(temp),
        filterFactory.literal(OPACITY));
        store.setFillColor(temp);
        /*
        * Setting the geometryPropertyName arg to null signals that we want to
        * draw the default geomettry of features
        */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

    public Style createPolygonStyle2() {
        // create a partially opaque outline stroke
        RandomColor color = new RandomColor();
        Color temp;
        temp = color.randomColor();
        Stroke stroke = styleFactory.createStroke(
        filterFactory.literal(temp),        //Color.black
        filterFactory.literal(1),
        filterFactory.literal(OPACITY));
        //store.setLineColor(temp);
        // create a partial opaque fill
        temp = Color.WHITE;
        Fill fill = styleFactory.createFill(
        filterFactory.literal(temp),
        filterFactory.literal(OPACITY));
        //store.setFillColor(temp);
        /*
        * Setting the geometryPropertyName arg to null signals that we want to
        * draw the default geomettry of features
        */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

    private Style createLineStyle(DStore store) {
        RandomColor color = new RandomColor();
        Color temp;
        temp = color.randomColor();
        Stroke stroke = styleFactory.createStroke(
        filterFactory.literal(color.randomColor()),
        filterFactory.literal(1));
        store.setLineColor(temp);
        /*
        * Setting the geometryPropertyName arg to null signals that we want to
        * draw the default geomettry of features
        */
        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

    public Style createLineStyle2 () {
        RandomColor color = new RandomColor();
        Color temp;
        temp = color.randomColor();
        Stroke stroke = styleFactory.createStroke(
        filterFactory.literal(color.randomColor()),
        filterFactory.literal(1));
        //store.setLineColor(temp);
        /*
        * Setting the geometryPropertyName arg to null signals that we want to
        * draw the default geomettry of features
        */
        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

    private Style createPointStyle(DStore store) {
        Graphic gr = styleFactory.createDefaultGraphic();
        Mark mark = styleFactory.getCircleMark();
        RandomColor color = new RandomColor();
        Color temp;
        temp = color.randomColor();

        mark.setStroke(styleFactory.createStroke(filterFactory.literal(temp), filterFactory.literal(1)));
        store.setLineColor(temp);

        temp = Color.white;
        mark.setFill(styleFactory.createFill(filterFactory.literal(temp), filterFactory.literal(OPACITY)));
        //mark.setSize(filterFactory.literal(5));
        System.out.println("markSize: "+mark.getSize());
        mark.setSize(filterFactory.literal(10));
        System.out.println("markSize: "+mark.getSize());

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        store.setFillColor(temp);
        /*
        * Setting the geometryPropertyName arg to null signals that we want to
        * draw the default geomettry of features
        */
        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }
}
