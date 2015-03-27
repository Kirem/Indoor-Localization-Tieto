package pl.wroc.pwr.indoorlocalizationtieto.renderer.style;

public class MapObjectStyle {
    private Integer backgroundColor = null;
    private Integer borderColor = null;
    private Integer lineWidth = null;

    public MapObjectStyle() {
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
}
