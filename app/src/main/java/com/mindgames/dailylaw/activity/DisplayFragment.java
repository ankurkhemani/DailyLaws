package com.mindgames.dailylaw.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.model.LawBook;


public class DisplayFragment extends Fragment {


    public DisplayFragment() {
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
        final View rootView = inflater.inflate(R.layout.display, container, false);
        String message = getArguments().getString("message");
        final int id = getArguments().getInt("Id");
        final int bm = getArguments().getInt("bookmark");


        final TextView txtView = (TextView) rootView.findViewById(R.id.display);
        txtView.setText(message);

        final ToggleButton bookmark = (ToggleButton) rootView.findViewById(R.id.bookmark);
        if(bm==0) {
            bookmark.setTextOff("Add to Favorites");
            bookmark.setChecked(false);
        }
        else {
            bookmark.setTextOn("Remove from Favorites");
            bookmark.setChecked(true);
        }

         bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked) { // toggle is on
                     LawBook.updateBookmarks(id, 1);
                     bookmark.setTextOn("Remove from Favorites");
                 } else { // toggle is off
                     LawBook.updateBookmarks(id, 0);
                     bookmark.setTextOff("Add to Favorites");
                 }
             }
         });

        return rootView;

    }
}
