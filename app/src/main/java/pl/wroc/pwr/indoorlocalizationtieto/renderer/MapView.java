package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class MapView extends View {
    public static final String MAP_VIEW_TAG = "MAP_VIEW_TAG";
    public static final int MAX_ZOOM_LEVEL = 3;
    private static final float ZOOM_SPAN_SCALE = 0.05f;
    private Renderer renderer = null;
    private float zoomLevel = 1;
    private PointF offset;
    private PointF viewSize;
    private PointF mapSize;
    private MoveGestureListener moveListener;
    private ScaleGestureListener scaleListener;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scalegestureDetector;
    private boolean isScaling = false;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
        setOnTouchListener(new MapMoveListener());
        offset = new PointF();
        offset.x = 0.0f;
        offset.y = 0.0f;
        measureView();
    }

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(new MapMoveListener());
        moveListener = new MoveGestureListener();
        gestureDetector = new GestureDetector(context, moveListener);
        scaleListener = new ScaleGestureListener();
        scalegestureDetector = new ScaleGestureDetector(context, scaleListener);

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

    public void setPosition(float x, float y) {
        if (x < mapSize.x && y < mapSize.y) {
            offset.x = x;
            offset.y = y;
        }
    }

    private class MapMoveListener implements OnTouchListener {
        PointF lastPosition;

        MapMoveListener() {
            lastPosition = new PointF();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }

            if (scalegestureDetector.onTouchEvent(event)) {
                return true;
            }
            return false;
        }
    }

    private class MoveGestureListener extends GestureDetector.SimpleOnGestureListener {
        public MoveGestureListener() {
            super();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isScaling)
                return true;

            if (offset.x - distanceX < 0
                    && offset.x - distanceX > (viewSize.x - mapSize.x * zoomLevel)) {
                offset.x -= distanceX;
            }
            if (offset.y - distanceY < 0
                    && offset.y - distanceY > (viewSize.y - mapSize.y * zoomLevel)) {
                offset.y -= distanceY;
            }
            invalidate();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        public ScaleGestureListener() {
            super();
        }
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float spanDelta = (detector.getCurrentSpan() - detector.getPreviousSpan())*ZOOM_SPAN_SCALE;
            if (spanDelta > 1) {
                spanDelta = 1;
            } else if (spanDelta < -1) {
                spanDelta = -1;
            }

            if ((zoomLevel + spanDelta) <= 4
                    && (zoomLevel + spanDelta) > 0) {
                zoomLevel += spanDelta;
                renderer.setZoomLevel(zoomLevel);
                invalidate();
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            isScaling = true;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isScaling = false;
            super.onScaleEnd(detector);
        }
    }
}
