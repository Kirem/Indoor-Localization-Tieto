package pl.wroc.pwr.indoorlocalizationtieto.Geometry;

import java.util.List;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class LineString extends Geometry {
    private List<Line> lines;

    public LineString(List<Line> linelist) {
        lines.addAll(linelist);
    }

    public LineString(Line[] linearr) {
        for (Line l : linearr) {
            lines.add(l);
        }
    }


    @Override
    public double CalculateLength() {
        float length =0;
        for(Line l : lines) {
            length += l.CalculateLength();
        }
        return length;
    }

    public List<Line> getLines() { return lines; }
}