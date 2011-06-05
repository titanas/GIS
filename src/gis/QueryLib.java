package gis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.swing.table.FeatureCollectionTableModel;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.ArrayList;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.map.MapLayer;
import org.geotools.swing.JMapFrame;
import org.opengis.filter.FilterFactory2;

@SuppressWarnings("serial")

public class QueryLib extends JFrame {

    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    //private DataStore store;
    private FDataStore dataStore;
    private JComboBox featureTypeCBox;
    private JTable table;
    private JTextField text;
    private SelectionLib selectionLib;
    private JMapFrame mapFrame;

    public QueryLib(){
    }

    public QueryLib (JMapFrame mapFrame, SelectionLib selectionLib, FDataStore store) {
        this.mapFrame = mapFrame;
        this.dataStore = store;
        this.selectionLib = selectionLib;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        text = new JTextField(80);
        text.setText("include");
        getContentPane().add(text, BorderLayout.NORTH);

        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setModel(new DefaultTableModel(5, 5));
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JLabel label = new JLabel("Choose data source: ");
        menubar.add(label);
        featureTypeCBox = new JComboBox();
        menubar.add(featureTypeCBox);
        addGetDataButton(menubar);

        /********************DOWN TOOLBAR*******************/
        JMenuBar downbar = new JMenuBar();
        getContentPane().add(downbar, BorderLayout.SOUTH);

        addShowToMapButton(downbar);
        addShowSelected(downbar);
        addShowSelectedExtend(downbar);
        addCountButton(downbar);
        addCenterButton(downbar);
        addGeometryButton(downbar);

        pack();
        try {
            updateUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public void addGetDataButton (JMenuBar toolbar) {
        JButton getDataButton = new JButton("Update Table");
        toolbar.add(getDataButton);
        getDataButton.setToolTipText("Show data from selected data source");
        toolbar.add(getDataButton);
        
        getDataButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                try {
                    filterFeatures();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void addShowToMapButton (JMenuBar toolbar) {
        JButton showToMapButton = new JButton("Show table data");
        toolbar.add(showToMapButton);
        showToMapButton.setToolTipText("Show selected data to the map");
        toolbar.add(showToMapButton);

        showToMapButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                try {
                    showAllIDs();
                    //updateUI();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }
        });
    }
   
    public void addShowSelectedExtend (JMenuBar toolbar) {
        JButton showSelectedExtendButton = new JButton("Show selected EXTEND");
        toolbar.add(showSelectedExtendButton);
        showSelectedExtendButton.setToolTipText("Show selected rows");
        toolbar.add(showSelectedExtendButton);

        showSelectedExtendButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                try {
                    if  (table.getSelectedRowCount() != 0) {
                        Vector<SimpleFeature> vector = makeSelectedFeaturesVector();
                        showSelected(vector);
                    } else {
                        JOptionPane.showMessageDialog(null, "Pažymėkite lentelės eilutes");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void addShowSelected (JMenuBar toolbar) {
        JButton showSelectedButton = new JButton("Show ONLY selected");
        toolbar.add(showSelectedButton);
        showSelectedButton.setToolTipText("Show selected rows");
        toolbar.add(showSelectedButton);

        showSelectedButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                try {
                    if  (table.getSelectedRowCount() != 0) {
                        Vector<SimpleFeature> vector = makeSelectedFeaturesVector();
                        showCurrentSelected(vector);
                    } else {
                        JOptionPane.showMessageDialog(null, "Pažymėkite lentelės eilutes");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void addCountButton (JMenuBar toolbar) {
        JButton showCountButton = new JButton("Count");
        toolbar.add(showCountButton);
        showCountButton.setToolTipText("Count selected features from the table");
        toolbar.add(showCountButton);

        showCountButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                try {
                    countFeatures();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void addCenterButton (JMenuBar toolbar) {
        JButton centerButton = new JButton("Center");
        toolbar.add(centerButton);
        centerButton.setToolTipText("Calculate center of the selected objects");
        toolbar.add(centerButton);

        centerButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                try {
                    centerFeatures();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    public void addGeometryButton (JMenuBar toolbar) {
        JButton geometryButton = new JButton("Geometry");
        toolbar.add(geometryButton);
        geometryButton.setToolTipText("Show geometry");
        toolbar.add(geometryButton);

        geometryButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                try {
                    queryFeatures();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void showAllIDs () throws Exception {
        String typeName = (String) featureTypeCBox.getSelectedItem();
        System.out.println("typeName: " + typeName);
        DStore dStore = findDataStore(typeName);
        DataStore dataStore = (DataStore) dStore.fileDataStore;
        FeatureSource featureSource = dataStore.getFeatureSource(typeName);
        FeatureCollection features = featureSource.getFeatures(makeFilterFromText());
        FeatureIterator<SimpleFeature> iter = features.features();
        ID ids = new ID();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = iter.next();
                ids.IDs.add(feature.getIdentifier());
            }
        } finally {
            iter.close();
        }
        this.selectionLib.getID().IDs.clear();
        this.selectionLib.setID(ids);
        this.selectionLib.setGeometry(featureSource);
        this.selectionLib.style = dStore.style;
        this.selectionLib.LAYER_NUMBER = dStore.number;
        this.selectionLib.displaySelectedFeatures();            //ids.IDs
    }

    private Vector<SimpleFeature> makeSelectedFeaturesVector () throws Exception {
        int count = this.table.getSelectedRowCount();
        Vector<SimpleFeature> featuresVector = null;
        String typeName = (String) featureTypeCBox.getSelectedItem();
        DStore dStore = findDataStore(typeName);
        FeatureSource source =  dStore.fileDataStore.getFeatureSource(typeName);


        System.out.println("count: " + count);
        ArrayList<String> selectedID = new ArrayList<String>();
        if (count != 0) {
            int[] row = this.table.getSelectedRows();

            for (int i = 0; i < count; i++) {
                selectedID.add(this.table.getValueAt(row[i], 0).toString());
                //System.out.println("Selected ID: " + this.table.getValueAt(row[i], 0) + " ");
            }
            featuresVector = makeSelectedFeatureVector(selectedID, source);
            this.selectionLib.setGeometry(source);
            MapLayer[] layers = this.mapFrame.getMapContext().getLayers();
            int find = 0;
            //System.out.println("layers.length "+  layers.length);
            for (int i = 0; i < layers.length; i++) {
                System.out.println(layers[i].getFeatureSource().toString() + " " + dStore.fileDataStore.getFeatureSource().toString());
                if (layers[i].getFeatureSource().equals(dStore.fileDataStore.getFeatureSource())) {
                    find = i;
                }
            }
            //System.out.println("FIND " + find);
            selectionLib.LAYER_NUMBER = dStore.number;
            System.out.println("LAYER NUMBER : " + selectionLib.LAYER_NUMBER);
            selectionLib.style = dStore.style;
        }
        return featuresVector;
    }

    public void showCurrentSelected (Vector<SimpleFeature> featuresVector) {
        selectionLib.selectCurrentFeatures(featuresVector);
    }

    public void showSelected (Vector<SimpleFeature> featuresVector) {
        selectionLib.selectFeatures(featuresVector);
    }

    public Vector<SimpleFeature> makeSelectedFeatureVector(ArrayList<String> list, FeatureSource source) throws IOException {
        FeatureCollection<SimpleFeatureType, SimpleFeature> selFeat = source.getFeatures();
        SelectedFeaturesCollection sfc = new SelectedFeaturesCollection();
        

        FeatureIterator<SimpleFeature> iter = selFeat.features();
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            String featID = feature.getID();

            for(int i = 0; i < list.size(); i++) {
                if (list.get(i).compareTo(featID) == 0) {
                    sfc.features.add(feature);          
                }
            }
        }
        //System.out.println("sfc.features.size() " + sfc.features.size());
        return sfc.features;
    }

    private void updateUI() throws Exception {      
        Vector<String> types = makeDataStoreTypesVector(this.dataStore); 
        ComboBoxModel cbm = new DefaultComboBoxModel(types);
        //System.out.println("stores len: " + dataStore.stores.size());
        featureTypeCBox.setModel(cbm);
        table.setModel(new DefaultTableModel(5, 5));
    } 

    private Vector<String> makeDataStoreTypesVector(FDataStore store) {
        Vector<String> storeTypes = new Vector<String>();
        for (int i = 0; i < store.stores.size(); i++) {
            try {
                String[] names = store.stores.get(i).fileDataStore.getTypeNames();
                for (int j = 0; j < names.length; j++) {
                    storeTypes.add(names[j]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storeTypes;
    }

    @SuppressWarnings("unchecked")
    public void filterFeatures() throws Exception {
        

        String typeName = (String) featureTypeCBox.getSelectedItem();
        DataStore dataStore = (DataStore) findDataStore(typeName).fileDataStore;
        FeatureSource source = dataStore.getFeatureSource(typeName);

        FeatureType schema = source.getSchema();
        String name = schema.getGeometryDescriptor().getLocalName();
        System.out.println("schema.getGeometryDescriptor().getLocalName() " + name);
        System.out.println("schema.getName().getLocalPart() " + schema.getName().getLocalPart());


        FeatureCollection features = source.getFeatures(makeFilterFromText());
        FeatureCollectionTableModel model = new FeatureCollectionTableModel(features);
        table.setModel(model);
    }

    public Filter makeFilterFromText() {

        Filter filter = null;
        try {
            filter = CQL.toFilter(text.getText());
        } catch (CQLException e) {
            try {
                filter = CQL.toFilter("include");
                JOptionPane.showMessageDialog(null, "Neteisingai įvesta užklausa.");
            } catch (CQLException ex) {
                ex.printStackTrace();
            }
        }
        return filter;
    }

    public Filter makeFilterFromTextNoMessage() {
        Filter filter = null;
        try {
            filter = CQL.toFilter(text.getText());
        } catch (CQLException e) {
            try {
                filter = CQL.toFilter("include");
                //JOptionPane.showMessageDialog(null, "Neteisingai įvesta užklausa.");
            } catch (CQLException ex) {
                ex.printStackTrace();
            }
        }
        return filter;
    }
    
    private DStore findDataStore(String typeName) {
        DStore store;
        for(int i = 0; i < dataStore.stores.size(); i++) {
            try {
                String[] names = dataStore.stores.get(i).fileDataStore.getTypeNames();
                for (int j = 0; j < names.length; j++) {
                    if (names[j].equals(typeName)) {
                        return dataStore.stores.get(i);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // docs start countFeatures
    private void countFeatures() throws Exception {
        String typeName = (String) featureTypeCBox.getSelectedItem();
        DataStore dataStore = (DataStore) findDataStore(typeName).fileDataStore;
        FeatureSource source = dataStore.getFeatureSource(typeName);

        FeatureCollection features = source.getFeatures(makeFilterFromTextNoMessage());
        int rowsCount = this.table.getSelectedRowCount();
        int count = features.size();
        JOptionPane.showMessageDialog(text, "Number of selected features is " + rowsCount + " of " + count + ".");
    }
    // docs end countFeatures

    @SuppressWarnings("unchecked")
    // docs start centerFeatures
    private void centerFeatures() throws Exception {
        String typeName = (String) featureTypeCBox.getSelectedItem();
        DataStore dataStore = (DataStore) findDataStore(typeName).fileDataStore;
        FeatureSource source = dataStore.getFeatureSource(typeName);
    
        FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures(makeFilterFromTextNoMessage());
        double totalX = 0.0;
        double totalY = 0.0;
        long count = 0;
        FeatureIterator<SimpleFeature> iterator = features.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                Point centroid = geom.getCentroid();
                totalX += centroid.getX();
                totalY += centroid.getY();
                count++;
            }
        } finally {
            iterator.close(); // IMPORTANT
        }
        double averageX = totalX / (double) count;
        double averageY = totalY / (double) count;
        Coordinate center = new Coordinate(averageX, averageY);

        JOptionPane.showMessageDialog(text, "Center of selected features:" + center);
    }

    public Coordinate centerFeatures(FeatureCollection<SimpleFeatureType, SimpleFeature> features) {
        double totalX = 0.0;
        double totalY = 0.0;
        long count = 0;
        FeatureIterator<SimpleFeature> iterator = features.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                Point centroid = geom.getCentroid();
                totalX += centroid.getX();
                totalY += centroid.getY();
                count++;
            }
        } finally {
            iterator.close(); // IMPORTANT
        }
        double averageX = totalX / (double) count;
        double averageY = totalY / (double) count;
        Coordinate center = new Coordinate(averageX, averageY);
        return center;

    }

 
    @SuppressWarnings("unchecked")
    private void queryFeatures() throws Exception {
        String typeName = (String) featureTypeCBox.getSelectedItem();
        DataStore dataStore = (DataStore) findDataStore(typeName).fileDataStore;
        FeatureSource source = dataStore.getFeatureSource(typeName);

        FeatureType schema = source.getSchema();
        String name = schema.getGeometryDescriptor().getLocalName();

        System.out.println("schema.getGeometryDescriptor().getLocalName() " + name);
        System.out.println("schema.getName().getLocalPart() " + schema.getName().getLocalPart());

        DefaultQuery query = new DefaultQuery(schema.getName().getLocalPart(), makeFilterFromTextNoMessage(),
                new String[] { name });
        //System.out.println("query.getMaxFeatures() " + query.getMaxFeatures());
        
        FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures(query);
        //System.out.println("features.size() "+features.size());
        FeatureCollectionTableModel model = new FeatureCollectionTableModel(features);
        //System.out.println("model.getColumnCount() "+ model.getColumnCount());
        table.setModel(model);
    }

    public void printVector (Vector<?> vector) {
        for (int i = 0; i < vector.size(); i++) {
            System.out.println(vector.get(i).toString());
        }
    }
    public void printArrayList (ArrayList<String> a) {
        for (int i = 0; i < a.size(); i++) {
            System.out.println(a.get(i).toString());
        }
    }
}