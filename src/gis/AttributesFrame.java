package gis;

import com.vividsolutions.jts.geom.Geometry;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.swing.table.FeatureCollectionTableModel;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.identity.FeatureId;

public class AttributesFrame extends JFrame {

    private JTabbedPane tabPane;
    private JTable table;
    private ID ids;
    private FeatureSource source;
    private FDataStore stores;
    private SelectionLib selectionLib;
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    public AttributesFrame (SelectionLib selectionLib, FDataStore stores) {
        
        this.selectionLib = selectionLib;
        this.ids = selectionLib.getID();
        this.stores = stores;

        this.tabPane = new JTabbedPane();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(this.tabPane);
        //JMenuBar downbar = new JMenuBar();
        //getContentPane().add(downbar, BorderLayout.SOUTH);
        pack();
    }

    public AttributesFrame () {

        //this.selectionLib = selectionLib;
        //this.ids = selectionLib.getID();
        //this.stores = stores;

        this.tabPane = new JTabbedPane();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(this.tabPane);
        JMenuBar downbar = new JMenuBar();
        getContentPane().add(downbar, BorderLayout.SOUTH);
        pack();
    }


    public void addShowSelected (JMenuBar toolbar) {
        JButton showSelectedButton = new JButton("Show selected");
        toolbar.add(showSelectedButton);
        showSelectedButton.setToolTipText("Show selected rows");
        toolbar.add(showSelectedButton);

        showSelectedButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e) {
                try {
                    if  (table.getSelectedRowCount() != 0) {
                        showSelected();
                    } else {
                        JOptionPane.showMessageDialog(null, "Pažymėkite lentelės eilutes");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void showSelected() throws IOException {
        QueryLib qLib = new QueryLib();

        //String typeName = (String) featureTypeCBox.getSelectedItem();
        //DStore dStore = findDataStore(typeName);

        int count = table.getSelectedRowCount();
        ArrayList<String> selectedID = new ArrayList<String>();
        if (count != 0) {
            int[] row = this.table.getSelectedRows();
            for (int i = 0; i < count; i++) {
                selectedID.add(this.table.getValueAt(row[i], 0).toString());
            }
            
            
            Vector<SimpleFeature> featuresVector = qLib.makeSelectedFeatureVector(selectedID, source);
            
            this.selectionLib.setGeometry(source);
            //this.selectionLib.style = dStore.style;
            this.selectionLib.selectFeatures(featuresVector);
        }
    }

    public void show2() {
        Filter filter = ff.id(ids.IDs);
        FeatureCollection features;
        try {
            features = source.getFeatures(filter);
            if (!features.isEmpty()) {

                JPanel jp1 = new JPanel();
                this.tabPane.addTab(source.getName().getLocalPart(), jp1);
                this.table = new JTable();
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.setModel(new DefaultTableModel(5, 5));
                table.setPreferredScrollableViewportSize(new Dimension(800, 600));
                JScrollPane scrollPane = new JScrollPane(table);
                jp1.add(scrollPane);

                FeatureCollectionTableModel model = new FeatureCollectionTableModel(features);
                table.setModel(model);
                pack();
                setSize(800, 600);
                setVisible(true);
            } 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Filter makeFilterFromGeom (FeatureCollection<SimpleFeatureType, SimpleFeature> feats) {
        FeatureIterator<SimpleFeature> iter = feats.features();
        
        SimpleFeature f1 = iter.next();
        Filter filter = ff.intersects(ff.property("the_geom"), ff.literal((Geometry) f1.getDefaultGeometry()));
        while(iter.hasNext()) {
            SimpleFeature f = iter.next();
            Filter temp = ff.intersects(ff.property("the_geom"), ff.literal((Geometry) f.getDefaultGeometry()));
            filter = ff.or(filter, temp);
        }
        return filter;
    }

    public void show3 (FeatureSource fc, FeatureCollection<SimpleFeatureType, SimpleFeature> feats, String title) {
        //Filter filter = ff.id(convertToSet(feats));
        Filter filter = makeFilterFromGeom(feats);
        FeatureCollection features;
        try {
            features = fc.getFeatures(filter);
            if (!features.isEmpty()) {

                JPanel jp1 = new JPanel();
                this.tabPane.addTab(title, jp1);
                this.table = new JTable();
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.setModel(new DefaultTableModel(5, 5));
                table.setPreferredScrollableViewportSize(new Dimension(800, 600));
                JScrollPane scrollPane = new JScrollPane(table);
                jp1.add(scrollPane);

                /********************DOWN TOOLBAR*******************/
                //JMenuBar downbar = new JMenuBar();
                //getContentPane().add(downbar, BorderLayout.SOUTH);
                //addShowSelectedExtend(downbar);

                FeatureCollectionTableModel model = new FeatureCollectionTableModel(features);
                table.setModel(model);
                pack();
                setSize(800, 600);
                setVisible(true);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void show3 (FeatureCollection<SimpleFeatureType, SimpleFeature> feats, String title) {
        Filter filter = ff.id(convertToSet(feats));
        FeatureCollection features;
        try {
            features = source.getFeatures(filter);
            if (!features.isEmpty()) {

                JPanel jp1 = new JPanel();
                this.tabPane.addTab(title, jp1);
                this.table = new JTable();
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.setModel(new DefaultTableModel(5, 5));
                table.setPreferredScrollableViewportSize(new Dimension(800, 600));
                JScrollPane scrollPane = new JScrollPane(table);
                jp1.add(scrollPane);

                /********************DOWN TOOLBAR*******************/
                //JMenuBar downbar = new JMenuBar();
                //getContentPane().add(downbar, BorderLayout.SOUTH);
                //addShowSelectedExtend(downbar);

                FeatureCollectionTableModel model = new FeatureCollectionTableModel(features);
                table.setModel(model);
                pack();
                setSize(800, 600);
                setVisible(true);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setFeatureSource (FeatureSource source) {
        this.source = source;
    }

    public Set<FeatureId> convertToSet (FeatureCollection<SimpleFeatureType, SimpleFeature> feats) {
        Set<FeatureId> ret = new HashSet();
        FeatureIterator<SimpleFeature> iter = feats.features();
        while (iter.hasNext()) {
            ret.add(iter.next().getIdentifier());
        }
        return ret;
    }

}
