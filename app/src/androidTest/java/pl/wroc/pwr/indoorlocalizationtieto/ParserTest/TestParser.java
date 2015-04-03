package pl.wroc.pwr.indoorlocalizationtieto.ParserTest;

import android.test.InstrumentationTestCase;

import pl.wroc.pwr.indoorlocalizationtieto.Parser.Parser;

/**
 * Created by Mateusz on 2015-03-22.
 */
public class TestParser extends InstrumentationTestCase {
    Parser parser;

    @Override
    public void setUp() throws Exception {
        parser = new Parser(100,50,0.05);
    }


    public void testGenerateQuery()throws Exception{
        assertEquals("[out:json];\n" +
                "(\n" +
                "  node\n" +
                "(49.95,99.95,50.05,100.05);\n" +
                "  way\n" +
                "(49.95,99.95,50.05,100.05);\n" +
                "  rel\n" +
                "(49.95,99.95,50.05,100.05);\n" +
                ");\n" +
                "(._;>;);\n" +
                "out;",parser.generateQuery());
    }
    public void testEncodeQuery()throws Exception{
        assertEquals("%5Bout%3Ajson%5D%3B%0A%28%0A++node%0A%2849.95%2C99.95%2C50.05%2C100.0" +
                        "5%29%3B%0A++way%0A%2849.95%2C99.95%2C50.05%2C100.05%29%3B%0A++rel%0" +
                        "A%2849.95%2C99.95%2C50.05%2C100.05%29%3B%0A%29%3B%0A%28._%3B%3E%3B%2" +
                        "9%3B%0Aout%3B",
                parser.encodeQuery());
    }

}
