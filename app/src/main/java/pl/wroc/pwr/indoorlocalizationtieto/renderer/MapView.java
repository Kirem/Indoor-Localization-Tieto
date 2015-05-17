package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs);
    }

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

        new Handler().postDelayed(new MapViewRefreshThread(), 1000/30);
    }

    private void measureView() {
        viewSize = new PointF();
        viewSize.x = (float) getWidth();
        viewSize.y = (float) getHeight();
        userPosition = new UserPositionDrawable(50, 50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (renderer != null) {
            userPosition.setOffset(new PointF(offset.x * zoomLevel, offset.y * zoomLevel));
            renderer.draw(canvas, offset);
            userPosition.draw(canvas);
        }
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

    public void setMapSize(float x, float y) {
        mapSize = new PointF();
        mapSize.x = x;
        mapSize.y = y;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setPosition(float lon, float lat) {
        int x = (int) pointCalculator.calibrateX(lat);
        int y = (int) pointCalculator.calibrateY(lon);
        if (x < mapSize.x && y < mapSize.y) {
            offset.x = x;
            offset.y = y;
        }
        userPosition.setxPos(x);
        userPosition.setyPos(y);
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

//        invalidate();
    }

    public void zoomIn() {
        zoomLevel += 1;
        if (zoomLevel > MAX_ZOOM_LEVEL)
            zoomLevel = MAX_ZOOM_LEVEL;
        renderer.setZoomLevel(zoomLevel);
//        invalidate();
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

    public void setZoomLevel(float zoomLevel) {
        if (zoomLevel > MAX_ZOOM_LEVEL) {
            zoomLevel = MAX_ZOOM_LEVEL;
        } else if (zoomLevel < 1) {
            zoomLevel = 1;
        }
        renderer.setZoomLevel(zoomLevel);
        this.zoomLevel = zoomLevel;
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
//                invalidate();
                updateCount = 0;
                offset.y -= distanceY;
                offset.x -= distanceX;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        int updateCount = 0;

        public ScaleGestureListener() {
            super();
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float spanDelta = (detector.getCurrentSpan() - detector.getPreviousSpan()) * ZOOM_SPAN_SCALE;
            if (spanDelta > 1) {
                spanDelta = 1;
            } else if (spanDelta < -1) {
                spanDelta = -1;
            }

            if ((zoomLevel + spanDelta) <= 4
                    && (zoomLevel + spanDelta) > 0) {
                if (updateCount++ >= 2) {
                    zoomLevel += spanDelta;
                    renderer.setZoomLevel(zoomLevel);
//                    invalidate();
                    updateCount = 0;
                }
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
            super.onScaleEnd(detector);
            isScaling = false;
        }
    }

    private class MapViewRefreshThread implements Runnable {

        @Override
        public void run() {
//            super.run();
            MapView.this.invalidate();
            Log.i("THREAD", "refresh");
            if(MapView.this.isShown())
                postDelayed(this, 1000 / 3);//30frames/sec
        }
    }
}
