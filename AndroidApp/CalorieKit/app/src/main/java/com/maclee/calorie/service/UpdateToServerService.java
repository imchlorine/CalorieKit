package com.maclee.calorie.service;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.os.AsyncTask;
import android.util.Log;

import com.maclee.calorie.BuildConfig;
import com.maclee.calorie.api.HttpUrlHelper;
import com.maclee.calorie.api.Toolkit;
import com.maclee.calorie.database.StepsDB;
import com.maclee.calorie.fragment.StepsFragment;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateToServerService extends IntentService {


    public static final String URL_REPORT = BuildConfig.SERVER_URL + "report";

    public UpdateToServerService() {
        super("UpdateToServerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("SERVICE","Update everyday!");
          new UpdateToServer().execute();


    }


    public class UpdateToServer extends AsyncTask<Void, Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            SharedPreferences report = getSharedPreferences("Report",0);
            int caloriegoals = Integer.parseInt(report.getString("caloriegoals","0"));
            int caloriesburned = Integer.parseInt(report.getString("caloriesburned","0"));
            int caloriesconsumed = Integer.parseInt(report.getString("caloriesconsumed","0"));
            int stepstotal = Integer.parseInt(report.getString("stepstotal","0"));
            String reportdate = Toolkit.getCurrentDataTime();
            SharedPreferences userInfor = getSharedPreferences("MyInfo",0);

             String info = Toolkit.getUserInfor(userInfor).toString();

            String jsonString = "{\"caloriegoals\": \"" + caloriegoals
                    + "\"" + "," + "\"caloriesburned\": \"" + caloriesburned
                    + "\"" + "," + "\"caloriesconsumed\": \"" + caloriesconsumed
                    + "\"" + "," + "\"stepstotal\": \"" + stepstotal
                    + "\"" + "," + "\"userid\":" + info
                    + "," + "\"reportdate\": \"" + reportdate + "\"}";
            String data = new HttpUrlHelper().getHttpUrlPostConnection(URL_REPORT, jsonString);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new DeleteAllStep().execute();
            Log.e("Updated","Sucess!");
        }
    }


    public class DeleteAllStep extends AsyncTask<Object,String,String> {


        @Override
        protected String doInBackground(Object... objects) {
            StepsDB sdb = Room.databaseBuilder(getApplicationContext(),
                    StepsDB.class, "steps_db")
                    .fallbackToDestructiveMigration()
                    .build();
            sdb.stepsDao().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


    }


}
