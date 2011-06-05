package gis;

import java.util.Vector;

public class FDataStore {

    public Vector<DStore> stores = new Vector<DStore>();

    public FDataStore () {
    }

    public void addStore(DStore store) {
        this.stores.add(store);
    }
}
