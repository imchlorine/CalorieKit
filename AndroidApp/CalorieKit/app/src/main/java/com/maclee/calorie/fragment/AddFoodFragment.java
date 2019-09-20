package com.maclee.calorie.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maclee.calorie.MainActivity;
import com.maclee.calorie.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFoodFragment extends Fragment {


    Toolbar toolbar;
    public AddFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_food, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Add Consume");
        setupToolbar();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void setupToolbar(){

      // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //set back to last level
       // getActivity().getActionBar().setDisplayShowHomeEnabled(true);
       // getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
