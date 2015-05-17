package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class UserPositionDrawable extends Drawable {
    private static final int RADIUS = 100;
    private static final int COLOR = Color.RED;
    private PointF offset;

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    private int xPos;
    private int yPos;
    Paint paint;
    RectF boundingRect;

    public UserPositionDrawable(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        paint = new Paint();
        paint.setColor(COLOR);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(20);

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPoint(offset.x - xPos, offset.y - yPos, paint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public void setOffset(PointF offset) {

        this.offset = offset;
    }
}
