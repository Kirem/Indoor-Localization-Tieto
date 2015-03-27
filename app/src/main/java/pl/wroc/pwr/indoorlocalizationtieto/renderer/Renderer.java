package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.graphics.Canvas;

public interface Renderer {
    public void draw(Canvas canvas);
    void setStyle(int id);
}
