package pl.wroc.pwr.indoorlocalizationtieto.renderer.style;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pl.wroc.pwr.indoorlocalizationtieto.R;

public class StyleLoader {
    private static final String STYLE_LOADER_TAG = "STYLE LOADER TAG";
    static public String ReadStringFromResource(Context ctx, int resourceID) {
        StringBuilder contents = new StringBuilder();
        String sep = System.getProperty("line.separator");

        try {
            InputStream is = ctx.getResources().openRawResource(R.raw.mapjson);

            BufferedReader input = new BufferedReader(new InputStreamReader(is), 1024 * 8);
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(sep);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                input.close();
            }
        } catch (FileNotFoundException ex) {
            Log.e(STYLE_LOADER_TAG, "Couldn't find the file " + resourceID + " " + ex);
            return null;
        } catch (IOException ex) {
            Log.e(STYLE_LOADER_TAG, "Error reading file " + resourceID + " " + ex);
            return null;
        }
        return contents.toString();
    }
}
