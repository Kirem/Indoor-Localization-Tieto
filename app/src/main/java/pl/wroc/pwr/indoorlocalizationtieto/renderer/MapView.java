package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MapView extends View {
    Renderer renderer = null;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (renderer != null) {
            Log.i(getClass().getSimpleName(), "renderer not null");
            renderer.draw(canvas);
        }
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
        renderer.setContext(getContext());
    }

    public Renderer getRenderer() {
        return renderer;
    }
}
