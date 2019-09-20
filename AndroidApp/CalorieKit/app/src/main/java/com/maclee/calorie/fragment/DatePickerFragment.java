package com.maclee.calorie.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.maclee.calorie.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DatePickerFragmentListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.TimePickerTheme, this, year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        // Create a new instance of DatePickerDialog and return it
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        listener.onDateSet(year,month,day);
       // String date = String.format("%02d", year) + "-" +  String.format("%02d", month + 1) + "-" + String.format("%02d", day);
       // etDob.setText(date);

    }

    public interface DatePickerFragmentListener{
        public void onDateSet(int year, int month, int day);
    }

    public void onDateSetListener(DatePickerFragmentListener listener){
        this.listener = listener;
    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        listener = (DatePickerFragmentListener) context;
//    }

}