package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.graphics.Canvas;

public interface Renderer {
    public void draw(Canvas canvas, Vector2d<Float> offset);
    void setStyle(int id);
    void setZoomLevel(int zoomLevel);
}
