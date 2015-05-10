package pl.wroc.pwr.indoorlocalizationtieto.localization;

import java.util.ArrayList;

/**
 * Created by Mateusz on 2015-05-12.
 */
public class LocalizationData {
    ArrayList<AccessPoint> accessPoints = new ArrayList<>();
    ArrayList<ReferencePoint> referencePoints = new ArrayList<>();

    public ArrayList<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(ArrayList<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public ArrayList<ReferencePoint> getReferencePoints() {
        return referencePoints;
    }

    public void setReferencePoints(ArrayList<ReferencePoint> referencePoints) {
        this.referencePoints = referencePoints;
    }

    public void addAccessPoint(AccessPoint accessPoint) {
        this.accessPoints.add(accessPoint);
    }
    
    public void addReferencePoint(ReferencePoint referencePoint) {
        this.referencePoints.add(referencePoint);
    }
}
