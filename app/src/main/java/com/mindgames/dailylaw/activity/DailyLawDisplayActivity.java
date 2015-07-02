package com.mindgames.dailylaw.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.activeandroid.ActiveAndroid;
import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.adapter.ExpandableListAdapter;
import com.mindgames.dailylaw.external.AnimatedExpandableListView;
import com.mindgames.dailylaw.external.TypeFaceSpan;
import com.mindgames.dailylaw.model.FAQ;
import com.mindgames.dailylaw.model.LawBook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DailyLawDisplayActivity extends ActionBarActivity {

    List<FAQ> FAQList;
    @InjectView(R.id.spinner) Spinner spinner;
    @InjectView(R.id.format)  Button format;
    @InjectView(R.id.relativeLayout) RelativeLayout relativeLayout;

    private int type;
    ExpandableListAdapter listAdapter;
    AnimatedExpandableListView expListView;
    HashMap<Integer, List<FAQ>> Map;
    int spinnerPosition =0;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;


    @OnClick(R.id.format) public void WOMEN() {
        // showing pdf
        switch(type) {
            case 1:
                CopyReadAssets("ConsumerFormat.pdf");
                break;
            case 3:
                CopyReadAssets("ConsumerFormat.pdf");
                break;
        }
    }

    public DailyLawDisplayActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailylaw_display);

        //Gesture Detection
        gestureDetection();

        //initialize ActiveAndroid
        ActiveAndroid.initialize(this);

        ButterKnife.inject(this);


        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        // get the action bar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // Enabling Back navigation on Action Bar icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.Black_transparent_black_percent_70));

        //get type and name of Daily Law
        type = getIntent().getIntExtra("type", 1);
        final String name = getIntent().getStringExtra("name");

        //font change
        SpannableString s = new SpannableString(name);
        s.setSpan(new TypeFaceSpan(DailyLawDisplayActivity.this, "alpha_echo.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        // set the view according to type of Daily Law
        handleType(type);

    }

    private void handleType(int type){
        // get the listview
        expListView = (AnimatedExpandableListView) findViewById(R.id.lvExp);
        expListView.setClickable(false);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (expListView.isGroupExpanded(groupPosition))
                    expListView.collapseGroupWithAnimation(groupPosition);
                else
                    expListView.expandGroupWithAnimation(groupPosition);
                return true;
            }
        });

        switch(type){

            case 1:
                format.setVisibility(View.VISIBLE);
                format.setText("Format");
                prepareListData(0);
                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            case 2:
                relativeLayout.setVisibility(View.VISIBLE);
                // Spinner Drop down elements
                List<String> categories = new ArrayList<String>();
                categories.add("Protection Of Women From Domestic Violence Act, 2005");
                categories.add("Dowry Prohibition Act, 1961");
                categories.add("The Sexual Harassment Of Women At Workplace Act, 2013");
                categories.add("Arrest Of Women");

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (this, R.layout.spinner_item, categories);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        prepareListData(position+1);

                        listAdapter = new ExpandableListAdapter(DailyLawDisplayActivity.this, listDataHeader, listDataChild);
                        // setting list adapter
                        expListView.setAdapter(listAdapter);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                break;
            case 3:
                format.setVisibility(View.VISIBLE);
                format.setText("Format");
                prepareListData(0);
                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
        }

    }

    /*
     * Preparing the list data
     */
    private void prepareListData(int category) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        FAQList = new ArrayList<FAQ>();
        FAQList = FAQ.getFAQs(type, category);

        HashMap<Integer, List<String>> chapMap = new HashMap<Integer, List<String>>();

        // Adding child data
        List<String> childData = new ArrayList<String>();



        for(int i=0; i<FAQList.size(); i++){
            listDataHeader.add(FAQList.get(i).Question);
            childData.add(FAQList.get(i).Answer);
        }


        for(int i=0; i<FAQList.size(); i++){
            listDataChild.put(listDataHeader.get(i), childData.subList(i,i+1)); // Header, Child data
        }


    }



    private void CopyReadAssets(String filename)
    {
        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(this.getFilesDir(), filename);
        try
        {
            in = assetManager.open(filename);
            out = this.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

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
                Uri.parse("file://" + this.getFilesDir() + "/" + filename),
                "application/pdf");

        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(DailyLawDisplayActivity.this,
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent myIntent = new Intent(DailyLawDisplayActivity.this, SearchResultsActivity.class);
                this.startActivity(myIntent);
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Intent myIntent = new Intent(DailyLawDisplayActivity.this, SearchResultsActivity.class);
                    DailyLawDisplayActivity.this.startActivity(myIntent);
                }
                // Left to Right swipe
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onBackPressed();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    private void gestureDetection(){
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        //Get the entire view and set gesture listener
        View v = (View) findViewById(R.id.rootView);
        v.setOnTouchListener(gestureListener);
    }
}
