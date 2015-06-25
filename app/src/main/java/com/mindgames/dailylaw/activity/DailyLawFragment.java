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
        // showing pdf
        CopyReadAssets("FIR.pdf");
    }

    @InjectView(R.id.button2) Button button2;
    @OnClick(R.id.button2) public void WOMEN() {
        // showing pdf
        CopyReadAssets("Women.pdf");
    }

    @InjectView(R.id.button3) Button button3;
    @OnClick(R.id.button3) public void sayHello() {
        // showing pdf
        CopyReadAssets("Consumer.pdf");
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
        YoYo.with(Techniques.Tada)
                .duration(1000)
                .playOn(button1);
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

    private void CopyReadAssets(String filename)
    {
        AssetManager assetManager = getActivity().getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getActivity().getFilesDir(), filename);
        try
        {
            in = assetManager.open(filename);
            out = getActivity().openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getActivity().getFilesDir() + "/" + filename),
                "application/pdf");

        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(),
                    "No Application Available to View PDF",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }
}
