package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

import pl.wroc.pwr.indoorlocalizationtieto.R;

public class MapView extends View {
    public static final String MAP_VIEW_TAG = "MAP_VIEW_TAG";
    public static final int MAX_ZOOM_LEVEL = 3;
    private Renderer renderer = null;
    private float zoomLevel = 1;
    private PointF offset;
    private PointF viewSize;
    private PointF mapSize;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnTouchListener(new MapMoveListener());
        offset = new PointF();
        offset.x = 0.0f;
        offset.y = 0.0f;
        measureView();
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(new MapMoveListener());
        offset = new PointF();
        offset.x = 0.0f;
        offset.y = 0.0f;
        measureView();

    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(new MapMoveListener());
        offset = new PointF();
        offset.x = 0.0f;
        offset.y = 0.0f;
        measureView();
    }

    private void measureView() {
        viewSize = new PointF();
        viewSize.x = (float) getWidth();
        viewSize.y = (float) getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (renderer != null) {
            renderer.draw(canvas, offset);
        }
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setMapSize(float x, float y) {
        mapSize = new PointF();
        mapSize.x = x;
        mapSize.y = y;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void zoomOut() {
        if (zoomLevel > 1) {
            zoomLevel--;
            renderer.setZoomLevel(zoomLevel);

        }
    }

    public void zoomIn() {
        if (zoomLevel < MAX_ZOOM_LEVEL) {
            zoomLevel++;
            renderer.setZoomLevel(zoomLevel);
        }
    }

    private class MapMoveListener implements OnTouchListener {
        PointF lastPosition;
        MapMoveListener(){
            lastPosition = new PointF();
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == getId()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lastPosition.x = event.getX();
                    lastPosition.y = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float moveX = event.getX() - lastPosition.x;
                    float moveY = event.getY() - lastPosition.y;
                    lastPosition.x = event.getX();
                    lastPosition.y = event.getY();
                    if(offset.x + moveX < 0
                            && offset.x + moveX > (viewSize.x - mapSize.x*zoomLevel) ){
                        offset.x += moveX;
                    }
                    if(offset.y + moveY < 0
                            && offset.y + moveY > (viewSize.y - mapSize.y*zoomLevel) ){
                        offset.y += moveY;
                    }
                    invalidate();
                }
                return true;
            }
            return false;
        }
    }
}
