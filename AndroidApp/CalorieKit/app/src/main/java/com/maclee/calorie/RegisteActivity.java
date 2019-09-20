package com.maclee.calorie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.maclee.calorie.fragment.AddressFragment;
import com.maclee.calorie.fragment.LOAFragment;
import com.maclee.calorie.fragment.RegisteFragment;
import com.maclee.calorie.fragment.WelcomeFragement;

public class RegisteActivity extends AppCompatActivity implements WelcomeFragement.OnFragmentInteractionListener,
        LOAFragment.OnFragmentInteractionListener,
        AddressFragment.OnFragmentInteractionListener, RegisteFragment.OnFragmentInteractionListener {

    public static final String REGISTERINFOR = "RegisterInfo";
    WelcomeFragement welcomeFragement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorBackground));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign Up");
        setSupportActionBar(toolbar);
        //set back to last level
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        welcomeFragement = new WelcomeFragement();
        if (savedInstanceState == null) {
            setTitle("User Info");
            goToFragment(welcomeFragement, false,"WelcomeFragement");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //click on back arrow
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(
                        R.anim.trans_right_in, R.anim.trans_right_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void goToFragment(Fragment fragment, boolean isTop, String TAG) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.trans_left_in,R.anim.trans_left_out,R.anim.trans_right_in,R.anim.trans_right_out);
        fragmentTransaction.replace(R.id.main_frame, fragment, TAG);
        if (isTop) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

}

