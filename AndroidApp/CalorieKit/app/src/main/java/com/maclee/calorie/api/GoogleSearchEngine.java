package com.maclee.calorie.api;

public class GoogleSearchEngine {

    private final String key = "AIzaSyCG1k9FXjE9dTEA3Zul0H_yqfflYvS5j1E";
    private final String searchId = "015016294801435681717:gbdcvojkngy";

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
