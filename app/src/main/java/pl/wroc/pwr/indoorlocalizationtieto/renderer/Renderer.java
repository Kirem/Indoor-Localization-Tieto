package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.graphics.Canvas;
import android.graphics.PointF;

public interface Renderer {
    void draw(Canvas canvas, PointF offset);

    void setStyle(int id);

    void setZoomLevel(float zoomLevel);
}
