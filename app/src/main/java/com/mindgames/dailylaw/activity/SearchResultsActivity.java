package com.mindgames.dailylaw.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.external.TypeFaceSpan;
import com.mindgames.dailylaw.model.LawBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import me.drakeet.materialdialog.MaterialDialog;


public class SearchResultsActivity extends ActionBarActivity {

    private TextView txtQuery;
    private ListView listView ;

    List<LawBook> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        // get the action bar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // Enabling Back navigation on Action Bar icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        //font change
        SpannableString s = new SpannableString("Search");
        s.setSpan(new TypeFaceSpan(SearchResultsActivity.this, "alpha_echo.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        txtQuery = (TextView) findViewById(R.id.txtQuery);

        searchList = new ArrayList<LawBook>();


        // Find the ListView resource.
        listView = (ListView) findViewById( R.id.listView );

        //inititally listview is invisible
        listView.setVisibility(View.GONE);
        txtQuery.setText("Please enter a section number that you want to search ..");

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
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        //searchView.setQueryHint("Enter section number .. ");
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setFocusable(true);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocusFromTouch();
        searchView.hasFocus();


        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        searchViewItem.expandActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.isEmpty()) {
                    txtQuery.setText("Please enter a section number that you want to search ..");
                    listView.setVisibility(View.GONE);
                }
                else {
                    txtQuery.setText("Showing Results for Section " + newText + " in the entire Law Book");
                    listView.setVisibility(View.VISIBLE);
                }
                searchList = LawBook.searchQuery(newText);

                ArrayList<String> listItems= new ArrayList<String>();

                if(searchList.size()==0 && !newText.isEmpty())
                    listItems.add("No matching section number found .. ");

                for(LawBook row : searchList)
                {
                    switch (row.Type) {
                        case 0:
                            listItems.add("Section " + row.SectionNumber + " in " + "IPC");
                            break;
                        case 1:
                            listItems.add("Section " + row.SectionNumber + " in " + "CrPC");
                            break;
                        case 2:
                            listItems.add("Section " + row.SectionNumber + " in " + "CPC");
                            break;
                        case 3:
                            listItems.add("Section " + row.SectionNumber + " in " + "Evidence");
                            break;
                        case 4:
                            listItems.add("Article " + row.SectionNumber + " in " + "Constitution");
                            break;
                    }
                }

                // Create ArrayAdapter
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(SearchResultsActivity.this, R.layout.list_item, listItems);

                // Set the ArrayAdapter as the ListView's adapter.
                listView.setAdapter( listAdapter );

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        final Dialog alertDialog = new Dialog(SearchResultsActivity.this);
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        alertDialog.setContentView(R.layout.display);
                        Button done = (Button)alertDialog.findViewById(R.id.done);
                        TextView dialogTitle = (TextView)alertDialog.findViewById(R.id.title);
                        TextView txtView = (TextView)alertDialog.findViewById(R.id.display);

//                        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),
//                                "fonts/alpha_echo.");
//                        dialogTitle.setTypeface(typeface);
                        txtView.setTypeface(Typeface.SERIF);

                        String message1;
                        if (searchList.get(position).Type != 4)
                            message1 = "Section " + searchList.get(position).SectionNumber + " - " + searchList.get(position).SectionDisplay;
                        else
                            message1 = "Part " + searchList.get(position).SectionNumber + " - " + searchList.get(position).SectionDisplay;

                        txtView.setText(message1);

                        final ToggleButton bookmark = (ToggleButton) alertDialog.findViewById(R.id.bookmark);
                        final int bm = searchList.get(position).Bookmark;
                        final int Id = searchList.get(position).Id;

                        String title = "";

                        switch (searchList.get(position).Type) {
                            case 0:
                                title = "Chapter " + searchList.get(position).ChapterNumber.IPCChapterDenotion +
                                        " - " + searchList.get(position).ChapterNumber.IPCChapterDescription;

                                break;
                            case 1:
                                title = "Chapter " + searchList.get(position).ChapterNumber.CrPCChapterDenotion +
                                        " - " + searchList.get(position).ChapterNumber.CrPCChapterDescription;
                                break;
                            case 2:
                                title = "Part " + searchList.get(position).ChapterNumber.CPCChapterDenotion +
                                        " - " + searchList.get(position).ChapterNumber.CPCChapterDescription;
                                break;
                            case 3:
                                title = "Chapter " + searchList.get(position).ChapterNumber.EvidenceChapterDenotion +
                                        " - " + searchList.get(position).ChapterNumber.EvidenceChapterDescription;
                                break;
                            case 4:
                                title = "Part " + searchList.get(position).ChapterNumber.ConstiChapterDenotion +
                                        " - " + searchList.get(position).ChapterNumber.ConstiChapterDescription;
                                break;
                        }

                        if (bm == 0) {
                            bookmark.setTextOff("Add to Favorites");
                            bookmark.setChecked(false);
                        } else {
                            bookmark.setTextOn("Remove from Favorites");
                            bookmark.setChecked(true);
                        }

                        bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) { // toggle is on
                                    LawBook.updateBookmarks(Id, 1);
                                    bookmark.setTextOn("Remove from Favorites");
                                } else { // toggle is off
                                    LawBook.updateBookmarks(Id, 0);
                                    bookmark.setTextOff("Add to Favorites");
                                }
                            }
                        });

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
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_search) {

            return true;
        }
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            /**
             * Use this query to display search results like 
             * 1. Getting the data from SQLite and showing in listview 
             * 2. Making webrequest and displaying the data 
             * For now we just display the query only
             */
            txtQuery.setText("Search Query: " + query);


        }

    }
}