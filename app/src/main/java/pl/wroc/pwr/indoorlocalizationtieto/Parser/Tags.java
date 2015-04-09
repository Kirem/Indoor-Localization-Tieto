package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PiroACC on 2015-04-09.
 */
public class Tags {

    //TODO ZDEFINIOWAC W KONCU STRUKUTRE NA TAGI
    public static final String WAYS_TAGS[] = new String[]{"building"};
    public static final String NODES_TAGS[] = new String[]{"building"};
    public static final String RELATIONS_TAGS[] = new String[]{"name"};

    // JSON Node names
    //TODO prawdopodobnie do wyrzucenia
    public static final String TAG_LEVEL = "level";
    public static final String NODES = "nodes";
    public static final String ELEMENTS = "elements";
    public static final String MEMBERS = "members";
    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String REF = "ref";
    public static final String ROLE = "role";
    public static final String TAGS = "tags";
    public static final String BUILDINGPART = "buildingpart";
    public static final String TAG_TAGS_HIGHWAY = "highway";
    public static final String TAG_TAGS_POI = "POI";
    public static final String TAG_TAGS_DOOR = "door";
    public static final String TAG_TAGS_TYPE = "type";
    public static final String TAG_TAGS_REF = "ref";
    public static final String TAG_TAGS_NAME = "name";
    public static final String TAG_TAGS_ROUTE = "route";
}
