package com.mindgames.dailylaw.activity;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mindgames.dailylaw.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DailyLawFragment extends Fragment {

    @InjectView(R.id.button1) Button button1;
    @OnClick(R.id.button1) public void FIR() {
        Intent myIntent = new Intent(getActivity(), DailyLawDisplayActivity.class);
        myIntent.putExtra("type", 1);
        myIntent.putExtra("name", "Daily Law - FIR");

        this.startActivity(myIntent);
    }

    @InjectView(R.id.button2) Button button2;
    @OnClick(R.id.button2) public void WOMEN() {
        Intent myIntent = new Intent(getActivity(), DailyLawDisplayActivity.class);
        myIntent.putExtra("type", 2);
        myIntent.putExtra("name", "Daily Law - Women");

        this.startActivity(myIntent);
    }

    @InjectView(R.id.button3) Button button4;
    @OnClick(R.id.button3) public void CONSUMER() {
        Intent myIntent = new Intent(getActivity(), DailyLawDisplayActivity.class);
        myIntent.putExtra("type", 3);
        myIntent.putExtra("name", "Daily Law - Consumer");

        this.startActivity(myIntent);
    }

    @InjectView(R.id.button4) Button button3;
    @OnClick(R.id.button4) public void OTHER() {
        Intent myIntent = new Intent(getActivity(), DailyLawDisplayActivity.class);
        myIntent.putExtra("type", 4);
        myIntent.putExtra("name", "Daily Law - Other");

        this.startActivity(myIntent);
    }

    public DailyLawFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dailylaw, container, false);

        ButterKnife.inject(this, rootView);
//        YoYo.with(Techniques.Tada)
//                .duration(1000)
//                .playOn(button1);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
