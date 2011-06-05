package gis;

import java.io.IOException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.geotools.map.MapLayer;
import org.jfree.ui.ExtensionFileFilter;
import org.geotools.swing.JMapPane;

public class MapViewer {

    public int MAX = 0;
    public JMapFrame mapFrame;
    public MapContext mapContext;
    public JToolBar toolBar;
    public JMapPane mapPane;
    public FDataStore stores = new FDataStore();
    public SelectionLib selectionLib;
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private String surfacePath = "C:\\GIS\\Duomenys\\pavirs_lt_p.shp";
    private String roadsPath = "C:\\GIS\\Duomenys\\keliai.shp";

    public MapViewer () {
        mapContext = new DefaultMapContext();
        mapFrame = new JMapFrame(mapContext);
        mapFrame.enableLayerTable( true );
        mapFrame.enableToolBar( true );
        mapFrame.enableStatusBar( true );
        mapFrame.enableInputMethods( true );
        mapFrame.initComponents();
        toolBar = mapFrame.getToolBar();
        mapPane = mapFrame.getMapPane();
        mapContext.setTitle("GIS");
        selectionLib = new SelectionLib(mapFrame, this);
    }

    public void show () {
        updateGUI();
        addAddButton();
        addSelectButton();
        addClearButton();
        addZoomToExtentButton();
        addShowAttributesButton();
        addSearchButton();
        addRouteButton();

        addDefaultLayer(this.surfacePath, 0);
        MAX++;
        addDefaultLayer(this.roadsPath, 1);
        MAX++;

        mapFrame.setTitle("GIS");

        //Image image = mapFrame.getIconImage(); image.getSource(); ImageConsumer a = new ImageConsumer();
        mapFrame.setSize(800, 600);
        mapFrame.setVisible(true);
    }

    public void updateGUI() {
        removeSeparators();
        for (int i = 0; i < toolBar.getComponentCount(); i++) {        
            JButton button = (JButton)toolBar.getComponent(i);
            switch (i) {

                case 0 : {
                    button.setIcon(new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\Zoom in.png"));
                    break;
                }

                case 1 : {
                    button.setIcon(new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\Zoom out.png"));
                    break;
                }

                case 2 : {
                    button.setIcon(new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\Move.png"));
                    break;
                }

                case 3 : {
                    button.setIcon(new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\cursor.png"));
                    break;
                }

                case 4 : {
                    button.setIcon(new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\zoom-extend-icon.png"));
                    break;
                }
            }
        }
    }

    public void removeSeparators() {
        for (int i = 0; i < toolBar.getComponentCount(); i++) {
            JButton button;
            try {
                button = (JButton)toolBar.getComponent(i);
            } catch (ClassCastException e) {
                toolBar.remove(i);
            }
        }
    }

    public void addAddButton () {
        JButton addButton =new JButton( new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\Add.png"));
        addButton.setToolTipText("Add new layer");
        toolBar.add(addButton);
        addButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser("C:\\GIS\\Duomenys\\");
                FileFilter fileFilter = (FileFilter) new ExtensionFileFilter("Shape files", ".shp");
                chooser.addChoosableFileFilter(fileFilter);
                
                chooser.setMultiSelectionEnabled(true);                
                int status = chooser.showOpenDialog(mapFrame);
                if (status == JFileChooser.APPROVE_OPTION) {
                    File[] files = chooser.getSelectedFiles();
                    try {
                        for (int i = 0; i < files.length; i++) {      
                            DStore dStore = new DStore();
                            dStore.setFileDataStore(FileDataStoreFinder.getDataStore(files[i]));                           
                            StyleLib styleLib = new StyleLib(); 
                            Style style = styleLib.createStyle2(dStore);
                            mapFrame.repaint();
                            dStore.setStyle(style);
                            dStore.setNumber(i);
                            mapContext.addLayer(dStore.fileDataStore.getFeatureSource(), dStore.style);
                            stores.addStore(dStore);
                            MAX++;
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void addDefaultLayer(String path, int number) {
        DStore dStore = new DStore();
        try {
            dStore.setFileDataStore(FileDataStoreFinder.getDataStore(new File(path)));
        } catch (IOException ex) {
            Logger.getLogger(MapViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        StyleLib styleLib = new StyleLib();
        Style style = styleLib.createStyle2(dStore);
        mapFrame.repaint();
        dStore.setStyle(style);
        dStore.setNumber(number);
        try {
            mapContext.addLayer(dStore.fileDataStore.getFeatureSource(), dStore.style);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        stores.addStore(dStore);
    }

    public void addZoomToExtentButton() {
        JButton addButton =new JButton( new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\extend.png"));
        addButton.setToolTipText("Zoom to selected objects");
        toolBar.add(addButton);
        addButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("x: " + mapPane.getBounds().x + " y: " + mapPane.getBounds().y);
                MapLayer[] mapLayers = mapContext.getLayers();
                selectionLib.setExtendEnvelope(null);
                if (mapLayers.length != 0) {
                    for (int i = 0; i < mapLayers.length; i++) {
                    selectionLib.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[i].getFeatureSource());
                    selectionLib.LAYER_NUMBER = i;
                    selectionLib.collectEnvelopes();
                }          
                selectionLib.zoomToFeature();
            }
        }
        });
    }

    public void addSearchButton () {
        JButton addButton =new JButton( new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\Find.png"));
        addButton.setToolTipText("Search by attributes");
        toolBar.add(addButton);
        addButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QueryLib queryLib = new QueryLib(mapFrame, selectionLib, stores);
                try {
                    if (!stores.stores.isEmpty()) {
                        queryLib.filterFeatures();
                        queryLib.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nėra informacijos sluoksnių");
                    }                
                } catch (Exception ex) {
                    ex.printStackTrace();
                }  
            }
        });
    }

    public void addClearButton () {
        JButton clearButton = new JButton(new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\Clear.png"));
        clearButton.setToolTipText("Clear all selected items");    
        toolBar.add(clearButton);
        clearButton.addActionListener( new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                selectionLib.clearAll();
                mapFrame.repaint();
            }
        });
    }

    public void addShowAttributesButton() {
        JButton showAttributesButton = new JButton(new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\attributes.png"));
        showAttributesButton.setToolTipText("Show attributes of selected objects");
        toolBar.add(showAttributesButton);
        showAttributesButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                MapLayer[] mapLayers = mapContext.getLayers();
                if ((mapLayers.length != 0) && (selectionLib.getID().IDs.size() != 0)) {
                    AttributesFrame attributesFrame = new AttributesFrame(selectionLib, stores);
                    
                    for (int i = 0; i < mapLayers.length; i++){
                        attributesFrame.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[i].getFeatureSource());
                        attributesFrame.show2();
                    }
                    attributesFrame.setSize(800, 600);
                    attributesFrame.setVisible(true);   
                } else {
                    JOptionPane.showMessageDialog(null, "Nėra informacijos sluoksnio ir/arba nepažymėtas objektas");
                }
            }
        });
    };

    public void addSelectButton() {
        JButton selectButton = new JButton(new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\cursor2.png"));
        selectButton.setToolTipText("Select the layers");
        toolBar.add(selectButton);
        selectButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mapFrame.getMapPane().setCursorTool(
                    new CursorTool() {

                        private java.awt.Point startPos;
                        private java.awt.Point endPos;
                        private Rectangle rectangle;    

                        @Override
                        public void onMouseClicked(MapMouseEvent ev) {
                            java.awt.Point screenPos = ev.getPoint();
                            Rectangle rectangle2 = new Rectangle(screenPos.x-1, screenPos.y-1, 2, 2);
                            showSelected(rectangle2);
                        }

                        @Override
                        public void onMousePressed(MapMouseEvent ev) {
                            this.startPos =  ev.getPoint();
                        }
                       
                        @Override
                        public void onMouseDragged(MapMouseEvent ev) {

                            this.endPos = ev.getPoint();
                            this.rectangle = new Rectangle (startPos);
                            this.rectangle.add(endPos);
                            
                            System.out.println("x: " + this.rectangle.x + " width: " + this.rectangle.width);
                        }

                        public void onMouseReleased(MapMouseEvent ev) {                           
                            if (this.rectangle != null) {
                                showSelected(this.rectangle);
                                this.rectangle = null;
                            }                           
                        }

                        public void showSelected (Rectangle rect) {
                            MapLayer[] mapLayers = mapContext.getLayers();
                            for (int i = 0; i < mapLayers.length; i++) {
                                System.out.println("layer numer : " + (i + 1));
                                selectionLib.style = mapLayers[i].getStyle();
                                selectionLib.setFeatureSource((FeatureSource<SimpleFeatureType, SimpleFeature>) mapLayers[i].getFeatureSource());
                                selectionLib.LAYER_NUMBER = i;
                                selectionLib.selectFeatures(selectionLib.selectFeatures(rect));
                            }
                        }
                        
                        @Override
                        public boolean drawDragBox() {
                            return true;
                        }
                    });
                }
        });
    }

    public List createModel() {
        List model = new ArrayList <String>();
        MapLayer[] layers = mapContext.getLayers();
        System.out.println("size: "+layers.length);
        for (int i = 1; i < MAX; i++) {
            System.out.println(layers[i].getFeatureSource().getSchema().getName().getLocalPart());
            model.add(layers[i].getFeatureSource().getSchema().getName().getLocalPart());
        }
        return model;
    }

    public void addRouteButton() {
        
        JButton routeButton = new JButton(new ImageIcon("C:\\Users\\Lukas\\Documents\\NetBeansProjects\\GIS\\img\\route.png"));
        routeButton.setToolTipText("Make air baloon route");
        toolBar.add(routeButton);
        routeButton.addActionListener( new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (selectionLib.getID().IDs.size() == 0) {
                    JOptionPane.showMessageDialog(null, "Nurodykite pradinę oro baliono pakilimo vietą");
                } else {
                    RouteParamFrame route = new RouteParamFrame(selectionLib, mapContext, createModel(), mapFrame, MAX);
                    route.setVisible(true);
                    route.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }
        });
    }
}