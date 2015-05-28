package pl.wroc.pwr.indoorlocalizationtieto.localization;

import java.util.ArrayList;

/**
 * Class for keeping data about access points and reference points
 * Created by Mateusz on 2015-05-12.
 */
public class LocalizationData {
    ArrayList<AccessPoint> accessPoints = new ArrayList<>();
    ArrayList<ReferencePoint> referencePoints = new ArrayList<>();

    protected ArrayList<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    protected void setAccessPoints(ArrayList<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    protected ArrayList<ReferencePoint> getReferencePoints() {
        return referencePoints;
    }

    protected void setReferencePoints(ArrayList<ReferencePoint> referencePoints) {
        this.referencePoints = referencePoints;
    }

    protected void addAccessPoint(AccessPoint accessPoint) {
        this.accessPoints.add(accessPoint);
    }

    protected void addReferencePoint(ReferencePoint referencePoint) {
        this.referencePoints.add(referencePoint);
    }
}
