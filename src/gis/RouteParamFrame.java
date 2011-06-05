package gis;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.geotools.data.FeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.geotools.geometry.jts.JTSFactoryFinder;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import java.io.IOException;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

public final class RouteParamFrame extends javax.swing.JFrame {

    public SelectionLib selectionLib;
    public MapContext mapContext;
    public JMapFrame mapFrame;
    public int MAX;
    public List model;
    public Vector<String> selectedLayers;
    
    public FeatureCollection<SimpleFeatureType, SimpleFeature> selectedPolygons = null;
    public FeatureCollection<SimpleFeatureType, SimpleFeature> selectedPolygons2 = null;
    public FeatureCollection<SimpleFeatureType, SimpleFeature> from = null;
    public FeatureCollection<SimpleFeatureType, SimpleFeature> toArea = null;
    public FeatureCollection<SimpleFeatureType, SimpleFeature> roadsNearFromArea = null;
    public FeatureCollection<SimpleFeatureType, SimpleFeature> roadsNearToArea = null;   
    public FeatureCollection<SimpleFeatureType, SimpleFeature> objects = null;

    public void createModel() {
        this.model = new ArrayList<String>();
        MapLayer[] layers = mapContext.getLayers();
        for (int i = 1; i < this.MAX; i++) {
            model.add(layers[i].getFeatureSource().getSchema().getName().getLocalPart());
        }
    }

    public javax.swing.AbstractListModel modelStruct = new javax.swing.AbstractListModel() {
        public int getSize() { return model.size(); }
        public Object getElementAt(int i) { return model.get(i); }
    };
    
    public RouteParamFrame(SelectionLib selectionLib, MapContext mapContext, List model, JMapFrame mapFrame, int MAX) {
        this.model = model;
        this.selectionLib = selectionLib;
        this.mapContext = mapContext;
        this.MAX = MAX;
        this.mapFrame = mapFrame;
        initComponents();
    }

    @Override
    public void setDefaultCloseOperation(int operation) {
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        AikstelesPlotis = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        AtstumasIkiKelio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        ReljefoSvyravimas = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        SkrydzioAtstumas = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        LankytiniObjektai = new javax.swing.JList();
        Rodyti = new javax.swing.JButton();
        Isvalyti = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        Noth = new javax.swing.JRadioButton();
        East = new javax.swing.JRadioButton();
        South = new javax.swing.JRadioButton();
        West = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        AreaAttributesButton = new javax.swing.JButton();
        RoadsAttributesButton = new javax.swing.JButton();
        ObjectsButton = new javax.swing.JButton();
        ClearMapButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Parametrų parinkimas");
        setResizable(false);

        jLabel1.setText("Aikštelės plotis:");

        AikstelesPlotis.setText("metrai");
        AikstelesPlotis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AikstelesPlotisActionPerformed(evt);
            }
        });

        jLabel2.setText("Aikštelės atstumas iki kelio:");

        AtstumasIkiKelio.setText("metrai");
        AtstumasIkiKelio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AtstumasIkiKelioActionPerformed(evt);
            }
        });

        jLabel3.setText("Aikštelės reljefo svyravimas:");

        ReljefoSvyravimas.setText("metrai");
        ReljefoSvyravimas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReljefoSvyravimasActionPerformed(evt);
            }
        });

        jLabel4.setText("Skrydžio atstumas:");

        SkrydzioAtstumas.setText("kilometrai");
        SkrydzioAtstumas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SkrydzioAtstumasActionPerformed(evt);
            }
        });

        jLabel5.setText("Lankytini objektai");

        jLabel6.setText("(galima pasirinkti kelis)");

        LankytiniObjektai.setModel(modelStruct);
        jScrollPane1.setViewportView(LankytiniObjektai);

        Rodyti.setText("Rodyti");
        Rodyti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RodytiActionPerformed(evt);
            }
        });

        Isvalyti.setText("Išvalyti laukus");
        Isvalyti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IsvalytiActionPerformed(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\compass.jpg")); // NOI18N

        jLabel8.setText("Pasirinkite vėjo kryptį");

        Noth.setText("Šiaurės");
        Noth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NothActionPerformed(evt);
            }
        });

        East.setText("Rytų");
        East.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EastActionPerformed(evt);
            }
        });

        South.setText("Pietų");
        South.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SouthActionPerformed(evt);
            }
        });

        West.setText("Vakarų");
        West.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        West.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WestActionPerformed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Atributiniai duomenys");

        AreaAttributesButton.setText("Aikštelių paviršius");
        AreaAttributesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AreaAttributesButtonActionPerformed(evt);
            }
        });

        RoadsAttributesButton.setText("Keliai šalia aikštelių");
        RoadsAttributesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RoadsAttributesButtonActionPerformed(evt);
            }
        });

        ObjectsButton.setText("Lankytini objektai");
        ObjectsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ObjectsButtonActionPerformed(evt);
            }
        });

        ClearMapButton.setText("Išvalyti žemėlapį");
        ClearMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearMapButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(West)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(Noth)
                            .addComponent(South, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(East))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AikstelesPlotis, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AtstumasIkiKelio, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SkrydzioAtstumas, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ReljefoSvyravimas, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Rodyti)
                .addGap(18, 18, 18)
                .addComponent(Isvalyti)
                .addGap(18, 18, 18)
                .addComponent(ClearMapButton)
                .addContainerGap(113, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(424, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AreaAttributesButton)
                .addGap(18, 18, 18)
                .addComponent(RoadsAttributesButton)
                .addGap(18, 18, 18)
                .addComponent(ObjectsButton)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AikstelesPlotis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(AtstumasIkiKelio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ReljefoSvyravimas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SkrydzioAtstumas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(jScrollPane1, 0, 0, Short.MAX_VALUE))
                .addGap(36, 36, 36)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(Noth, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(East, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(West, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(South)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Rodyti)
                    .addComponent(Isvalyti)
                    .addComponent(ClearMapButton))
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(9, 9, 9)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(AreaAttributesButton)
                        .addComponent(RoadsAttributesButton)
                        .addComponent(ObjectsButton)))
                .addContainerGap())
        );

        Noth.getAccessibleContext().setAccessibleName("North");
        East.getAccessibleContext().setAccessibleName("East");
        South.getAccessibleContext().setAccessibleName("South");
        West.getAccessibleContext().setAccessibleName("West");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AikstelesPlotisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AikstelesPlotisActionPerformed
        
    }//GEN-LAST:event_AikstelesPlotisActionPerformed

    private void SkrydzioAtstumasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SkrydzioAtstumasActionPerformed
        
    }//GEN-LAST:event_SkrydzioAtstumasActionPerformed

    private void AtstumasIkiKelioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AtstumasIkiKelioActionPerformed
        
    }//GEN-LAST:event_AtstumasIkiKelioActionPerformed

    private void ReljefoSvyravimasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReljefoSvyravimasActionPerformed
        
    }//GEN-LAST:event_ReljefoSvyravimasActionPerformed

    private void IsvalytiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IsvalytiActionPerformed
        this.AikstelesPlotis.setText("");
        this.AtstumasIkiKelio.setText("");
        this.ReljefoSvyravimas.setText("");
        this.SkrydzioAtstumas.setText("");
        this.LankytiniObjektai.clearSelection();
    }//GEN-LAST:event_IsvalytiActionPerformed

    public boolean validateFields() {
        boolean ret = true;
        try {
            Double.parseDouble(this.AikstelesPlotis.getText()) ;
        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Klaidingai įvestas aikštelės plotis");
            ret =  false;
        }
        
        try {
            Double.parseDouble(this.AtstumasIkiKelio.getText()) ;
        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Klaidingai įvestas atstumas iki kelio");
            ret =  false;
        }
        
        try {
            Double.parseDouble(this.ReljefoSvyravimas.getText()) ;
        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Klaidingai įvestas reljefo svyravimas");
            ret =  false;
        }
        
        try {
            Double.parseDouble(this.SkrydzioAtstumas.getText()) ;
        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Klaidingai įvestas skrydžio atstumas");
            ret =  false;
        }
        setSelectedValue();
        return ret;
    }

    private void setSelectedValue() {
        Object[] sel = this.LankytiniObjektai.getSelectedValues();
        this.selectedLayers = new Vector<String>();
        for (int i = 0; i < sel.length; i++) {
            this.selectedLayers.add((String)sel[i]);
        }
    }

    private void RodytiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RodytiActionPerformed

        if (validateFields()) {

            Double areaWidth = Double.parseDouble(this.AikstelesPlotis.getText());
            Double roadDistance = Double.parseDouble(this.AtstumasIkiKelio.getText());
            Double areaHeigth = Double.parseDouble(this.ReljefoSvyravimas.getText());
            Double routeDistance = Double.parseDouble(this.SkrydzioAtstumas.getText()) * 1000;
            
            MapLayer[] mapLayers = mapContext.getLayers();
            selectionLib.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[0].getFeatureSource());
            selectionLib.LAYER_NUMBER = 0;
            if (this.selectedPolygons == null) {
                this.selectedPolygons = new DefaultFeatureCollection(selectionLib.getPavirsLTFeatures());
            }

            QueryLib queryLib = new QueryLib();
            Coordinate coordinate = queryLib.centerFeatures(this.selectedPolygons);
            GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);
            Point fromPoint = gf.createPoint(coordinate).getCentroid();

            if (checkAreaWMsg(coordinate, areaWidth, roadDistance, areaHeigth)) {
                prepare();
                // Nusileidomo poligonu ziedas
                selectionLib.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[0].getFeatureSource());
                selectionLib.LAYER_NUMBER = 0;
                FeatureCollection<SimpleFeatureType, SimpleFeature> ring = selectionLib.getRingFeatures(coordinate, routeDistance, 25.0);

                //selectionLib.selectFeaturesExtend(ring);
                selectionLib.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[0].getFeatureSource());
                selectionLib.LAYER_NUMBER = 0;
                ring = checkAllSides(ring, selectionLib.bboxForSides);
                
                if ((ring != null) && (!ring.isEmpty())) {
                    int k = 0;
                    FeatureIterator<SimpleFeature> iter = ring.features();
                    while (iter.hasNext()) {

                        SimpleFeature feature = iter.next();
                        Geometry geom = (Geometry) feature.getDefaultGeometry();
                        Point toPoint = geom.getCentroid();
                        
                        double distance = fromPoint.distance(toPoint);
                        System.out.println((++k) + ". Distance : " + distance);
                        double ratio = (routeDistance - areaWidth/2) / distance;

                        if (checkAreaNoMsg(toPoint.getCoordinate(), areaWidth, roadDistance, areaHeigth)) {
                            if (!this.selectedLayers.isEmpty()) {
                                FeatureCollection<SimpleFeatureType, SimpleFeature> obs = checkRouteObjects(coordinate, toPoint.getCoordinate());
                                if ((obs != null) && (!obs.isEmpty())) {
                                    //SimpleFeature tf = modifyFeature2(feature, areaWidth, calculateCenter(fromPoint, toPoint, ratio));
                                    //System.out.println(" NAUJAS Distance : " + fromPoint.distance((Geometry)tf.getDefaultGeometry()));
                                    //this.toArea.add(tf);
                                    this.toArea.add(modifyFeature(feature, areaWidth));
                                    //this.toArea.addAll(getFeats(coordinate2, areaWidth));
                                    this.objects.addAll(obs);
                                } else {
                                    System.out.println("NETINKA");
                                }
                            } else {
                                //SimpleFeature tf = modifyFeature2(feature, areaWidth, calculateCenter(fromPoint, toPoint, ratio));
                                //System.out.println(" NAUJAS Distance : " + fromPoint.distance((Geometry)tf.getDefaultGeometry()));
                                //this.toArea.add(tf);
                                this.toArea.add(modifyFeature(feature, areaWidth));
                                //this.toArea.addAll(getFeats(coordinate2, areaWidth));
                                System.out.println("Nepasirinkta objektu");
                            }
                        } else System.out.println("NETINKA");
                    }
                    if ((this.toArea != null) && (!this.toArea.isEmpty())) {
                        StyleLib styleLib = new StyleLib();
                        mapContext.addLayer(this.from, styleLib.createPolygonStyle2());
                        mapContext.addLayer(this.toArea, styleLib.createPolygonStyle2());
                        mapContext.addLayer(makeDirectionLayer(this.from, this.toArea), styleLib.createLineStyle2());
                        //selectionLib.selectFeaturesExtend(this.toArea);
                        
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Pagal pasirinktus parametrus, sudaryti maršruto negalima.");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Pagal pasirinktus parametrus, sudaryti maršruto negalima.");
                }
            }
        }
    }//GEN-LAST:event_RodytiActionPerformed

    public FeatureCollection<SimpleFeatureType, SimpleFeature> makeDirectionLayer (FeatureCollection<SimpleFeatureType, SimpleFeature> from, FeatureCollection<SimpleFeatureType, SimpleFeature> to) {
        FeatureCollection<SimpleFeatureType, SimpleFeature> ret = new DefaultFeatureCollection(roadsNearToArea);
        ret.clear();

        FeatureIterator<SimpleFeature> it = from.features();
        Geometry geom = (Geometry) it.next().getDefaultGeometry();
        
        Coordinate[] coordinates = new Coordinate[2];

        coordinates[0]  = geom.getCentroid().getCoordinate();

        
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);

        MapLayer[] mapLayers = mapContext.getLayers();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter f1 = ff.id(selectionLib.convertToSet(toArea));
        Filter f2 = ff.id(selectionLib.convertToSet(from));
        Filter f3 = ff.and(f1, f2);
        Filter f4 = ff.not(f3);
        FeatureCollection <SimpleFeatureType, SimpleFeature> roads = null;
        try {
            FeatureSource<SimpleFeatureType, SimpleFeature> fs = (FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[0].getFeatureSource();
            roads = fs.getFeatures(f4);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FeatureIterator<SimpleFeature> iter = to.features();
        FeatureIterator<SimpleFeature> iter2 = roads.features();

        while (iter.hasNext() && iter2.hasNext()) {

            SimpleFeature f = iter.next();
            coordinates[1] = ((Geometry) f.getDefaultGeometry()).getCentroid().getCoordinate();
            LineString line = gf.createLineString(coordinates);
            //f.setDefaultGeometry(line);


            SimpleFeature newf = iter2.next();
            newf.setDefaultGeometry(line);
            //SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
            //SimpleFeature newf = builder.buildFeature("123");
            //newf.setDefaultGeometry(line);

            
            ret.add(newf);
        }
        return ret;
    }

    public Coordinate calculateCenter (Point from, Point to, Double ratio) {

        System.out.println("Current point center: " + to.getX() + "; " + to.getY());
        double diffx = to.getX() - from.getX();
        double diffy = to.getY() - from.getY();
        double x;
        double y;
        //if (ratio.compareTo(1.0) > 0) {
            x = from.getX() + diffx * ratio;
            y = from.getY() + diffy * ratio;
        //} else {
        //    x = to.getX() + diffx * ratio;
        //    y = to.getY() + diffy * ratio;
        //}
        
        System.out.println("NEW point center: " + x + "; " + y);
        return new Coordinate(x, y);
    }

    public SimpleFeature modifyFeature2 (SimpleFeature feat, double areaWidth, Coordinate center) {
        //Geometry geom = (Geometry) feat.getDefaultGeometry();
        //Coordinate center = geom.getCentroid().getCoordinate();

        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(center.x - areaWidth/2, center.y - areaWidth/2);
        coordinates[1] = new Coordinate(center.x - areaWidth/2, center.y + areaWidth/2);
        coordinates[2] = new Coordinate(center.x + areaWidth/2, center.y + areaWidth/2);
        coordinates[3] = new Coordinate(center.x + areaWidth/2, center.y - areaWidth/2);
        coordinates[4] = new Coordinate(center.x - areaWidth/2, center.y - areaWidth/2);

        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);

        LinearRing ring = gf.createLinearRing( coordinates );

        Polygon p = gf.createPolygon(ring, null);
        feat.setDefaultGeometry(p);
        return feat;
    }

    public SimpleFeature modifyFeature (SimpleFeature feat, double areaWidth) {
        Geometry geom = (Geometry) feat.getDefaultGeometry();
        Coordinate center = geom.getCentroid().getCoordinate();

        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(center.x - areaWidth/2, center.y - areaWidth/2);
        coordinates[1] = new Coordinate(center.x - areaWidth/2, center.y + areaWidth/2);
        coordinates[2] = new Coordinate(center.x + areaWidth/2, center.y + areaWidth/2);
        coordinates[3] = new Coordinate(center.x + areaWidth/2, center.y - areaWidth/2);
        coordinates[4] = new Coordinate(center.x - areaWidth/2, center.y - areaWidth/2);

        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);

        LinearRing ring = gf.createLinearRing( coordinates );

        Polygon p = gf.createPolygon(ring, null);
        feat.setDefaultGeometry(p);
        return feat;
    }

    public SimpleFeature modifyFeature (SimpleFeature feat, Coordinate center, double areaWidth) {
        //Geometry geom = (Geometry) feat.getDefaultGeometry();

        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(center.x - areaWidth/2, center.y - areaWidth/2);
        coordinates[1] = new Coordinate(center.x - areaWidth/2, center.y + areaWidth/2);
        coordinates[2] = new Coordinate(center.x + areaWidth/2, center.y + areaWidth/2);
        coordinates[3] = new Coordinate(center.x + areaWidth/2, center.y - areaWidth/2);
        coordinates[4] = new Coordinate(center.x - areaWidth/2, center.y - areaWidth/2);

        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);

        LinearRing ring = gf.createLinearRing( coordinates );

        Polygon p = gf.createPolygon(ring, null);
        feat.setDefaultGeometry(p);
        return feat;
    }

    public void prepare() {
        this.toArea = new DefaultFeatureCollection(this.selectedPolygons);
        this.toArea.clear();
        this.roadsNearToArea = new DefaultFeatureCollection(this.selectedPolygons);
        this.roadsNearToArea.clear();
        this.objects = new DefaultFeatureCollection(this.selectedPolygons);
        this.objects.clear();
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> checkAllSides(FeatureCollection<SimpleFeatureType, SimpleFeature> feats, ReferencedEnvelope bbox) {
        FeatureCollection<SimpleFeatureType, SimpleFeature> ret = new DefaultFeatureCollection(feats);
        ret.clear();
        boolean selected = false;

        Coordinate center = bbox.centre();

        double X1 = bbox.getMinX(); double Y1 = bbox.getMinY(); Coordinate coord1 = new Coordinate(X1, Y1);
        double X2 = bbox.getMinX(); double Y2 = bbox.getMaxY(); Coordinate coord2 = new Coordinate(X2, Y2);
        double X3 = bbox.getMaxX(); double Y3 = bbox.getMaxY(); Coordinate coord3 = new Coordinate(X3, Y3);
        double X4 = bbox.getMaxX(); double Y4 = bbox.getMinY(); Coordinate coord4 = new Coordinate(X4, Y4);

        if (this.Noth.isSelected()) {
            selected = true;
            ret.addAll(selectionLib.selectSide(feats, coord2, center, coord3));
        }
        if (this.East.isSelected()) {
            selected = true;
            ret.addAll(selectionLib.selectSide(feats, coord3, center, coord4));
        }
        if (this.South.isSelected()) {
            selected = true;
            ret.addAll(selectionLib.selectSide(feats, coord4, center, coord1));
        }
        if (this.West.isSelected()) {
            selected = true;
            ret.addAll(selectionLib.selectSide(feats, coord1, center, coord2));
        }
        if (selected && ret.isEmpty()) return null;

        if (ret.isEmpty()) return feats;
        else return ret;
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> checkRouteObjects (Coordinate from, Coordinate to) {
        FeatureCollection<SimpleFeatureType, SimpleFeature> ret = new DefaultFeatureCollection(this.selectedPolygons);
        ret.clear();

        MapLayer[] mapLayers = mapContext.getLayers();
        for (int i = 1; i < mapLayers.length; i++) {
            if (isSelected(mapLayers[i].getFeatureSource().getSchema().getName().getLocalPart())) {
                selectionLib.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[i].getFeatureSource());
                selectionLib.LAYER_NUMBER = i;
                FeatureCollection<SimpleFeatureType, SimpleFeature> ret3 = selectionLib.isIntersect(from, to);
                if (ret3.isEmpty()) return null;
                else ret.addAll(ret3);
            }
        }
        return ret;
    }

    public boolean isSelected(String name) {
        for (int i = 0; i < this.selectedLayers.size(); i++) {
            if (this.selectedLayers.get(i).compareTo(name) == 0)
                return true;
        }
        return false;
    }

    public boolean checkAreaWMsg (Coordinate coordinate, Double areaWidth, Double roadDistance, Double areaHeigth) {
        boolean ret = true;
        ReferencedEnvelope env = selectionLib.getAreaEnvelope(coordinate, areaWidth / 2);
        this.from = new DefaultFeatureCollection(this.selectedPolygons);
        this.from.clear();
        this.from.addAll(selectionLib.getRectFeatures(env));
        System.out.println("Pakilimo teritorijos poligonu skaicius: " + from.size());

        Double heigth = getAreaHeigth(from);
        if (areaHeigth.compareTo(heigth) >= 0) {
            if (isFreeArea(coordinate, areaWidth/2)) {
                this.roadsNearFromArea = isNearRoads(coordinate, roadDistance+areaWidth/2);
                if (!this.roadsNearFromArea.isEmpty()) {
                    // darom viena poligona
                    FeatureIterator<SimpleFeature> iter = this.from.features();

                    SimpleFeature feat = modifyFeature(iter.next(), coordinate, areaWidth);
                    this.from.clear();
                    this.from.add(feat);
                    
                } else {
                    this.roadsNearFromArea = null;
                    JOptionPane.showMessageDialog(null, "Pasirinktoje vietoveje, pasirinktu atstumu, keliu nera.");
                    ret = false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pasirinktoje vietoveje laisvos vietos nera (2).");
                ret = false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pasirinktoje vietoveje reljefo svyravimas " + heigth + " metrai.");
            ret = false;
        }
        return ret;
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getFeats (Coordinate coordinate, Double areaWidth) {
        MapLayer[] mapLayers = mapContext.getLayers();
        selectionLib.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[0].getFeatureSource());
        selectionLib.LAYER_NUMBER = 0;
        ReferencedEnvelope env = selectionLib.getAreaEnvelope(coordinate, areaWidth / 2);
        //FeatureCollection<SimpleFeatureType, SimpleFeature> to = new DefaultFeatureCollection(selectionLib.getRectFeatures(env));
        //return to;
        //return selectionLib.getToAreaPolygons(coordinate, areaWidth / 2);
        return selectionLib.getRectFeatures(env);
    }

    public boolean checkAreaNoMsg (Coordinate coordinate, Double areaWidth, Double roadDistance, Double areaHeigth) {
        boolean ret = true;
        ReferencedEnvelope env = selectionLib.getAreaEnvelope(coordinate, areaWidth / 2);
        //FeatureCollection<SimpleFeatureType, SimpleFeature> to = new DefaultFeatureCollection(selectionLib.getRectFeatures(env));
        FeatureCollection<SimpleFeatureType, SimpleFeature> to = getFeats(coordinate, areaWidth);
        //this.from = selectionLib.getRectFeatures(env);
        System.out.println("Nusileidimo teritorijos poligonu skaicius: " + to.size());
        Double heigth = getAreaHeigth(to);

        if (areaHeigth.compareTo(heigth) >= 0) {
            if (isFreeArea(coordinate, areaWidth/2)) {
                FeatureCollection<SimpleFeatureType, SimpleFeature> temp = isNearRoads(coordinate, roadDistance+areaWidth/2);
                if (!temp.isEmpty()) {
                    this.roadsNearToArea.addAll(temp);
                    return true;
                } else {
                    ret = false;
                }
            } else {
                ret = false;
            }
        } else {
            ret = false;
        }
        
        return ret;
    }


    public FeatureCollection<SimpleFeatureType, SimpleFeature> isNearRoads (Coordinate coordinate, Double roadDistance) {
        MapLayer[] mapLayers = mapContext.getLayers();
        selectionLib.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[1].getFeatureSource());
        selectionLib.LAYER_NUMBER = 1;
        FeatureCollection<SimpleFeatureType, SimpleFeature> isRoads = selectionLib.isRoads(coordinate, roadDistance);
        System.out.println("isRoads near " + roadDistance + " meters: " + isRoads);
        return isRoads;
    }

    public boolean isFreeArea (Coordinate coordinate, double width) {
        boolean free = true;
        MapLayer[] mapLayers = mapContext.getLayers();
        for (int i = 1; i < mapLayers.length; i++) {
            selectionLib.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[i].getFeatureSource());
            selectionLib.LAYER_NUMBER = i;
            boolean ret = selectionLib.isEmptyArea(coordinate, width);
            
            if (ret == false)
                free = false;
        }
        return free;
    }

    public Double getAreaHeigth(FeatureCollection<SimpleFeatureType, SimpleFeature> from) {
        Double heigth = selectionLib.calculateHeight(from);
        System.out.println("Pakilimo teritorijos reljefo svyravimas: " + heigth);
        return heigth;
    }


    private void EastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EastActionPerformed

    }//GEN-LAST:event_EastActionPerformed

    private void NothActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NothActionPerformed

    }//GEN-LAST:event_NothActionPerformed

    private void WestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WestActionPerformed
       
    }//GEN-LAST:event_WestActionPerformed

    private void SouthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SouthActionPerformed

    }//GEN-LAST:event_SouthActionPerformed

    private void ObjectsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ObjectsButtonActionPerformed
        MapLayer[] mapLayers = mapContext.getLayers();

        if ((this.objects != null) && (!this.objects.isEmpty())) {
            AttributesFrame attributesFrame = new AttributesFrame();
            for (int i = 1; i < mapLayers.length; i++) {
                attributesFrame.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[i].getFeatureSource());
                attributesFrame.show3(this.objects, mapLayers[i].getFeatureSource().getSchema().getName().getLocalPart());
            }
            attributesFrame.setSize(800, 600);
            attributesFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Nėra informacijos sluoksnio ir/arba nepažymėtas objektas", "Pranešimas", 3);
        }
        
    }//GEN-LAST:event_ObjectsButtonActionPerformed

    private void AreaAttributesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AreaAttributesButtonActionPerformed
        MapLayer[] mapLayers = mapContext.getLayers();
        
        if (((this.from != null) && (!this.from.isEmpty())) &&
                ((this.toArea != null) && (!this.toArea.isEmpty()))) {
            AttributesFrame attributesFrame = new AttributesFrame();
            attributesFrame.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[0].getFeatureSource());
            attributesFrame.show3(mapContext.getLayer(0).getFeatureSource(), this.from, "Pakilimo aikštelė");
            attributesFrame.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[0].getFeatureSource());
            System.out.println("toArea len: " + this.toArea.size());
            attributesFrame.show3(mapContext.getLayer(0).getFeatureSource(), this.toArea, "Nusileidimo aikštelės");

            attributesFrame.setSize(800, 600);
            attributesFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Nesudarytas maršrutas.", "Pranešimas", 3);
        }
    }//GEN-LAST:event_AreaAttributesButtonActionPerformed

    private void RoadsAttributesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RoadsAttributesButtonActionPerformed
        MapLayer[] mapLayers = mapContext.getLayers();
        
        if ((this.roadsNearFromArea != null && (!this.roadsNearFromArea.isEmpty()))
                && (this.roadsNearToArea != null) && (!this.roadsNearToArea.isEmpty())) {

            AttributesFrame attributesFrame = new AttributesFrame();
            attributesFrame.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[1].getFeatureSource());
            attributesFrame.show3(this.roadsNearFromArea, "Keliai šalia pakilimo aikštelės");
            attributesFrame.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[1].getFeatureSource());
            attributesFrame.show3(this.roadsNearToArea, "Keliai šalia nusileidimo aikštelės");

            attributesFrame.setSize(800, 600);
            attributesFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Nesudarytas maršrutas.", "Pranešimas", 3);
        }
    }//GEN-LAST:event_RoadsAttributesButtonActionPerformed

    private void ClearMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearMapButtonActionPerformed
        selectionLib.clearAll();
        MapLayer[] mapLayers = this.mapContext.getLayers();
        this.selectionLib.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[0].getFeatureSource());
        this.selectionLib.LAYER_NUMBER = 0;
        
        if ((this.selectedPolygons != null) && (!this.selectedPolygons.isEmpty())) {
            this.selectionLib.selectFeatures(this.selectedPolygons);
            System.out.println("selectedPolygons: "+ this.selectedPolygons.size());
        } else {
            JOptionPane.showMessageDialog(null, "Pažymėkite žemėlapyje pakilimo vietą", "Pranešimas", 3);
        }
        
        this.from = null;
        this.toArea = null;
        this.roadsNearFromArea = null;
        this.roadsNearToArea = null;
        this.objects = null;
        System.out.println("LAYERS COUNT before: " + this.mapContext.getLayerCount());
        for (int i = this.MAX; i < this.mapContext.getLayerCount(); i++) {
            this.mapContext.removeLayer(i);
        }
        System.out.println("LAYERS COUNT after: " + this.mapContext.getLayerCount());
        mapFrame.repaint();
    }//GEN-LAST:event_ClearMapButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AikstelesPlotis;
    private javax.swing.JButton AreaAttributesButton;
    private javax.swing.JTextField AtstumasIkiKelio;
    private javax.swing.JButton ClearMapButton;
    private javax.swing.JRadioButton East;
    private javax.swing.JButton Isvalyti;
    private javax.swing.JList LankytiniObjektai;
    private javax.swing.JRadioButton Noth;
    private javax.swing.JButton ObjectsButton;
    private javax.swing.JTextField ReljefoSvyravimas;
    private javax.swing.JButton RoadsAttributesButton;
    private javax.swing.JButton Rodyti;
    private javax.swing.JTextField SkrydzioAtstumas;
    private javax.swing.JRadioButton South;
    private javax.swing.JRadioButton West;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

}
