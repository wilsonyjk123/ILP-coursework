package uk.ac.ed.inf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class WordParser {
    String jsonText;
    WordParser(String jsonText){
        this.jsonText = jsonText;
    }
    Word word = null;

    public static class Word {
        String country;
        Square square;
        public static class Square {
            Southwest southwest;
            public static class Southwest {
                double lng;
                double lat;
            }
            Northeast northeast;
            public static class Northeast {
                double lng;
                double lat;
            }
        }
        String nearestPlace;
        Coordinates coordinates;

        public static class Coordinates {
            double lng;
            double lat;
        }

        String language;
        String map;
    }
    public void parseWord(){
        word = new Gson().fromJson(jsonText, Word.class);
    }


}
