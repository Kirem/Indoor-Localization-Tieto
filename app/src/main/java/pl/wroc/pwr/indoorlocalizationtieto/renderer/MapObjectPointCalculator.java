package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.graphics.PointF;
import android.util.Log;

public class MapObjectPointCalculator {
    private static final double KM_PER_DEGREE_LAT = 110.574;
    private static final double KM_PER_DEGREE_LONG = 111.320;//*cos(lat)km

    private final float latitude;
    private final float longitude;
    private float viewHeight;
    //radius in meters
    private final float radius;
    private float degreesPerPixelHorizontal;
    private float degreesPerPixelVertical;
    private float sourceHeight;

    public MapObjectPointCalculator(float latitude, float longitude, int vHeight, float radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.viewHeight = vHeight;
        this.radius = radius;
        Log.i("Calibrate", "radius: " + radius*0.001);

        double top = latitude + (radius * 0.001) / (KM_PER_DEGREE_LAT);
        Log.i("Calibrate", "top: " + top);

            double bottom = latitude - (radius * 0.001) / (KM_PER_DEGREE_LAT);
        Log.i("Calibrate", "bottom: " + bottom);
        double left = longitude - (radius * 0.001) / (KM_PER_DEGREE_LONG * Math.cos(latitude));
        Log.i("Calibrate", "left: " + left);
        double right = longitude + (radius * 0.001) / (KM_PER_DEGREE_LONG * Math.cos(latitude));
        Log.i("Calibrate", "rigt: " + right);
        setScale(top, bottom, left, right);
    }

    private void setScale(double top, double bottom, double left, double right) {
        degreesPerPixelVertical = (float) (viewHeight / (top - bottom));
        Log.i("Calibrate", "scale vertical: " + degreesPerPixelVertical);

        degreesPerPixelHorizontal = viewHeight / ((float) (right - left));
        Log.i("Calibrate", "scale horizontal: " + degreesPerPixelHorizontal);
    }

    public PointF calibratePoint(PointF realPosition) {

        return calibrate(realPosition.x, realPosition.y);
    }

    private PointF calibrate(float latDis, float lonDis) {
        PointF position = new PointF(0.0f, 0.0f);
        position.x = latDis * degreesPerPixelHorizontal;
        position.y = lonDis * degreesPerPixelVertical;
        return position;
    }

    public double calibrateX(double latDis) {
//        Log.i("Calibrate", "Old x = " + latDis + " new x = " + (latitude - latDis) * degreesPerPixelVertical);
        return (latDis - latitude) * degreesPerPixelHorizontal;
    }

    public double calibrateY(double longDis) {
//        Log.i("Calibrate", "Old y = " + longDis + " new y = " + (longitude - longDis) * degreesPerPixelVertical);
        return (longDis - longitude) * degreesPerPixelVertical;
    }


}
