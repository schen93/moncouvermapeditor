package com.schenplayground.fight_map_editor;

public class Util {

    public static int getRandomInt(int min, int max) {
        return (int)Math.round(Math.random() * ( max - min )) + min;
    }
}
