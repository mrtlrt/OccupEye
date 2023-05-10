package com.example.occupeye;

import java.util.ArrayList;

public class Bookmark {
    private static Bookmark bookmark= null;

    public static ArrayList<String> getBookmarkedLocs() {
        return bookmarkedLocs;
    }
    private static ArrayList<String> bookmarkedLocs = new ArrayList<>();

    public static void setBookmarkedLocs(ArrayList<String> bookmarkedLocs) {
        Bookmark.bookmarkedLocs = bookmarkedLocs;
    }

    private Bookmark(){
        bookmarkedLocs=new ArrayList<>();
    }
    public static Bookmark getBookmark(){
        if(bookmark==null){
            new Bookmark();
        }
        return bookmark;
    }
}
