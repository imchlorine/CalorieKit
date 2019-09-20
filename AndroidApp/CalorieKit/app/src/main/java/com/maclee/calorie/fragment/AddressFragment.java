package com.maclee.calorie.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.maclee.calorie.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import static com.maclee.calorie.RegisteActivity.REGISTERINFOR;

public class AddressFragment extends Fragment implements Validator.ValidationListener{


    private OnFragmentInteractionListener mListener;
    Button btnNext;
    EditText etAddress;
    EditText etCity;
    EditText etState;
    @NotEmpty(message = "Please enter your country.")
    EditText etCountry;
    @NotEmpty(message = "Please enter your postcode.")
    EditText etPostCode;
    RegisteFragment registeFragment;
    Validator validator;
    SharedPreferences settings;
    public AddressFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        settings = getActivity().getSharedPreferences(REGISTERINFOR, 0); // 0 - for private mode

        validator = new Validator(this);
        validator.setValidationListener(this);
        btnNext = view.findViewById(R.id.btn_next_address);
        etAddress = view.findViewById(R.id.et_address);
        etCity = view.findViewById(R.id.et_city);
        etState = view.findViewById(R.id.et_state);
        etPostCode = view.findViewById(R.id.et_postcode);
        etCountry = view.findViewById(R.id.et_country);
        etAddress.setText(settings.getString("address",""));
        etCity.setText(settings.getString("city",""));
        etState.setText(settings.getString("state",""));
        etCountry.setText(settings.getString("country",""));
        etPostCode.setText(settings.getString("postcode",""));

        registeFragment  = new RegisteFragment();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        //btnNext.setEnabled(false);
        return view;
    }

    @Override
    public void onValidationSucceeded() {
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("address",etAddress.getText().toString());
        editor.putString("city",etCity.getText().toString());
        editor.putString("state",etState.getText().toString());
        editor.putString("country",etCountry.getText().toString());
        editor.putString("postcode", etPostCode.getText().toString());
        editor.commit();
        mListener.goToFragment(registeFragment,true,"RegisteFragment");
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
