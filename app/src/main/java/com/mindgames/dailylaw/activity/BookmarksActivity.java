package com.mindgames.dailylaw.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.activeandroid.ActiveAndroid;
import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.adapter.ExpandableListAdapter;
import com.mindgames.dailylaw.external.AnimatedExpandableListView;
import com.mindgames.dailylaw.external.TypeFaceSpan;
import com.mindgames.dailylaw.model.LawBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BookmarksActivity extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    AnimatedExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<Integer,List<LawBook>> lawbook;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    public BookmarksActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarks);

        //initialize ActiveAndroid
        ActiveAndroid.initialize(this);

        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        //Gesture Detection
        gestureDetection();

        // get the action bar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // Enabling Back navigation on Action Bar icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //font change
        SpannableString s = new SpannableString("BOOKMARKS");
        s.setSpan(new TypeFaceSpan(BookmarksActivity.this, "proxima_nova.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.Black_transparent_black_percent_70));


        // get the listview
        expListView = (AnimatedExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                if(lawbook.get(groupPosition).size()!=0) {
                    final Dialog alertDialog = new Dialog(BookmarksActivity.this);
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    alertDialog.setContentView(R.layout.display);
                    Button done = (Button) alertDialog.findViewById(R.id.done);
                    TextView dialogTitle = (TextView) alertDialog.findViewById(R.id.title);
                    TextView txtView = (TextView) alertDialog.findViewById(R.id.display);

//                        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),
//                                "fonts/alpha_echo.");
//                        dialogTitle.setTypeface(typeface);
                    txtView.setTypeface(Typeface.SERIF);

                    String message1;
                    if (groupPosition != 4)
                        message1 = "Section " + lawbook.get(groupPosition).get(childPosition).SectionNumber + "\n" +
                                lawbook.get(groupPosition).get(childPosition).SectionParticulars + "\n\n"
                                + lawbook.get(groupPosition).get(childPosition).SectionDisplay;
                    else
                        message1 = "Part " + lawbook.get(groupPosition).get(childPosition).SectionNumber + "\n" +
                                lawbook.get(groupPosition).get(childPosition).SectionParticulars + "\n\n"
                                + lawbook.get(groupPosition).get(childPosition).SectionDisplay;

                    txtView.setText(message1);

                    final ToggleButton bookmark = (ToggleButton) alertDialog.findViewById(R.id.bookmark);
                    final int bm = lawbook.get(groupPosition).get(childPosition).Bookmark;
                    final int Id = lawbook.get(groupPosition).get(childPosition).Id;

                    String title = "";

                    switch (groupPosition) {
                        case 0:
                            title = "Chapter " + lawbook.get(groupPosition).get(childPosition).ChapterNumber.IPCChapterDenotion +
                                    " - " + lawbook.get(groupPosition).get(childPosition).ChapterNumber.IPCChapterDescription;

                            break;
                        case 1:
                            title = "Chapter " + lawbook.get(groupPosition).get(childPosition).ChapterNumber.CrPCChapterDenotion +
                                    " - " + lawbook.get(groupPosition).get(childPosition).ChapterNumber.CrPCChapterDescription;
                            break;
                        case 2:
                            title = "Part " + lawbook.get(groupPosition).get(childPosition).ChapterNumber.CPCChapterDenotion +
                                    " - " + lawbook.get(groupPosition).get(childPosition).ChapterNumber.CPCChapterDescription;
                            break;
                        case 3:
                            title = "Chapter " + lawbook.get(groupPosition).get(childPosition).ChapterNumber.EvidenceChapterDenotion +
                                    " - " + lawbook.get(groupPosition).get(childPosition).ChapterNumber.EvidenceChapterDescription;
                            break;
                        case 4:
                            title = "Part " + lawbook.get(groupPosition).get(childPosition).ChapterNumber.ConstiChapterDenotion +
                                    " - " + lawbook.get(groupPosition).get(childPosition).ChapterNumber.ConstiChapterDescription;
                            break;
                    }

                    if (bm == 0) {
//                        bookmark.setTextOff("Add to Favorites");
                        bookmark.setChecked(false);
                    } else {
//                        bookmark.setTextOn("Remove from Favorites");
                        bookmark.setChecked(true);
                    }

                    bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) { // toggle is on
                                LawBook.updateBookmarks(Id, 1);
//                                bookmark.setTextOn("Remove from Favorites");
                            } else { // toggle is off
                                LawBook.updateBookmarks(Id, 0);
//                                bookmark.setTextOff("Add to Favorites");
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
                return false;
            }
        });

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
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        HashMap<Integer, List<String>> chapMap = new HashMap<Integer, List<String>>();


        lawbook = new HashMap<Integer, List<LawBook>>();

        // Adding child data
        listDataHeader.add("Indian Penal Code, 1860");
        listDataHeader.add("Code of Criminal Procedure, 1973");
        listDataHeader.add("Code Of Civil Procedure, 1908");
        listDataHeader.add("Evidence Act, 1872");
        listDataHeader.add("Constitution of India, 1949");






        for (int i=0; i< 5; i++){

            lawbook.put(i, LawBook.getBookmarks(i));


            // Adding child data
            List<String> childData = new ArrayList<String>();

            if(lawbook.get(i).size()==0)
                childData.add("You haven't bookmarked anything yet!");

            for (LawBook row : lawbook.get(i)){
                switch(i) {
                    case 0:
                    childData.add("Chapter " + row.ChapterNumber.IPCChapterDenotion
                            + " | Section " + row.SectionNumber
                            + "\n" + row.SectionParticulars);
                        break;
                    case 1:
                    childData.add("Chapter " + row.ChapterNumber.CrPCChapterDenotion
                            + " | Section " + row.SectionNumber
                            + "\n" + row.SectionParticulars);
                        break;
                    case 2:
                        childData.add("Part " + row.ChapterNumber.CPCChapterDenotion
                                + " | Section " + row.SectionNumber
                                + "\n" + row.SectionParticulars);
                        break;
                    case 3:
                        childData.add("Chapter " + row.ChapterNumber.EvidenceChapterDenotion
                                + " | Section " + row.SectionNumber
                                + "\n" + row.SectionParticulars);
                        break;
                    case 4:
                        childData.add("Part " + row.ChapterNumber.ConstiChapterDenotion
                                + " | Article " + row.SectionNumber
                                + "\n" + row.SectionParticulars);
                        break;

                }

            }
            chapMap.put(i, childData);

            //linking child to header
            listDataChild.put(listDataHeader.get(i), chapMap.get(i)); // Header, Child data

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
        getMenuInflater().inflate(R.menu.menu_other, menu);
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
                Intent myIntent = new Intent(BookmarksActivity.this, SearchResultsActivity.class);
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
                    Intent myIntent = new Intent(BookmarksActivity.this, SearchResultsActivity.class);
                    BookmarksActivity.this.startActivity(myIntent);
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
