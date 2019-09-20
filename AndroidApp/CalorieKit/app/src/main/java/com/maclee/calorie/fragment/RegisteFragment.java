package com.maclee.calorie.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maclee.calorie.BuildConfig;
import com.maclee.calorie.R;
import com.maclee.calorie.api.HttpUrlHelper;
import com.maclee.calorie.api.Toolkit;
import com.maclee.calorie.model.User;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.maclee.calorie.RegisteActivity.REGISTERINFOR;

public class RegisteFragment extends Fragment implements Validator.ValidationListener{


    private OnFragmentInteractionListener mListener;
    final String url_Register = BuildConfig.SERVER_URL + "calorieuser/register";
    final String url_GetUserId = BuildConfig.SERVER_URL + "calorieuser/findByEmail/";
    final String url_CreateAccount = BuildConfig.SERVER_URL + "credential/createCredential";
    final String url_DeleteUser = BuildConfig.SERVER_URL + "calorieuser/remove/";
    Button btnRegister;
    @NotEmpty(message = "Please enter your user name.")
    @Length(min = 5, max = 15, message = "Enter at least 5 but no more than 15 characters.")
    @Pattern(regex = "^[A-Za-z0-9]+", message = "Should contain only alphabets and number.")
    EditText etUsername;
    @NotEmpty(message = "Please enter you email address.")
    @Email
    EditText etEmail;
    @NotEmpty(message = "Please enter your password.")
    EditText etPassword;
    Validator validator;
    SharedPreferences settings;

    public RegisteFragment() {
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
       // container.removeAllViews(); // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_registe, container, false);
        validator = new Validator(this);
        validator.setValidationListener(this);
        btnRegister = view.findViewById(R.id.btn_finish_register);
        etUsername = view.findViewById(R.id.et_username);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);

        //btnRegister.setEnabled(false);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        return view;
    }

    @Override
    public void onValidationSucceeded() {
        settings = getActivity().getSharedPreferences(REGISTERINFOR, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username",etUsername.getText().toString());
        editor.putString("email",etEmail.getText().toString());
        editor.putString("password",Toolkit.md5(etPassword.getText().toString()));
        editor.commit();
        register();

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

    public class RegisterUser extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... objects) {
            Gson gson = new Gson();
            String jsonString  = gson.toJson(objects[0]);
            Log.d("DTAG", "date: " + jsonString);
            String data = new HttpUrlHelper().getHttpUrlPostConnection(url_Register, jsonString);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == "ok"){
                Object dataTransfer[] = new Object[1];
                String email = settings.getString("email","");
                dataTransfer[0] = url_GetUserId + email;
                new GetUserIdByEmail().execute(dataTransfer);
            } else if (s == "found"){
                Toast.makeText(getContext(),"Exist email! ",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(),"Error!",Toast.LENGTH_LONG).show();
            }
        }
    }

    public class GetUserIdByEmail extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... objects) {
            String url = (String) objects[0];
            String data = new HttpUrlHelper().getHttpUrlConnection(url);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONArray resultArray = null;
            JSONObject dataObj = null;
            try {
                resultArray = new JSONArray(s);
                dataObj = resultArray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            s = dataObj.toString();
            Log.e("UserResult",s);
            String password = settings.getString("password","");
            String username = settings.getString("username","");
            Object dataTransferCredential[] = new Object[4];
            dataTransferCredential[0] = username;
            dataTransferCredential[1] = password;
            dataTransferCredential[2] = s;
            dataTransferCredential[3] = Toolkit.getCurrentDataTime();
            new CreateCredential().execute(dataTransferCredential);
        }
    }

    public class CreateCredential extends AsyncTask<Object,String,String>{

        @Override
        protected String doInBackground(Object... objects) {
            String username = (String) objects[0];
            String password = (String) objects[1];
            String userid = (String) objects[2];
            String signupdate = (String) objects[3];
            String jsonString = "{\"username\": \"" + username
                    + "\"" + "," + "\"password\": \"" + password
                    + "\"" + "," + "\"userid\":" + userid
                    + "," + "\"signupdate\": \"" + signupdate + "\"}";
            Log.w("JsonString", jsonString);
            String data = new HttpUrlHelper().getHttpUrlPostConnection(url_CreateAccount, jsonString);
            return data;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == "ok"){
                Toast.makeText(getContext(),
                        "Register Success!", Toast.LENGTH_LONG).show();
                getActivity().finish();
            } else if (s == "found"){
                Toast.makeText(getContext(),"Exist user name! ",Toast.LENGTH_LONG).show();
                Object dataTransfer[] = new Object[1];
                dataTransfer[0] = url_DeleteUser + etEmail.getText().toString();
                new DeleteRegisterUserExist().execute(dataTransfer);
            }else {
                Toast.makeText(getContext(),"Error!",Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DeleteRegisterUserExist extends AsyncTask<Object,String,String>{


        @Override
        protected String doInBackground(Object... objects) {
            int code = new HttpUrlHelper().getHttpUrlDeleteConnection((String) objects[0]);
            return String.valueOf(code);
        }
    }

    public void register(){
        String firtname = settings.getString("firstname","");
        String lastname = settings.getString("lastname","");
        String email = settings.getString("email","");
        String birthdate = Toolkit.addTimePattern(settings.getString("birthdate","")) ;
        char gender = settings.getString("gender","").charAt(0);
        Double height = Double.parseDouble(settings.getString("height","0")) ;
        Double weight = Double.parseDouble(settings.getString("weight","0")) ;
        String loa = settings.getString("loa","");
        int spm = settings.getInt("spm",0);
        StringBuilder sb = new StringBuilder();
        if (!settings.getString("address","").equals("")){
            sb.append(settings.getString("address","") + ", ");
        }
        if (!settings.getString("city","").equals("")){
            sb.append(settings.getString("city","") + ", ");
        }
        if (!settings.getString("state","").equals("")){
            sb.append(settings.getString("state","") + ", ");
        }
        sb.append(settings.getString("country",""));
        String address = sb.toString();
        String postcode = settings.getString("postcode","");

        User user = new User(firtname,lastname, email, birthdate,height,
                weight,gender,address,postcode,loa,spm);
            new RegisterUser().execute(user);
    }



}
