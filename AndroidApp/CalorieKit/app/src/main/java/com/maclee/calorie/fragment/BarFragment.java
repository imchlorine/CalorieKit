package com.maclee.calorie.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.JsonArray;
import com.maclee.calorie.BuildConfig;
import com.maclee.calorie.MainActivity;
import com.maclee.calorie.R;
import com.maclee.calorie.api.HttpUrlHelper;
import com.maclee.calorie.api.Toolkit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BarFragment extends Fragment implements DatePickerFragment.DatePickerFragmentListener {

    View view;
    private BarChart barChart;
    private EditText etStartDate;
    private EditText etEndDate;
    private Button btnSearch;
    ArrayList<BarEntry> barBurned;
    ArrayList<BarEntry> barConsumed;
    List<String> date;
    int DATE_DIALOG = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_bar, container, false);

        etStartDate = view.findViewById(R.id.et_start_date);
        etEndDate = view.findViewById(R.id.et_end_date);
        btnSearch = view.findViewById(R.id.search_range);


        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 1;
                showDatePickerDialog("StartDate");

            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 2;
                showDatePickerDialog("EndDate");
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = etStartDate.getText().toString();
                String endDate = etEndDate.getText().toString();

                if (!startDate.equals("") && !endDate.equals("")) {
                    String userId = "";
                    try {
                        userId = ((MainActivity) getActivity()).infoObj.getString("userid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    StringBuilder sb = new StringBuilder(BuildConfig.SERVER_URL + "report/findByUserAndDateRange");
                    sb.append("/" + userId);
                    sb.append("/" + startDate);
                    sb.append("/" + endDate);
                    String url = sb.toString();
                    Object dataTransfer[] = new Object[1];
                    dataTransfer[0] = url;
                    new GetReportData().execute(dataTransfer);
                } else {
                    Toast.makeText(getContext(),"Please select Date",Toast.LENGTH_LONG).show();
                }


            }
        });
        return view;

    }


    @Override
    public void onDateSet(int year, int month, int day) {
        String date = String.format("%02d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
        if (DATE_DIALOG == 1) {
            etStartDate.setText(date);
        } else if (DATE_DIALOG == 2) {
            etEndDate.setText(date);
        }

    }


    public class GetReportData extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... objects) {

            //Log.w("Report","I'm here!");
            String url = (String) objects[0];
            String data = new HttpUrlHelper().getHttpUrlConnection(url);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            barBurned = new ArrayList<>();
            barConsumed = new ArrayList<>();
            date = new ArrayList<>();
            if (!s.equals("")) {
                JSONArray data = null;
                try {
                    data = new JSONArray(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < data.length(); i++) {
                    try {
                        barBurned.add(new BarEntry(i, data.getJSONObject(i).getInt("caloriesburned")));
                        barConsumed.add(new BarEntry(i, data.getJSONObject(i).getInt("caloriesconsumed")));
                        date.add(Toolkit.getFormatDate(data.getJSONObject(i).getString("reportdate")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            generateBarChart();

        }
    }

    public BarChart generateBarChart() {

        barChart = view.findViewById(R.id.bar_chart);
        BarDataSet barDataSet1 = new BarDataSet(barBurned, "Burned");
        barDataSet1.setColor(getResources().getColor(R.color.colorBurned));
        BarDataSet barDataSet2 = new BarDataSet(barConsumed, "Consumed");
        barDataSet2.setColor(getResources().getColor(R.color.colorConsumed));
        BarData barData = new BarData(barDataSet1, barDataSet2);
        barData.setValueTextSize(14f);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.getData().setHighlightEnabled(false);
        barChart.animateY(1000);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        Legend legend = barChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setFormSize(18f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(date));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        barChart.setVisibleXRangeMaximum(date.size());
        //(barWidth + barSpace) * num of bar + groupSpace = 1
        float barSpace = 0.0f;
        float groupSpace = 0.2f;
        barData.setBarWidth(0.4f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * date.size());
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();

        return null;

    }


    public void showDatePickerDialog(String TAG) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.onDateSetListener(this);
        newFragment.show(getFragmentManager(), TAG);
    }

}
