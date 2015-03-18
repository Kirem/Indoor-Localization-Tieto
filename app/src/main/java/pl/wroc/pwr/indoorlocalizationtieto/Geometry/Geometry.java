package pl.wroc.pwr.indoorlocalizationtieto.Geometry;

/**
 * Created by Mateusz on 2015-03-18.
 * TODO - add checking if values passed to constructors are valid
 * TODO - change calculateLength() method to calculate using latitude and longitude instead of cartesian coord. system
 * TODO - update test validating above
 */
abstract class Geometry {

    public abstract double calculateLength();
    //public abstract boolean isEqual
}