package uk.ac.ed.inf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MenuParser {
    String jsonText;
    MenuParser(String jsonText){
        this.jsonText = jsonText;
    }
    ArrayList<Menu> wordList = null;
    public static class Menu {
        String name;
        String location;
        List<Item> menu;
        public static class Item {
            String item;
            int pence;
        }
    }

    public void parseMenus(){
        Type listType = new TypeToken<ArrayList<Menu>>(){}.getType();
        wordList = new Gson().fromJson(this.jsonText, listType);
        System.out.println(wordList);
    }
}
