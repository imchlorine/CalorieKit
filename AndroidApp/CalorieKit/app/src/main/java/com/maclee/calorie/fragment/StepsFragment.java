package com.maclee.calorie.fragment;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maclee.calorie.MainActivity;
import com.maclee.calorie.R;
import com.maclee.calorie.adapter.StepsAdapter;
import com.maclee.calorie.api.Toolkit;
import com.maclee.calorie.database.StepsDB;
import com.maclee.calorie.model.Steps;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StepsFragment extends Fragment {

    StepsDB sdb;
    StepsAdapter adapter;
    ListView stepsList;

    public StepsFragment() {
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
        final View view =  inflater.inflate(R.layout.fragment_steps, container, false);
        sdb = Room.databaseBuilder(getActivity().getApplicationContext(),
                StepsDB.class, "steps_db")
                .fallbackToDestructiveMigration()
                .build();
        adapter = new StepsAdapter(getContext());
        FloatingActionButton btnDelete = view.findViewById(R.id.btn_delete_steps);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                //builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Are you sure to delete All?");
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteAllStep().execute();
                    }
                });
                builderSingle.show();
            }
        });

        stepsList = view.findViewById(R.id.list_steps);
        stepsList.setAdapter(adapter);


        stepsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                //builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Change your steps");
                final EditText inputSteps = new EditText(getContext());
                inputSteps.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputSteps.setText(String.valueOf(adapter.getItem(position).getSteps()));
                builderSingle.setView(inputSteps);
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int steps = 0;
                        if (!inputSteps.getText().toString().equals("")){
                            steps = Integer.parseInt(inputSteps.getText().toString());
                        }
                        adapter.getItem(position).setSteps(steps);
                        Object dataTransfer[] = new Object[1];
                        dataTransfer[0] = adapter.getItem(position);
                        new EditSteps().execute(dataTransfer);
                    }
                });
                builderSingle.show();
                //Toast.makeText(getContext(), String.valueOf(adapter.getItem(position).getSteps()),Toast.LENGTH_LONG).show();
            }
        });

        stepsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                //builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Are you sure to delete?");
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Object dataTransfer[] = new Object[1];
                        dataTransfer[0] = adapter.getItem(position);
                        new DeleteStep().execute(dataTransfer);
                    }
                });
                builderSingle.show();
                return true;

            }
        });


        FloatingActionButton fab = view.findViewById(R.id.fab_add_step);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                //builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Add your steps");
                final EditText inputSteps = new EditText(getContext());
                inputSteps.setInputType(InputType.TYPE_CLASS_NUMBER);
                builderSingle.setView(inputSteps);
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int step  = Integer.parseInt(inputSteps.getText().toString());
                        Steps steps = new Steps();
                        steps.setSteps(step);
                        steps.setAddTime(Toolkit.getCurrentDataTime());
                        Object dataTransfer[] = new Object[1];
                        dataTransfer[0] = steps;
                        new AddStep().execute(dataTransfer);
                    }
                });
                builderSingle.show();

            }
        });

        new GetSteps().execute();

        return view;
    }

    public class GetSteps extends AsyncTask<Object,String,List<Steps>> {


        @Override
        protected List<Steps> doInBackground(Object... objects) {
            List<Steps> steps = sdb.stepsDao().getAll();

            Collections.reverse(steps);
            return steps;

        }

        @Override
        protected void onPostExecute(List<Steps> steps) {
            super.onPostExecute(steps);
            resetDataSource(steps);
        }
    }

    public class AddStep extends AsyncTask<Object,String,String> {


        @Override
        protected String doInBackground(Object... objects) {
            Steps steps = (Steps) objects[0];
            sdb.stepsDao().insertAll(steps);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            reload();
        }
    }

    public class DeleteAllStep extends AsyncTask<Object,String,String> {


        @Override
        protected String doInBackground(Object... objects) {
            sdb.stepsDao().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            reload();
        }
    }

    public void resetDataSource(List<Steps> source) {
        adapter.getData().clear();
        adapter.getData().addAll(source);
        adapter.notifyDataSetChanged();
        stepsList.setSelection(0);
    }

    public class DeleteStep extends AsyncTask<Object,String,String> {
        @Override
        protected String doInBackground(Object... objects) {
            sdb.stepsDao().delete((Steps) objects[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            reload();
        }
    }

    public class EditSteps extends AsyncTask<Object, String, String>{

        @Override
        protected String doInBackground(Object... objects) {
            sdb.stepsDao().updateUsers((Steps) objects[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            reload();
        }
    }

    public void reload(){
        // Reload current fragment
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("StepsFragment");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }


}
