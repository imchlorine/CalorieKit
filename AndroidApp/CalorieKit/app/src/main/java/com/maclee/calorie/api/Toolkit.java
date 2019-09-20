package com.maclee.calorie.api;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maclee.calorie.model.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Toolkit {

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String addTimePattern(String date){
        date = date + "T00:00:00+00:00";
        return date;
    }

    public static String getCurrentDataTime(){
        Date c = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String formattedDate = df.format(c);
        return formattedDate;
    }
    public static String getCurrentTime(){
        Date c = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String getFormatDataTime(String datetime) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = df.parse(datetime);
        df = new SimpleDateFormat("MMM dd HH:mm");
        return df.format(date);
    }

    public static String getFormatDate(String datetime) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = df.parse(datetime);
        df = new SimpleDateFormat("MMM-dd");
        return df.format(date);
    }


    public static JSONObject getUserInfor(SharedPreferences settings){
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        String userInfo = settings.getString("userInfo","");
        JSONObject infoObj = null;
        try {
            JSONArray resultArray = new JSONArray(userInfo);
            JSONObject dataObj = resultArray.getJSONObject(0);
            String info = dataObj.getString("userid");
            infoObj = new JSONObject(info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return infoObj;
    }

    public static void parseJSON(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Food>>(){}.getType();
        List<Food> foodList = gson.fromJson(jsonString, type);
        for (Food food : foodList){

        }
    }
}
