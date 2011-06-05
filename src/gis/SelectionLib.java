package gis;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Set;
import java.util.Vector;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.identity.FeatureId;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import java.util.HashSet;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.identity.Identifier;

public class SelectionLib {

    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    private GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);
    

    public enum GeomType { POINT, LINE, POLYGON };

    private static final Color LINE_COLOUR = Color.BLACK;
    private static final Color FILL_COLOUR = Color.WHITE;
    private static final Color SELECTED_LINE_COLOUR = Color.RED;
    private static final Color SELECTED_FILL_COLOUR = Color.lightGray;
    private static final float SELECTED_LINE_WIDTH = 2.0f;
    private static final float OPACITY = 0.7f;
    private static final float LINE_WIDTH = 1.0f;
    private static final float POINT_SIZE = 8.0f;

    public static Style style;
    public static int LAYER_NUMBER = 0;
    private GeomType geometryType;
    private JMapFrame mapFrame;
    private ID ids;
    private FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
    private MapViewer mapViewer;
    private String geometryAttributeName;
    public ReferencedEnvelope extendEnvelope;
    public SelectedFeaturesCollection sFeatures =  new SelectedFeaturesCollection();
    public FeatureCollection<SimpleFeatureType, SimpleFeature> selectedFeatures2;
    public ReferencedEnvelope bboxForSides;


    public SelectionLib (JMapFrame mapFrame, MapViewer mapViewer) {
        this.ids = new ID();
        this.mapFrame = mapFrame;
        this.mapViewer = mapViewer;
    }

    public SelectionLib (JMapFrame mapFrame, FeatureSource<SimpleFeatureType, SimpleFeature> featureSource, MapViewer mapViewer) {
        this.ids = new ID();
        this.mapFrame = mapFrame;
        this.featureSource = featureSource;
        this.mapViewer = mapViewer;
    }

    public void selectCurrentFeatures(Vector<SimpleFeature> feats) {
        this.ids.IDs.clear();
        this.sFeatures.features.clear();
        for (int i = 0; i < feats.size(); i++) {
            SimpleFeature feature = feats.get(i);
            this.ids.IDs.add(feature.getIdentifier());
            this.sFeatures.features.add(feature);
            //mapFrame.getMapPane().repaint();
            if (ids.IDs.isEmpty()) {
                System.out.println(" no feature selected");
                this.sFeatures.features.clear();
                this.mapFrame.repaint();
            }
            displaySelectedFeatures();             
        }
    }

    public void selectFeatures(Vector<SimpleFeature> feats) {     
        for (int i = 0; i < feats.size(); i++) {
            SimpleFeature feature = feats.get(i);

            if (ids.IDs.contains(feature.getIdentifier())) {
                ids.IDs.remove(feature.getIdentifier());
                sFeatures.features.remove(feature);
            } else {
                ids.IDs.add(feature.getIdentifier());
                sFeatures.features.add(feature);
            }
            
            //ids.IDs.add(feature.getIdentifier());
            //sFeatures.features.add(feature);
            //mapFrame.getMapPane().repaint();
            if (ids.IDs.isEmpty()) {
                System.out.println(" no feature selected");
                this.sFeatures.features.clear();
                mapFrame.repaint();
            }
            displaySelectedFeatures();
        }
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> selectFeatures (Rectangle screenRect) {      //MapMouseEvent ev
        setGeometry();
        FeatureCollection<SimpleFeatureType, SimpleFeature> selectedFeatures = null;
        AffineTransform screenToWorld = mapFrame.getMapPane().getScreenToWorldTransform();
        Rectangle2D worldRect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
        ReferencedEnvelope bbox = new ReferencedEnvelope(worldRect, mapFrame.getMapContext().getCoordinateReferenceSystem());
        
        //Filter filter = ff.intersects(ff.property(geometryAttributeName), ff.literal(bbox));
        Filter filter = ff.bbox(ff.property(geometryAttributeName), bbox);

        try {
            selectedFeatures = featureSource.getFeatures(filter);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Slected size: " + selectedFeatures.size());
        return selectedFeatures;
    }
    
    public FeatureCollection<SimpleFeatureType, SimpleFeature> selectFeatures (Coordinate coordinate) {      //MapMouseEvent ev
        setGeometry();
        FeatureCollection<SimpleFeatureType, SimpleFeature> selectedFeatures = null;
        Geometry p = gf.createPoint(coordinate);

        Filter filter = ff.intersects(ff.property("the_geom"), ff.literal(p));

        try {
            selectedFeatures = featureSource.getFeatures(filter);
            //System.out.println("selectedFeatures pries DWITHIN: "+selectedFeatures.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return selectedFeatures;
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getRingFeatures (Coordinate coordinate, double distance, double ringWidth) {
        setGeometry();
        FeatureCollection<SimpleFeatureType, SimpleFeature> selectedFeatures = null;

        Point p = gf.createPoint(coordinate);

        Filter f1 = ff.dwithin(ff.property("the_geom"), ff.literal(p), distance + ringWidth, "meters");
        Filter f2 = ff.beyond(ff.property("the_geom"), ff.literal(p), distance - ringWidth, "meters");
        Filter f3 = ff.and(f1, f2);

        Filter f11 = ff.dwithin(ff.property("the_geom"), ff.literal(p), distance+2*ringWidth, "meters");
        Filter f22 = ff.beyond(ff.property("the_geom"), ff.literal(p), distance-2*ringWidth, "meters");
        Filter f33 = ff.and(f11, f22);

       

        Filter f0 = ff.or(f3, f33);

        Filter f4 = ff.dwithin(ff.property("the_geom"), ff.literal(p), distance+4.5*ringWidth, "meters");
        Filter f5 = ff.beyond(ff.property("the_geom"), ff.literal(p), distance-4.5*ringWidth, "meters");
        Filter f6 = ff.and(f4, f5);

        Filter f44 = ff.dwithin(ff.property("the_geom"), ff.literal(p), distance+10.5*ringWidth, "meters");
        Filter f55 = ff.beyond(ff.property("the_geom"), ff.literal(p), distance-10.5*ringWidth, "meters");
        Filter f66 = ff.and(f44, f55);

        Filter f00 = ff.or(f6, f66);

        Filter f = ff.or(f0, f00);
       
        try {
            
            //this.bboxForSides = featureSource.getFeatures(outsideRingFilter).getBounds();
            //selectedFeatures = featureSource.getFeatures(f);

            this.bboxForSides = featureSource.getFeatures(f).getBounds();
            selectedFeatures = featureSource.getFeatures(f);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("DWITHIN AND BEYOND size: " + selectedFeatures.size());
        return selectedFeatures;
    }
    
    public void selectFeatures(FeatureCollection<SimpleFeatureType, SimpleFeature> selectedFeatures) {
        selectedFeatures2 = selectedFeatures;                
            FeatureIterator<SimpleFeature> iter = selectedFeatures.features();
            try { 
                while (iter.hasNext()) {
                    SimpleFeature feature = iter.next();

                    if (ids.IDs.contains(feature.getIdentifier())) {
                        ids.IDs.remove(feature.getIdentifier());
                        sFeatures.features.remove(feature);
                        this.extendEnvelope = null;

                    } else {
                        ids.IDs.add(feature.getIdentifier());
                        sFeatures.features.add(feature);
                    }
                    mapFrame.getMapPane().repaint();
                }
            } finally {
                iter.close();
            }
            if (ids.IDs.isEmpty()) {
                System.out.println(" no feature selected");
                this.sFeatures.features.clear();
                this.extendEnvelope = null;
                mapFrame.repaint();
            }
            displaySelectedFeatures();      
        
        //this.ids.printCount();
        //this.sFeatures.printCount();
    }

    public void selectFeaturesExtend (FeatureCollection<SimpleFeatureType, SimpleFeature> selectedFeatures) {
        selectedFeatures2 = selectedFeatures;
            FeatureIterator<SimpleFeature> iter = selectedFeatures.features();
            try {
                while (iter.hasNext()) {
                    SimpleFeature feature = iter.next();

                    if (ids.IDs.contains(feature.getIdentifier())) {
                        //ids.IDs.remove(feature.getIdentifier());
                        //sFeatures.features.remove(feature);
                        this.extendEnvelope = null;

                    } else {
                        ids.IDs.add(feature.getIdentifier());
                        sFeatures.features.add(feature);
                    }
                    mapFrame.getMapPane().repaint();
                }
            } finally {
                iter.close();
            }
            if (ids.IDs.isEmpty()) {
                System.out.println(" no feature selected");
                this.sFeatures.features.clear();
                this.extendEnvelope = null;
                mapFrame.repaint();
            }
            displaySelectedFeatures();
    }

    public void clearAll () {
        this.getID().IDs.clear();
        this.sFeatures.features.clear();
        this.extendEnvelope = null;
    }

    public void collectEnvelopes () {
        setGeometry();
        Filter filter2 = ff.id(this.ids.IDs);
        try {
            FeatureCollection<SimpleFeatureType, SimpleFeature> selFeatures = featureSource.getFeatures(filter2);
            ReferencedEnvelope envelope = (ReferencedEnvelope) selFeatures.getBounds();
            if (extendEnvelope == null) {
                extendEnvelope = envelope;
            } else {
                extendEnvelope.expandToInclude(envelope);
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmptyArea (Coordinate coord, double width) {
        setGeometry();
        boolean ret = false;

        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(coord.x - width, coord.y - width);
        coordinates[1] = new Coordinate(coord.x - width, coord.y + width);
        coordinates[2] = new Coordinate(coord.x + width, coord.y + width);
        coordinates[3] = new Coordinate(coord.x + width, coord.y - width);
        coordinates[4] = new Coordinate(coord.x - width, coord.y - width);

        LinearRing ring = gf.createLinearRing( coordinates );

        Polygon p = gf.createPolygon(ring, null);
        
        try {
            //Filter filter = ff.bbox(ff.property(geometryAttributeName), bbox.getMinX() +0.0000001 , bbox.getMinY() +0.0000001, bbox.getMaxX() -0.0000001, bbox.getMaxY() -0.0000001, "");
            Filter filter = ff.intersects(ff.property(geometryAttributeName), ff.literal(p));
            FeatureCollection<SimpleFeatureType, SimpleFeature> f = featureSource.getFeatures(filter);
            //System.out.println("keliu leyerio SIZE: " + f.size());
            if (f.isEmpty())
                ret = true;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    /**
    public boolean isCross (Coordinate c) {
        setGeometry();
        boolean ret = false;
        Geometry point = gf.createPoint(c);
        Filter f = ff.crosses(ff.property("THE_GEOM"), ff.literal(point));
    }
    **/

    public FeatureCollection<SimpleFeatureType, SimpleFeature> isIntersect (Coordinate from, Coordinate to) {
        setGeometry();
        FeatureCollection<SimpleFeatureType, SimpleFeature> ret = null;
        Coordinate[] coordinates = {from, to};
        Geometry line = gf.createLineString(coordinates);
        Filter f = null;
        if (this.geometryType == GeomType.POINT) {
            f = ff.dwithin(ff.property("the_geom"), ff.literal(line), 500, "meters");
        } else {
            f = ff.intersects(ff.property("the_geom"), ff.literal(line));
        }
        try {
            ret = this.featureSource.getFeatures(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
        
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> isRoads (Coordinate coordinate, double distance) {
        FeatureCollection<SimpleFeatureType, SimpleFeature> ret = null;
        setGeometry();
        Point point = gf.createPoint(coordinate);
        Filter f = ff.dwithin(ff.property("the_geom"), ff.literal(point), distance, "meters");
        try {
            ret = this.featureSource.getFeatures(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public boolean isRoads (ReferencedEnvelope env, double distance) {
        boolean ret = true;
        setGeometry();
        Geometry polygon = gf.toGeometry(env);
        Filter f = ff.dwithin(ff.property("the_geom"), ff.literal(polygon), distance, "meters");
        try {
            if (this.featureSource.getFeatures(f).isEmpty()) ret = false;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getPavirsLTFeatures() {
        setGeometry();
        Filter filter2 = ff.id(this.ids.IDs);
        FeatureCollection<SimpleFeatureType, SimpleFeature> selFeatures = null;
        try {
            selFeatures = featureSource.getFeatures(filter2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selFeatures;
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> selectSide (FeatureCollection<SimpleFeatureType, SimpleFeature> feats,
            Coordinate a, Coordinate center, Coordinate b) {
        setGeometry();
        
        Coordinate[] coordinates = new Coordinate[4];
        coordinates[0] = a;
        coordinates[1] = center;
        coordinates[2] = b;
        coordinates[3] = a;

        LinearRing ring = gf.createLinearRing( coordinates );
        
        Polygon p = gf.createPolygon(ring, null);
        Filter f1 = ff.intersects(ff.property(geometryAttributeName), ff.literal(p));
        Filter f2 = ff.id(convertToSet(feats));
        Filter f3 = ff.and(f1, f2);
        
        FeatureCollection<SimpleFeatureType, SimpleFeature> ret = null;
        try {
            ret = this.featureSource.getFeatures(f3);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public Set<FeatureId> convertToSet (FeatureCollection<SimpleFeatureType, SimpleFeature> feats) {
        Set<FeatureId> ret = new HashSet();
        FeatureIterator<SimpleFeature> iter = feats.features();
        while (iter.hasNext()) {
            ret.add(iter.next().getIdentifier());
        }
        return ret;
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getSelectedFeatures() {
        FeatureCollection<SimpleFeatureType, SimpleFeature> selFeatures = null;
        Filter filter1 = ff.id(this.ids.IDs);
        try {
            selFeatures = featureSource.getFeatures(filter1);
            System.out.println("ONLY Selected features on layer: " + selFeatures.size());

            //ReferencedEnvelope envelope = (ReferencedEnvelope) selFeatures.getBounds();
            //Filter filter2 = ff.bbox(ff.property(geometryAttributeName), envelope);
            //selFeatures = featureSource.getFeatures(filter2);
            //System.out.println("ENVELOPED features on layer: " + selFeatures.size());
            //clearAll();
            
            System.out.println("Auksciu skirtumai");
            System.out.println("skir: " + calculateHeight(selFeatures));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selFeatures;
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getAreaFeatures (Coordinate coordinate, double distance) {
        FeatureCollection<SimpleFeatureType, SimpleFeature> feats = null;
        
        Point p = gf.createPoint(coordinate);
        Filter f = ff.dwithin(ff.property("the_geom"), ff.literal(p), distance, "meters");

        try {
            feats = featureSource.getFeatures(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
        return feats;
    }

    public ReferencedEnvelope getAreaEnvelope (Coordinate coordinate, double distance) {
        ReferencedEnvelope bbox = null;
        FeatureCollection<SimpleFeatureType, SimpleFeature> feats = null;

        Point p = gf.createPoint(coordinate);
        Filter f = ff.dwithin(ff.property(geometryAttributeName), ff.literal(p), distance, "meters");
        try {
            feats = featureSource.getFeatures(f);
            bbox = feats.getBounds();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bbox;
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getToAreaPolygons (Coordinate coordinate, double distance) {
        FeatureCollection<SimpleFeatureType, SimpleFeature> ret = null;
        Point p = gf.createPoint(coordinate);
        Filter f = ff.dwithin(ff.property(geometryAttributeName), ff.literal(p), distance, "meters");
        try {
            ret = featureSource.getFeatures(f);
            //bbox = ret.getBounds();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getRectFeatures (ReferencedEnvelope bbox) {
        FeatureCollection<SimpleFeatureType, SimpleFeature> feats = null;
        setGeometry();
        Filter filter = ff.bbox(ff.property(geometryAttributeName), bbox.getMinX() +0.0000001 , bbox.getMinY() +0.0000001, bbox.getMaxX() -0.0000001, bbox.getMaxY() -0.0000001, "");
        //Filter filter = ff.bbox(ff.property(geometryAttributeName), bbox);
        try {
            
            feats = featureSource.getFeatures(filter);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return feats;
    }

    public void getSelectedFeaturesOnLayer() {
        setGeometry();
        FeatureCollection<SimpleFeatureType, SimpleFeature> selFeatures = getSelectedFeatures();
        System.out.println("selFeature size: " + selFeatures.size());
        if (selFeatures != null) {
            FeatureIterator<SimpleFeature> iter = selFeatures.features();
            while (iter.hasNext()) {
                SimpleFeature feature = iter.next();
                if (ids.IDs.contains(feature.getIdentifier())) {
                    //ids.IDs.remove(feature.getIdentifier());
                    //sFeatures.features.remove(feature);
                    this.extendEnvelope = null;
                } else {
                    ids.IDs.add(feature.getIdentifier());
                    sFeatures.features.add(feature);
                }
                mapFrame.getMapPane().repaint();
            }
            displaySelectedFeatures();
        }
    }

    public Double calculateHeight(FeatureCollection<SimpleFeatureType, SimpleFeature> features) {
        if ((features.isEmpty()) || (features.size() == 1)) {
            return 0.0;
        }
        FeatureIterator<SimpleFeature> iter = features.features();
        Double minHeight = 999999.0;
        Double maxHeight = 0.0;
        //System.out.println("size: "+ features.size());
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            Double temp = null;
            try {
                temp = (Double) feature.getAttribute(3);
            } catch(ClassCastException e) {
                temp = Double.parseDouble(Integer.toString((Integer)feature.getAttribute(3)));
            }
            if (temp.compareTo(maxHeight) > 0) maxHeight = temp;
            if (temp.compareTo(minHeight) < 0) minHeight = temp;
        }
        return maxHeight - minHeight;
    }

    public void expandEnvelope () {
        extendEnvelope.expandToInclude(extendEnvelope.getMinX()+10, extendEnvelope.getMinY()+10);
        extendEnvelope.expandToInclude(extendEnvelope.getMinX()-10, extendEnvelope.getMinY()-10);
    }

    public void zoomToFeature() {
        if (!this.ids.IDs.isEmpty()) {
            expandEnvelope();
            mapFrame.getMapPane().setDisplayArea(extendEnvelope);     //envelope
        } else {
            try {
                mapFrame.getMapPane().setDisplayArea(mapFrame.getMapPane().getMapContext().getLayerBounds());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void makeFeatureCollection() {
        selectedFeatures2.clear();
        for (int i = 0; i < sFeatures.features.size(); i++) {
            selectedFeatures2.add(sFeatures.features.get(i));
        }
    }

    public void displaySelectedFeatures() {     //Set<FeatureId> IDs
        Style newStyle;
        if (this.ids.IDs.isEmpty()) {
            newStyle = style;
        } else {
            newStyle = createStyle(this.ids.IDs);
        }
        mapFrame.getMapContext().getLayer(LAYER_NUMBER).setStyle(newStyle);
        mapFrame.getMapPane().repaint();
        mapFrame.repaint();
        //this.ids.printCount();
    }

    private Style createDefaultStyle() {
        //this.style.
        Rule rule = createRule(LINE_COLOUR, FILL_COLOUR, LINE_WIDTH);
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        fts.rules().add(rule);
        Style style = sf.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

    private Style createStyle(Set<FeatureId> IDs) {

        Rule selectedRule = createRule(SELECTED_LINE_COLOUR, SELECTED_FILL_COLOUR, SELECTED_LINE_WIDTH);
        selectedRule.setFilter(ff.id(IDs));
        
        Rule otherRule;
        //otherRule = createRule(SELECTED_LINE_COLOUR, SELECTED_FILL_COLOUR, SELECTED_LINE_WIDTH);
        otherRule = this.style.featureTypeStyles().get(0).rules().get(this.style.featureTypeStyles().get(0).rules().size()-1);
        
        otherRule.setElseFilter(true);
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        fts.rules().add(selectedRule);

        fts.rules().add(otherRule);
        Style style = sf.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

    private DStore findDStoreBySource(FeatureSource<SimpleFeatureType, SimpleFeature> featureSource) {
        DStore dStore = null;
        System.out.println(mapViewer.stores.stores.size());
        for(int i = 0; i < mapViewer.stores.stores.size(); i++) {
            try {
                FeatureSource<SimpleFeatureType, SimpleFeature> fff = (FeatureSource<SimpleFeatureType, SimpleFeature>) mapViewer.stores.stores.get(i).fileDataStore.getFeatureSource();
                if (fff.equals(featureSource)) {
                    System.out.println("rastas");
                    return mapViewer.stores.stores.get(i);
                } else
                    System.out.println("nerastas");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }      
        return dStore;
    }

    private Rule createRule(Color outlineColor, Color fillColor, float lineWidth) {
        Symbolizer symbolizer = null;
        Fill fill = null;
        Stroke stroke = sf.createStroke(ff.literal(outlineColor), ff.literal(lineWidth));
        switch (geometryType) {
            case POLYGON:
                fill = sf.createFill(ff.literal(fillColor), ff.literal(OPACITY));
                symbolizer = sf.createPolygonSymbolizer(stroke, fill, geometryAttributeName);
                break;

            case LINE:
                symbolizer = sf.createLineSymbolizer(stroke, geometryAttributeName);
                break;

            case POINT:
                fill = sf.createFill(ff.literal(fillColor), ff.literal(OPACITY));
                Mark mark = sf.getCircleMark();
                mark.setFill(fill);
                mark.setStroke(stroke);
                Graphic graphic = sf.createDefaultGraphic();
                graphic.graphicalSymbols().clear();
                graphic.graphicalSymbols().add(mark);
                graphic.setSize(ff.literal(POINT_SIZE));
                symbolizer = sf.createPointSymbolizer(graphic, geometryAttributeName);
        }
        Rule rule = sf.createRule();
        rule.symbolizers().add(symbolizer);
        return rule;
    }

    private void setGeometry() {
        GeometryDescriptor geomDesc = featureSource.getSchema().getGeometryDescriptor();
        geometryAttributeName = geomDesc.getLocalName();
        //System.out.println("geometryAttributeName: "+geometryAttributeName);
        Class<?> clazz = geomDesc.getType().getBinding();
        if (Polygon.class.isAssignableFrom(clazz) ||
            MultiPolygon.class.isAssignableFrom(clazz)) {
            geometryType = GeomType.POLYGON;
        } else if (LineString.class.isAssignableFrom(clazz) ||
            MultiLineString.class.isAssignableFrom(clazz)) {
            geometryType = GeomType.LINE;
        } else {
            geometryType = GeomType.POINT;
        }
    }

    public void setGeometry(FeatureSource<SimpleFeatureType, SimpleFeature> featureSource) {
        this.featureSource = featureSource;
        GeometryDescriptor geomDesc = featureSource.getSchema().getGeometryDescriptor();
        geometryAttributeName = geomDesc.getLocalName();
        Class<?> clazz = geomDesc.getType().getBinding();
        if (Polygon.class.isAssignableFrom(clazz) ||
            MultiPolygon.class.isAssignableFrom(clazz)) {
            geometryType = GeomType.POLYGON;
        } else if (LineString.class.isAssignableFrom(clazz) ||
            MultiLineString.class.isAssignableFrom(clazz)) {
            geometryType = GeomType.LINE;
        } else {
            geometryType = GeomType.POINT;

        }
    }

    public void setFeatureSource (FeatureSource<SimpleFeatureType, SimpleFeature> featureSource) {
        this.featureSource = featureSource;
    }

    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource () {
        return this.featureSource;
    }

    public ID getID () {
        return ids;
    }

    public void setID (ID ids) {
        this.ids = ids;
    }

    public void setGeometryType(GeomType g) {
        this.geometryType = g;
    }

    public void setExtendEnvelope (ReferencedEnvelope extendEnvelope) {
        this.extendEnvelope = extendEnvelope;
    }

    public GeomType getGeomType () {
        return this.geometryType;
    }

    
    
}
