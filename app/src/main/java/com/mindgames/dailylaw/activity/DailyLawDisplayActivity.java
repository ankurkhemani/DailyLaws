package com.mindgames.dailylaw.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.activeandroid.ActiveAndroid;
import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.adapter.IPCListAdapter;
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

    private ListView listView;
    private int type;

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


        //get type and name of Daily Law
        type = getIntent().getIntExtra("type", 1);
        final String name = getIntent().getStringExtra("name");

        //font change
        SpannableString s = new SpannableString(name);
        s.setSpan(new TypeFaceSpan(DailyLawDisplayActivity.this, "alpha_echo.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        handleType(type);

    }

    private void handleType(int type){

        listView = (ListView) findViewById( R.id.listView );
        FAQList = new ArrayList<FAQ>();
        final ArrayList<String> listItems= new ArrayList<String>();
        FAQList = FAQ.getFAQs(type);

        switch(type){

            case 1:
                format.setVisibility(View.VISIBLE);
                for(FAQ row : FAQList)
                    listItems.add(row.Question);
                if(FAQList.size()==0)
                    listItems.add("Nothing to display .. ");


                // Create ArrayAdapter
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>
                        (DailyLawDisplayActivity.this, R.layout.list_item, listItems);
                // Set the ArrayAdapter as the ListView's adapter.
                listView.setAdapter( listAdapter );

                break;
            case 2:
                relativeLayout.setVisibility(View.VISIBLE);
                // Spinner Drop down elements
                List<String> categories = new ArrayList<String>();
                categories.add(" Protection Of Women From Domestic Violence Act, 2005");
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

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                break;
            case 3:
                format.setVisibility(View.VISIBLE);
                break;

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog alertDialog = new Dialog(DailyLawDisplayActivity.this);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                alertDialog.setContentView(R.layout.display);
                Button done = (Button)alertDialog.findViewById(R.id.done);
                TextView dialogTitle = (TextView)alertDialog.findViewById(R.id.title);
                TextView txtView = (TextView)alertDialog.findViewById(R.id.display);

//                        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),
//                                "fonts/alpha_echo.");
//                        dialogTitle.setTypeface(typeface);
                txtView.setTypeface(Typeface.SERIF);

                String title = listItems.get(position);
                String message = FAQList.get(position).Answer;

                txtView.setText(message);

                final ToggleButton bookmark = (ToggleButton) alertDialog.findViewById(R.id.bookmark);
                bookmark.setVisibility(View.GONE);

                dialogTitle.setText(title);
                alertDialog.show();

                done.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // Log the user out
                        alertDialog.dismiss();
                    }
                });

            }
        });

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
}
