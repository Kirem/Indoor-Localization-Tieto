package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;

public class MapView extends View {
    public static final String MAP_VIEW_TAG = "MAP_VIEW_TAG";
    public static final int MAX_ZOOM_LEVEL = 5;
    private static final float ZOOM_SPAN_SCALE = 0.1f;
    private Renderer renderer = null;
    private float zoomLevel = 1;
    private float zoomMultiplier = 4;
    private PointF offset;
    private PointF viewSize;
    private PointF mapSize;
    private MoveGestureListener moveListener;
    private ScaleGestureListener scaleListener;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scalegestureDetector;
    private boolean isScaling = false;
    private MapObjectPointCalculator pointCalculator;
    private UserPositionDrawable userPosition;

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
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
        new Handler().postDelayed(new MapViewRefreshThread(), 1000 / 30);
    }

    private void measureView() {
        viewSize = new PointF();
        viewSize.x = (float) getWidth();
        viewSize.y = (float) getHeight();
        userPosition = new UserPositionDrawable(50, 50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.save();
        canvas.translate(offset.x * zoomLevel, offset.y * zoomLevel);
        if (renderer != null) {
            userPosition.setOffset(new PointF(offset.x * zoomLevel, offset.y * zoomLevel));
            renderer.draw(canvas, offset);
            userPosition.draw(canvas);
        }
        canvas.restore();
    }

    public void setMapSize(float x, float y) {
        mapSize = new PointF();
        mapSize.x = x;
        mapSize.y = y;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(final Renderer renderer) {
        this.renderer = renderer;
        if (getWidth() != 0) {
            renderer.setDrawnArea(getWidth(), getHeight());
        } else {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    MapView.this.renderer.setDrawnArea(getWidth(), getHeight());
                }
            });
        }
    }

    public void setPosition(float lon, float lat) {
        PointF p = pointCalculator.calibrate(lat, lon);
        if (p.x < mapSize.x && p.y < mapSize.y) {
            offset.x = p.x + getWidth() / 2;
            offset.y = p.y + getHeight() / 2;
        }
        Log.i("MAP", "user x = " + p.x + " user y = " + p.y);
        userPosition.setxPos((int) p.x);
        userPosition.setyPos((int) p.y);
    }

    public MapObjectPointCalculator getPointCalculator() {
        return pointCalculator;
    }

    public void setPointCalculator(MapObjectPointCalculator pointCalculator) {
        this.pointCalculator = pointCalculator;
    }

    public void zoomOut() {
        if (zoomLevel >= 2)
            zoomLevel -= 1;
        else
            zoomLevel = 1;
        renderer.setZoomLevel(zoomLevel);
    }

    public void zoomIn() {
        zoomLevel += 1;
        if (zoomLevel > MAX_ZOOM_LEVEL)
            zoomLevel = MAX_ZOOM_LEVEL;
        renderer.setZoomLevel(zoomLevel);
    }

    public void setZoomLevel(float zoomLevel) {
        if (zoomLevel > MAX_ZOOM_LEVEL) {
            zoomLevel = MAX_ZOOM_LEVEL;
        } else if (zoomLevel < 1) {
            zoomLevel = 1;
        }
        renderer.setZoomLevel(zoomLevel);
        this.zoomLevel = zoomLevel;
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
        int updateCount = 0;

        public MoveGestureListener() {
            super();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isScaling)
                return true;

            if (updateCount++ >= 2) {
                updateCount = 0;
                offset.y -= distanceY;
                offset.x -= distanceX;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        public ScaleGestureListener() {
            super();
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            zoomLevel *= detector.getScaleFactor();
            zoomLevel = Math.max(1, Math.min(5, zoomLevel));
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            isScaling = true;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            isScaling = false;
        }
    }

    private class MapViewRefreshThread implements Runnable {

        @Override
        public void run() {
            MapView.this.invalidate();
            if (MapView.this.isShown())
                postDelayed(this, 1000 / 3);//30frames/sec
        }
    }
}
