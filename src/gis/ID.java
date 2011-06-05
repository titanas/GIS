package gis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.opengis.filter.identity.FeatureId;

public class ID {

    public Set<FeatureId> IDs = new HashSet<FeatureId>();

    public ID () {
    }

    public void printIDs () {
        Iterator<FeatureId> iter = IDs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + "; ");
        }
        System.out.println("size: " + IDs.size());
    }

    public void printCount() {
        System.out.println("ID count: " + IDs.size());
    }

    public void getBounds() {

    }
}
