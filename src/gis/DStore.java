package gis;

import java.awt.Color;
import org.geotools.data.FileDataStore;
import org.geotools.styling.Style;

public class DStore {

    public FileDataStore fileDataStore;
    public Style style;
    public Color line;
    public Color fill;
    public int number;


    public DStore () {
    }

    public void setFileDataStore(FileDataStore store) {
        this.fileDataStore = store;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public void setLineColor(Color line) {
        this.line = line;
    }

    public void setFillColor(Color fill) {
        this.fill = fill;
    }

    public void setNumber(int num) {
        this.number = num;
    }
}
