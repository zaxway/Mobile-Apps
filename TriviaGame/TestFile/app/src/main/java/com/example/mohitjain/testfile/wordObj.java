package com.example.mohitjain.testfile;

/**
 * Created by mohitjain on 2/5/18.
 */

public class wordObj {

    private String word;
    private String definition;

    public wordObj(String w, String d) {
        word = w;
        definition = d;
    }

    public String getWord() {
        return word;
    }

    public String getDefintion() {
        return definition;
    }

    public void setWord(String words) {
        word = words;
    }

    public void setDefinition(String definitions) {
        definition = definitions;
    }

}
