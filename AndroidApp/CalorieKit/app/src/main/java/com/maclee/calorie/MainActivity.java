package com.maclee.calorie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.maclee.calorie.api.Toolkit;
import com.maclee.calorie.fragment.AddFoodFragment;
import com.maclee.calorie.fragment.AddressFragment;
import com.maclee.calorie.fragment.DashboardFragment;
import com.maclee.calorie.fragment.MapFragment;
import com.maclee.calorie.fragment.MyDietFragment;
import com.maclee.calorie.fragment.ReportFragment;
import com.maclee.calorie.fragment.StepsFragment;
import com.maclee.calorie.service.UpdateToServerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    MyDietFragment myDietFragment;
    StepsFragment stepsFragment;
    MapFragment mapFragment;
    ReportFragment reportFragment;
    DashboardFragment dashboardFragment;

    TextView headerEmail;
    TextView headerName;
    public static final String PREFS_NAME = "MyInfo";
    public static JSONObject infoObj;

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorBackground));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setTitle("Calorie Tracker");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        headerEmail = header.findViewById(R.id.tv_header_email);
        headerName = header.findViewById(R.id.header_name);
        myDietFragment = new MyDietFragment();
        mapFragment = new MapFragment();
        reportFragment = new ReportFragment();
        stepsFragment = new StepsFragment();
        dashboardFragment = new DashboardFragment();
        if (savedInstanceState == null) {
            goToFragment(dashboardFragment, true,"DashboardFragment");
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        infoObj = Toolkit.getUserInfor(settings);
        try {
            headerEmail.setText(infoObj.getString("email"));
            headerName.setText(infoObj.getString("firstname") + " " + infoObj.getString("lastname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, UpdateToServerService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE,35);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            //return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            setToolbar("Calorie Tracker",Color.GRAY,R.color.colorTitle);
            goToFragment(dashboardFragment, false,"DashboardFragment");
            // Handle the camera action
        } else if (id == R.id.nav_diet) {
            setToolbar("My Diet",Color.WHITE,R.color.colorBackground);
            goToFragment(myDietFragment, false,"MyDietFragment");
        } else if (id == R.id.nav_steps) {
            setToolbar("Steps",Color.WHITE,R.color.colorBackground);
            goToFragment(stepsFragment, false,"StepsFragment");
        } else if (id == R.id.nav_report) {
            setToolbar("Report",Color.WHITE,R.color.colorBackground);
            goToFragment(reportFragment, false,"ReportFragment");

        } else if (id == R.id.nav_map) {
            setToolbar("Map",Color.WHITE,R.color.colorBackground);
            goToFragment(mapFragment, false,"MapFragment");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       // drawer.closeDrawer(GravityCompat.START);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawers();
            }
        });
        return true;
    }



    public void setToolbar(String title, int titleColor, int backgroundColor ){
        toolbar.setTitle(title);
       // toolbar.setTitleTextColor(titleColor);
        //toggle.getDrawerArrowDrawable().setColor(titleColor);
        //toolbar.setBackgroundColor(getResources().getColor(backgroundColor));

    }

    void goToFragment(Fragment fragment, boolean isTop, String TAG) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, TAG);
        // fragmentTransaction.add(fragment,"Bookmark");
        if (!isTop) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }


    // Dismiss keyboard when click outside of EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
            v.clearFocus();
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
