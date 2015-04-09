package pl.wroc.pwr.indoorlocalizationtieto.ParserTest;

import android.test.InstrumentationTestCase;

import pl.wroc.pwr.indoorlocalizationtieto.Parser.OverpassDataFetcher;

/**
 * Created by Mateusz on 2015-03-22.
 */
public class TestParser extends InstrumentationTestCase {

    OverpassDataFetcher overpassDataFetcher = new OverpassDataFetcher();
//    OverpassDataFetcher overpassDataFetcherWithQuery = new OverpassDataFetcher();

    @Override
    public void setUp() throws Exception {
        overpassDataFetcher.fetchDataWithinRadius(51.1080643, 17.0647986, 100.0);
/*        overpassDataFetcherWithQuery.fetchData("[out:json];\n" +
                "rel(around:100.0,51.1080643,17.0647986);(._;>;);out;\n" +
                "way(around:100.0,51.1080643,17.0647986);(._;>;);out;\n" +
                "node(around:100.0,51.1080643,17.0647986);out;");*/
    }

    public void testOverpassDataFetcher() throws Exception {
    }
}
