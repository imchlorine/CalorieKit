package com.maclee.calorie.api;

public class GoogleSearchEngine {

    private final String key = "Your key";
    private final String searchId = "Your search id";

    public String searchKeywords(String keywords){
        StringBuilder sb = new StringBuilder("https://www.googleapis.com/customsearch/v1?");
        sb.append("&q=" + keywords);
        sb.append("&key=" + key);
        sb.append("&cx=" + searchId);
        String url = sb.toString();
        String data = new HttpUrlHelper().getHttpUrlConnection(url);
        return data;
    }
}
