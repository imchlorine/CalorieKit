package com.maclee.calorie.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.maclee.calorie.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import java.util.List;

import static com.maclee.calorie.RegisteActivity.REGISTERINFOR;

public class WelcomeFragement extends Fragment implements Validator.ValidationListener, DatePickerFragment.DatePickerFragmentListener{



    private OnFragmentInteractionListener mListener;

    @NotEmpty(message = "Please enter your first name.")
    EditText etFirstName;
    @NotEmpty(message = "Please enter your last name.")
    EditText etLastName;
    @NotEmpty(message = "Please enter your height.")
    EditText etHeight;
    @NotEmpty(message = "Please enter your weight.")
    EditText etWeight;

    @NotEmpty(message = "Please enter your birth date.")
    static EditText etDob;
    @Checked(message = "Please select your gender.")
    RadioGroup rgGender;
    RadioButton radioButton;
    Validator validator;
    Button btnNext;
    LOAFragment loaFragment;
    SharedPreferences settings;
    public WelcomeFragement() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_fragement, container, false);
        settings = getActivity().getSharedPreferences(REGISTERINFOR, 0);
        loaFragment = new LOAFragment();
        validator = new Validator(this);
        validator.setValidationListener(this);
        btnNext = view.findViewById(R.id.btn_next_welcome);
        etDob = view.findViewById(R.id.et_dob_info);
        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        rgGender = view.findViewById(R.id.rg_sex);
        etHeight = view.findViewById(R.id.et_height);
        etWeight = view.findViewById(R.id.et_weight);
         etFirstName.setText(settings.getString("firstname",""));
         etLastName.setText(settings.getString("lastname",""));
         etDob.setText(settings.getString("birthdate",""));
         etHeight.setText(settings.getString("height",""));
         etWeight.setText(settings.getString("weight",""));

        //btnNext.setEnabled(false);
        //btnNext.setBackgroundResource(R.drawable.button_rectangle_disable);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rgGender.getCheckedRadioButtonId();
                radioButton = (RadioButton)view.findViewById(selectedId);
                validator.validate();

            }
        });
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        etDob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog(v);
                }
            }
        });
        return view;
    }

    @Override
    public void onValidationSucceeded() {
         // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("firstname",etFirstName.getText().toString());
        editor.putString("lastname",etLastName.getText().toString());
        editor.putString("gender",radioButton.getText().toString());
        editor.putString("birthdate",etDob.getText().toString());
        editor.putString("height",etHeight.getText().toString());
        editor.putString("weight",etWeight.getText().toString());
        editor.commit();
        mListener.goToFragment(loaFragment,true,"LOAFragemnt");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onDateSet(int year, int month, int day) {
         String date = String.format("%02d", year) + "-" +  String.format("%02d", month + 1) + "-" + String.format("%02d", day);
         etDob.setText(date);
    }

    public interface OnFragmentInteractionListener {
        void goToFragment(Fragment fragment, boolean isTop, String TAG);
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.onDateSetListener(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }



    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }




}


