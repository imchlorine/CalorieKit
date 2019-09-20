package com.maclee.calorie.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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


public class LOAFragment extends Fragment implements Validator.ValidationListener{


    private OnFragmentInteractionListener mListener;
    Button btnNext;
    @Checked(message = "Please select your level of activity.")
    RadioGroup rgLoa;
    @NotEmpty(message = "Please enter your steps per mile.")
    EditText etSpm;
    Validator validator;
    AddressFragment addressFragment;
    String selectedLevel;
    SharedPreferences settings;
    public LOAFragment() {
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
        //container.removeAllViews(); // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loa, container, false);
        settings = getActivity().getSharedPreferences(REGISTERINFOR, 0); // 0 - for private mode
        validator = new Validator(this);
        validator.setValidationListener(this);
        btnNext = view.findViewById(R.id.btn_next_loa);
        addressFragment = new AddressFragment();
        etSpm = view.findViewById(R.id.et_spm);
        rgLoa = view.findViewById(R.id.rg_loa);
        etSpm.setText(String.valueOf(settings.getInt("spm",0)));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rgLoa.getCheckedRadioButtonId();
                //radioButton = (RadioButton)view.findViewById(selectedId);
                if (selectedId == view.findViewById(R.id.radio_level1).getId()){
                    selectedLevel = "1";
                }else if (selectedId == view.findViewById(R.id.radio_level2).getId()){
                    selectedLevel = "2";
                }else if (selectedId == view.findViewById(R.id.radio_level3).getId()){
                    selectedLevel = "3";
                }else if (selectedId == view.findViewById(R.id.radio_level4).getId()){
                    selectedLevel = "4";
                }else if (selectedId == view.findViewById(R.id.radio_level5).getId()){
                    selectedLevel = "5";
                }
                validator.validate();
            }
        });
        //btnNext.setEnabled(false);
        return view;
    }

    @Override
    public void onValidationSucceeded() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("loa",selectedLevel);
        editor.putInt("spm", Integer.parseInt(etSpm.getText().toString()));
        editor.commit();
        mListener.goToFragment(addressFragment,true,"AddressFragment");
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

    public interface OnFragmentInteractionListener {
        void goToFragment(Fragment fragment, boolean isTop, String TAG);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
