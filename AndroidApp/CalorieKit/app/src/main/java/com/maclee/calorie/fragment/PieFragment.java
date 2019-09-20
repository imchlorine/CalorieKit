package com.maclee.calorie.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.maclee.calorie.BuildConfig;
import com.maclee.calorie.MainActivity;
import com.maclee.calorie.R;
import com.maclee.calorie.api.HttpUrlHelper;
import com.maclee.calorie.api.Toolkit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PieFragment extends Fragment implements DatePickerFragment.DatePickerFragmentListener {

    View view;
    private PieChart pieChart;
    EditText etSelectDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pie,container,false);

        etSelectDate = view.findViewById(R.id.et_report_date);

        etSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        pieChart = view.findViewById(R.id.pie_chart);
        etSelectDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                generate(etSelectDate.getText().toString());
            }
        });

        etSelectDate.setText(Toolkit.getCurrentDate());
       // generate(Toolkit.getCurrentDate());


        return view;

    }

    @Override
    public void onDateSet(int year, int month, int day) {
        String date = String.format("%02d", year) + "-" +  String.format("%02d", month + 1) + "-" + String.format("%02d", day);
        etSelectDate.setText(date);
    }

    public class GetReportData extends AsyncTask<Object,String,String> {
        @Override
        protected String doInBackground(Object... objects) {

            //Log.w("Report","I'm here!");
            String url = (String) objects[0];
            String data = new HttpUrlHelper().getHttpUrlConnection(url);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {

            JSONObject data = null;
            if (!s.equals("")){

                JSONArray resultArray = null;
                try {
                    resultArray = new JSONArray(s);
                    data= resultArray.getJSONObject(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                generatePieData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private PieData generatePieData(JSONObject data) throws JSONException {

        PieData pieData =null;
        if (data != null){
            int totalConsumed = data.getInt("totalconsumed");
            int totalBurned = data.getInt("totalBurned");
            int remaining = data.getInt("remaining");
            int goal = totalConsumed - totalBurned - remaining;

            int color[] = null;
            if (remaining < 0){
                remaining = -remaining;
                color = new int[]{getResources().getColor(R.color.colorConsumed),
                                getResources().getColor(R.color.colorBurned),
                                getResources().getColor(R.color.colorRemained)};

            }else{
                color = new int[]{getResources().getColor(R.color.colorConsumed),
                        getResources().getColor(R.color.colorBurned),
                                getResources().getColor(R.color.colorDefict)};


            }

            ArrayList<PieEntry> entries1 = new ArrayList<>();
            entries1.add(new PieEntry(totalConsumed,"Consumed"));
            entries1.add(new PieEntry(totalBurned,"Burned"));
            entries1.add(new PieEntry(remaining, "Remaining"));
            PieDataSet ds1 = new PieDataSet(entries1,"Calorie");
            ds1.setColors(color);
            ds1.setSliceSpace(1f);
            ds1.setValueTextColor(Color.WHITE);
            ds1.setValueTextSize(12f);
            pieData = new PieData(ds1);
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText(generateCenterText(goal));
            pieChart.setCenterTextSize(10f);
            // radius of the center hole in percent of maximum radius
            pieChart.setHoleRadius(20f);
            pieChart.setTransparentCircleRadius(50f);
            Legend legend = pieChart.getLegend();
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setFormSize(18f);

            pieChart.animateX(1000);

        }
        pieChart.setData(pieData);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
        return pieData;
    }

    private SpannableString generateCenterText(int goal) {
        SpannableString s = new SpannableString( "Goal \n" + goal);
        s.setSpan(new RelativeSizeSpan(2f), 0, 4, 0);
        s.setSpan(new ForegroundColorSpan(Color.BLUE), 5, s.length(), 0);
        return s;
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.onDateSetListener(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void generate(String reportDate){
        String userid = null;
        try {
            userid = ((MainActivity) getActivity()).infoObj.getString("userid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String reportDate = Toolkit.getCurrentDate();

        StringBuilder sb = new StringBuilder(BuildConfig.SERVER_URL + "report/findByUserAndDate");
        sb.append("/" + userid);
        sb.append("/" + reportDate);
        String url = sb.toString();
        Object dataTransfer[] = new Object[1];
        dataTransfer[0] = url;
        new GetReportData().execute(dataTransfer);

    }

}
