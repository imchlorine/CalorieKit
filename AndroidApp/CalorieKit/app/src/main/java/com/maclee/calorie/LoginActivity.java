package com.maclee.calorie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.maclee.calorie.api.HttpUrlHelper;
import com.maclee.calorie.api.Toolkit;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {


    public static final String PREFS_NAME = "MyInfo";

    @Order(1)
    @Length(min = 5, max = 15, message = "Enter at least 5 but no more than 15 characters.")
    @Pattern(regex = "^[A-Za-z0-9]+", message = "Should contain only alphabets and number")
    EditText etUsername;
    @Order(2)
    @NotEmpty(message = "Please enter your password.")
    EditText etPassword;
    TextView tvRegister;
    Button btnLogin;
    Validator validator;
    SharedPreferences settings;

    final String url_login = BuildConfig.SERVER_URL + "credential/authenticate";
    final String url_userInfo = BuildConfig.SERVER_URL + "credential/findByUserName/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(Color.rgb(241,241,243));
        settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
        if (hasLoggedIn) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);//Go directly to main activity.
            LoginActivity.this.finish();
        }

        etUsername = (EditText) findViewById(R.id.tv_email);
        etPassword = (EditText) findViewById(R.id.tv_password);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        btnLogin = (Button) findViewById(R.id.btn_login);
        validator = new Validator(this);
        validator.setValidationListener(this);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisteActivity.class);
                startActivity(i);
                overridePendingTransition(
                        R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

    }

    @Override
    public void onValidationSucceeded() {
        login();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }

    public class LoginUser extends AsyncTask<Object, String, String> {


        @Override
        protected String doInBackground(Object... objects) {
            String username = (String) objects[0];
            String password = (String) objects[1];
            String jsonString = "{\"username\": \"" + username + "\"" + "," + "\"password\": \"" + password + "\"}";
            Log.w("JsonString", jsonString);
            String data = new HttpUrlHelper().getHttpUrlPostConnection(url_login, jsonString);
            return data;
        }
    }

    public class GetUserInfor extends AsyncTask<Object, String, String> {


        @Override
        protected String doInBackground(Object... objects) {
            String url = (String) objects[0];
            String data = new HttpUrlHelper().getHttpUrlConnection(url);
            return data;
        }
    }


    public void login(){
        String username = etUsername.getText().toString();
        String password = Toolkit.md5(etPassword.getText().toString());
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = username;
        dataTransfer[1] = password;
        try {
            String result = new LoginUser().execute(dataTransfer).get();
            if (result == "ok") {
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                Object dataTransferUser[] = new Object[1];
                dataTransferUser[0] = url_userInfo + username;
                String userInfo = new GetUserInfor().execute(dataTransferUser).get();
                editor.putString("userInfo", userInfo);
                //Set "hasLoggedIn" to true
                editor.putBoolean("hasLoggedIn", true);
                // Commit the edits!
                editor.commit();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                LoginActivity.this.finish();
           } else {
                Toast.makeText(LoginActivity.this,
                        url_login, Toast.LENGTH_LONG).show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

