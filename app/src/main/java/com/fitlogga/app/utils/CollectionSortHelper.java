package com.fitlogga.app.utils;

public class CollectionSortHelper {
    public static int byDescending(long t1, long t2) {
        if (t1 == t2) {
            return 0;
        }
        return t1 > t2 ? -1 : 1;
    }
}
