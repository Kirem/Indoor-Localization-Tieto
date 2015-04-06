package pl.wroc.pwr.indoorlocalizationtieto.renderer.style;

public class StyleMatcher {
    private final int zoomLevel;
    private final String objectClass;
    private final String object;

    public StyleMatcher(int zoomLevel, String objectClass, String object){
        this.zoomLevel = zoomLevel;
        this.objectClass = objectClass;
        this.object = object;
    }

    @Override
    public boolean equals(Object o) {
        StyleMatcher matcher = (StyleMatcher) o;
        return this.object.equals(matcher.object)
                && this.objectClass.equals(matcher.objectClass)
                && this.zoomLevel==matcher.zoomLevel;
    }

    @Override
    public int hashCode() {
        int hashCode = (int) (Math.pow(zoomLevel, zoomLevel)
                + objectClass.hashCode()*133
                + object.hashCode()*zoomLevel*977);
        return hashCode;
    }
}