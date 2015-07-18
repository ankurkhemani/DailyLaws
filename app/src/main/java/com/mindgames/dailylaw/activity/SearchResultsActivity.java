package com.mindgames.dailylaw.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.external.TypeFaceSpan;
import com.mindgames.dailylaw.model.LawBook;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class SearchResultsActivity extends ActionBarActivity {

    private TextView txtQuery;
    private ListView listView ;

    List<LawBook> searchList;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        //Gesture Detection
        gestureDetection();

        // get the action bar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // Enabling Back navigation on Action Bar icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.Black_transparent_black_percent_70));


        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        //font change
        SpannableString s = new SpannableString("Search");
        s.setSpan(new TypeFaceSpan(SearchResultsActivity.this, "proxima_nova.otf"), 0, s.length(),
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
        searchView.setBackgroundResource(R.drawable.custom_searchview);
        //searchView.setQueryHint("Enter section number .. ");
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setFocusable(true);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocusFromTouch();

        //removing blue underline
        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = searchView.findViewById(searchPlateId);
        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(Color.TRANSPARENT);
        }

        //changing cursor color
        final int textViewID = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null, null);
        final AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(textViewID);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, 0); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {}


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
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(SearchResultsActivity.this, R.layout.search_item, listItems);

                // Set the ArrayAdapter as the ListView's adapter.
                listView.setAdapter( listAdapter );

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        final Dialog alertDialog = new Dialog(SearchResultsActivity.this);
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


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
                            message1 = "Section " + searchList.get(position).SectionNumber + "\n" +
                                    searchList.get(position).SectionParticulars + "\n\n"
                                    + searchList.get(position).SectionDisplay;
                        else
                            message1 = "Part " + searchList.get(position).SectionNumber + "\n" +
                                    searchList.get(position).SectionParticulars + "\n\n"
                                    + searchList.get(position).SectionDisplay;

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
//                            bookmark.setTextOff("Add to Favorites");
                            bookmark.setChecked(false);
                        } else {
//                            bookmark.setTextOn("Remove from Favorites");
                            bookmark.setChecked(true);
                        }

                        bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) { // toggle is on
                                    LawBook.updateBookmarks(Id, 1);
//                                    bookmark.setTextOn("Remove from Favorites");
                                } else { // toggle is off
                                    LawBook.updateBookmarks(Id, 0);
//                                    bookmark.setTextOff("Add to Favorites");
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

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
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