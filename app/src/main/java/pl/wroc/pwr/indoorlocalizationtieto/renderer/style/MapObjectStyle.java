package pl.wroc.pwr.indoorlocalizationtieto.renderer.style;

import android.graphics.Paint;

public class MapObjectStyle {
    private int backgroundColor;
    private int borderColor;
    private int lineWidth;

    public MapObjectStyle() {
        backgroundColor = -1;
        borderColor = -1;
        lineWidth = -1;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void stylise(Paint paint) {
        if(backgroundColor != -1){
            paint.setColor(backgroundColor);
            paint.setStyle(Paint.Style.FILL);
        }
        if(borderColor != -1){
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
        }
        if(lineWidth != -1){
            paint.setStrokeWidth(lineWidth);
        }
    }
}
