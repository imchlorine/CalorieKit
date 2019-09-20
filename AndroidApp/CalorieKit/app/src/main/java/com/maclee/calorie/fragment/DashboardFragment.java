package com.maclee.calorie.fragment;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maclee.calorie.BuildConfig;
import com.maclee.calorie.MainActivity;
import com.maclee.calorie.R;
import com.maclee.calorie.api.HttpUrlHelper;
import com.maclee.calorie.api.Toolkit;
import com.maclee.calorie.database.StepsDB;
import com.maclee.calorie.model.Steps;
import org.json.JSONException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.os.Looper.getMainLooper;
import static com.maclee.calorie.service.UpdateToServerService.URL_REPORT;


public class DashboardFragment extends Fragment {

    TextView tvWelcome;
    TextView tvCurrentTime;
    TextView tvTotalSteps;
    TextView tvTotalBurned;
    TextView tvTotalConsumed;
    TextView tvCalorieGoal;
    TextView tvBMR;
    ImageView ivBanner;
    StepsDB sdb;
    Button btnSetgoal;
    Button btnUpdate;
    SharedPreferences settings;
    //TextView tvHeadEmail;

    public static final String URL_TOTAL_BURNED = BuildConfig.SERVER_URL + "calorieuser/caloriesperstep/";
    public static final String URL_BMR = BuildConfig.SERVER_URL + "calorieuser/bmr/";
    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        sdb = Room.databaseBuilder(getActivity().getApplicationContext(),
                StepsDB.class, "steps_db")
                .fallbackToDestructiveMigration()
                .build();
        settings = getActivity().getSharedPreferences("Report", 0); // 0 - for private mode
        tvWelcome = view.findViewById(R.id.tv_welcome);
        tvCurrentTime = view.findViewById(R.id.tv_current_time);
        tvTotalSteps = view.findViewById(R.id.tv_total_steps);
        tvTotalBurned = view.findViewById(R.id.tv_total_burned);
        tvTotalConsumed = view.findViewById(R.id.tv_total_consumed);
        ivBanner = view.findViewById(R.id.iv_banner);
        tvCalorieGoal = view.findViewById(R.id.tv_set_goal);
        btnSetgoal = view.findViewById(R.id.set_goal);
        btnUpdate = view.findViewById(R.id.btn_update);
        tvBMR = view.findViewById(R.id.tv_bmr);
        tvCalorieGoal.setText(settings.getString("caloriegoals","0"));




        btnSetgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                //builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Set you calorie goal");
                final EditText inputGoal = new EditText(getContext());
                inputGoal.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputGoal.setText(settings.getString("goal",""));
                if (inputGoal.getText().toString().equals("0")){
                    inputGoal.setText("");
                }
                builderSingle.setView(inputGoal);
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setPositiveButton("Set Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(),input.getText().toString(),Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("caloriegoals", inputGoal.getText().toString());
                        editor.commit();
                        tvCalorieGoal.setText(inputGoal.getText().toString());
                    }
                });
                builderSingle.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              new UpdateToServer().execute();
            }
        });
       // tvSet.setPaintFlags(tvSet.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

       // tvHeadEmail = view.findViewById(R.id.tv_header_email);



        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        try {
            String firstName = ((MainActivity)getActivity()).infoObj.getString("firstname");
            //String email = ((MainActivity)getActivity()).infoObj.getString("email");
           // tvHeadEmail.setText(email);
            tvWelcome.setText("Welcome, " + firstName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Handler realTimeHandler = new Handler(getMainLooper());
        realTimeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    tvCurrentTime.setText(Toolkit.getFormatDataTime(Toolkit.getCurrentDataTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                realTimeHandler.postDelayed(this, 1000);
            }
        }, 10);

        String totalSteps = "";
        try {
           totalSteps =  new GetTotalSteps().execute().get();
           tvTotalSteps.setText(totalSteps);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Object dataTransfer[] = new Object[1];
        try {
            dataTransfer[0] = ((MainActivity)getActivity()).infoObj.getString("userid") ;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Double burnedPerStep = Double.parseDouble(new GetBurnedPerStep().execute(dataTransfer).get());
            String tatalBurned =  String.valueOf((int)(burnedPerStep * Integer.parseInt(totalSteps)));
            tvTotalBurned.setText(tatalBurned);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("caloriesburned", tatalBurned);
            editor.commit();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder(BuildConfig.SERVER_URL + "consumption/totalcaloriesconsumed/");
        try {
            sb.append(((MainActivity)getActivity()).infoObj.getString("userid"));
            sb.append("/");
            sb.append(Toolkit.getCurrentDate());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Object dataTransferUrl[] = new Object[1];
        dataTransferUrl[0] = sb.toString();
        try {
           String totalConsumed =  new GetTotalConsumed().execute(dataTransferUrl).get();
           tvTotalConsumed.setText(totalConsumed);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Object dataTransferBMRURL[] = new Object[1];
        try {
            String url =  URL_BMR + ((MainActivity)getActivity()).infoObj.getString("userid");
            dataTransferBMRURL[0] = url;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new GetBMR().execute(dataTransferBMRURL);

        return view;
    }

    public class GetTotalSteps extends AsyncTask<Object,String,String> {

        @Override
        protected String doInBackground(Object... objects) {
            int totalSteps = 0;
            List<Steps> steps = sdb.stepsDao().getAll();
            if (!(steps.isEmpty() || steps == null) ){
                for (Steps temp : steps) {
                    totalSteps = totalSteps + temp.getSteps();
                }
            }
            return String.valueOf(totalSteps);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("stepstotal", s);
            editor.commit();

        }
    }

    public class GetBurnedPerStep extends AsyncTask<Object,String,String>{

        @Override
        protected String doInBackground(Object... objects) {
            String userId = (String) objects[0];
            return new HttpUrlHelper().getHttpUrlConnection(URL_TOTAL_BURNED + userId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    public class GetTotalConsumed extends AsyncTask<Object,String,String>{

        @Override
        protected String doInBackground(Object... objects) {
            String url = (String) objects[0];
            return new HttpUrlHelper().getHttpUrlConnection(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("caloriesconsumed", s);
            editor.commit();

        }
    }

    public class GetBMR extends AsyncTask<Object,String,String>{

        @Override
        protected String doInBackground(Object... objects) {
            String result = new HttpUrlHelper().getHttpUrlConnection((String)objects[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvBMR.setText(s);
        }
    }


    public class DeleteAllStep extends AsyncTask<Object,String,String> {

        @Override
        protected String doInBackground(Object... objects) {
            StepsDB sdb = Room.databaseBuilder(getContext(),
                    StepsDB.class, "steps_db")
                    .fallbackToDestructiveMigration()
                    .build();
            sdb.stepsDao().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            reload();
            Toast.makeText(getActivity(),"Updated Success!",Toast.LENGTH_LONG).show();
        }


    }

    public class UpdateToServer extends AsyncTask<Void, Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            SharedPreferences report = getActivity().getSharedPreferences("Report",0);
            int caloriegoals = Integer.parseInt(report.getString("caloriegoals","0"));
            int caloriesburned = Integer.parseInt(report.getString("caloriesburned","0"));
            int caloriesconsumed = Integer.parseInt(report.getString("caloriesconsumed","0"));
            int stepstotal = Integer.parseInt(report.getString("stepstotal","0"));
            String reportdate = Toolkit.getCurrentDataTime();
            SharedPreferences userInfor = getActivity().getSharedPreferences("MyInfo",0);

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

        }


    }

    public void reload(){
        // Reload current fragment
        ///Fragment frg = null;
       // frg = getFragmentManager().findFragmentByTag("DashboardFragment");
       // final FragmentTransaction ft = getFragmentManager().beginTransaction();
       // ft.detach(frg);
        //ft.attach(frg);
       // ft.commit();
        tvTotalSteps.setText("0");
        tvTotalBurned.setText("0");
    }
}
