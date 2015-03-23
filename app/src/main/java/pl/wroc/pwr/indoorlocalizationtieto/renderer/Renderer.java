package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.content.Context;
import android.graphics.Canvas;

public interface Renderer {
    public void draw(Canvas canvas);
    public void setContext(Context context);
}
