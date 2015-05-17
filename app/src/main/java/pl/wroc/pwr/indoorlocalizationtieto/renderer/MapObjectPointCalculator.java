package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.graphics.PointF;
import android.util.Log;

public class MapObjectPointCalculator {
    private static final double KM_PER_DEGREE_LAT = 110.574;
    private static final double KM_PER_DEGREE_LONG = 111.320;//*cos(lat)km
    private static final double EARTH_CIRCUIT = 40075014;//m


    private final float latitude;
    private final float longitude;
    //radius in meters
    private final float radius;
    double centerX;
    double centerY;
    private double mapHeight;
    private double mapWidth;
    private double scale;

    public MapObjectPointCalculator(float latitude, float longitude, int vHeight, int vWidth, float radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius * 5;
        setScale();
        this.mapHeight = vHeight * scale;
        this.mapWidth = vWidth * scale;
        centerX = ((longitude + 180) * (mapWidth / 360));
        double latRad = (latitude * Math.PI / 180);

        double mercN = Math.log(Math.tan((Math.PI / 4) + (latRad / 2)));
        centerY = ((mapHeight / 2) - (mapWidth * mercN / (2 * Math.PI)));
        Log.i("POSITION_TAG", "position x = " + centerX + " position y = " + centerY);
        Log.i("POSITION_TAG", "scale:  " + scale);
        Log.i("POSITION_TAG", "radius:  " + radius);


    }

    private void setScale() {
        this.scale = EARTH_CIRCUIT / radius;
    }

    public PointF calibrate(double latDis, double lonDis) {
//        Log.i("POSITION", "user x = " + latDis + " user y = " + lonDis);

        PointF position = new PointF(0.0f, 0.0f);
        double x;
        double y;
        x = ((lonDis + 180) * (mapWidth / 360));
        double latRad = (float) (latDis * Math.PI / 180);

        double mercN = Math.log(Math.tan((Math.PI / 4) + (latRad / 2)));
        y = ((mapHeight / 2) - (mapWidth * mercN / (2 * Math.PI)));
        x -= centerX;
        y -= centerY;

        position.x = (float) x;
        position.y = (float) y;
        return position;
    }

}
