package com.mindgames.dailylaw.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.activeandroid.ActiveAndroid;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.adapter.ViewPagerAdapter;
import com.mindgames.dailylaw.external.SlidingTabLayout;
import com.mindgames.dailylaw.external.TypeFaceSpan;
import com.mindgames.dailylaw.model.Chapters;
import com.mindgames.dailylaw.model.LawBook;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import im.delight.apprater.AppRater;
import me.drakeet.materialdialog.MaterialDialog;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    static HashMap<Integer, List<LawBook>> ipcMap, crpcMap, cpcMap, evidenceMap, constitutionMap;
    static HashMap<Integer,List<String>> listDataHeaderContainer;
    static HashMap<Integer,HashMap<String, List<String>>> listDataChildContainer;

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Daily Laws","Bare Acts", ""};
    int Numboftabs =2;
    ProgressDialog LoadDialog;
    boolean firstTime = true;

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onBackPressed() {
        final MaterialDialog mMaterialDialog = new MaterialDialog(this);
        mMaterialDialog.setTitle("Exit?")
                .setMessage("Are you sure that you want to leave this app?")
                .setPositiveButton("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setNegativeButton("No", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });

        mMaterialDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //GOOGLE ANALYTICS
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-40831892-7"); // Replace with actual tracker/property Id

        // All subsequent hits will be send with screen name = "main screen"
        tracker.setScreenName("main screen");

        tracker.enableExceptionReporting(true);
        tracker.enableAutoActivityTracking(true);


        //initialize ActiveAndroid
        ActiveAndroid.initialize(this);

        // initialize variables
        initializeVariables();

        //Run Async Task - preparing DB
        new PrepareDatabase().execute();

        //AppRater code
        AppRater appRater = new AppRater(this);
        appRater.setDaysBeforePrompt(1);
        appRater.setLaunchesBeforePrompt(3);
        appRater.setPhrases("Rate this app", "Please take a moment to rate this app!", "Rate now", "Later", "No, thanks");
        appRater.show();

        //Toolbar code
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SpannableString s = new SpannableString("INDIAN LAWS");
        s.setSpan(new TypeFaceSpan(MainActivity.this,"proxima_nova.otf" ), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.Black_transparent_black_percent_70));

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        //displayView(0);
//        Fragment fragment = new DailyLawFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container_body, fragment).commit();
//
//        // set the toolbar title
//        getSupportActionBar().setTitle(getString(R.string.title_home));





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
                Intent myIntent = new Intent(MainActivity.this, SearchResultsActivity.class);
                this.startActivity(myIntent);
                return true;

            case R.id.menu_item_share:
                try
                { Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    String sAux = "Daily Laws - India\n\nCheck out this cool Android App that spreads " +
                            "awareness of useful everyday Indian Laws in layman terms!\n\n" +
                            "Also, a must have for all law students and lawyers!\n\n";
                    sAux = sAux + "Get it on Google Play store now:\n\nhttps://play.google.com/store/apps/details?id="
                            + getApplicationContext().getPackageName() + "\n\n#UnleashThePowerOfCommonMan";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Choose One"));
                    return true;
                }
                catch(Exception e)
                { //e.toString();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
            displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        final Dialog alertDialog = new Dialog(MainActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        switch (position) {

            case 0:
                Intent myIntent = new Intent(MainActivity.this, BookmarksActivity.class);
                this.startActivity(myIntent);
                break;
            case 1:
                alertDialog.setContentView(R.layout.about);
                Button done1 = (Button) alertDialog.findViewById(R.id.done);
                Button flike = (Button) alertDialog.findViewById(R.id.flike);


                flike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // open facebook
                        try
                        {
                            // try to open fb app if available
                            getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/471755599659763"));

                            startActivity(i);
                            overridePendingTransition(R.anim.left_in,R.anim.left_out);
                        }
                        catch (Exception e)
                        {
                            // if no fb app then open web page
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/iqtestsaga"));

                            startActivity(i);
                            overridePendingTransition(R.anim.left_in,R.anim.left_out);
                        }
                    }
                });

                done1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

                break;
            case 2:
                alertDialog.setContentView(R.layout.feedback);
                Button done = (Button) alertDialog.findViewById(R.id.done);
                Button rate = (Button) alertDialog.findViewById(R.id.rate);
                Button suggestions = (Button) alertDialog.findViewById(R.id.suggestions);

                rate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("UX")
                                .setAction("click")
                                .setLabel("Rate Now")
                                .build());

                        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        try
                        {
                            startActivity(i);
                            overridePendingTransition(R.anim.left_in,R.anim.left_out);

                        }
                        catch (ActivityNotFoundException e)
                        {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                            //Toast.makeText(getApplicationContext(), "Sorry, unable to reach the Play Store!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                suggestions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "dailylaw@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestions - Indians Laws App");
                        startActivity(Intent.createChooser(emailIntent, null));

                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

//                        View customToastroot =getLayoutInflater().inflate(R.layout.custom_toast, null);
//
//                        Toast toast = Toast.makeText(MainActivity.this, "Added to bookmarks.. ", Toast.LENGTH_SHORT );
//                        toast.setView(customToastroot);
//
//                        TextView text = (TextView) customToastroot.findViewById(R.id.text);
//                        text.setText("Added to Bookmarks .. ");
//
//                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);                        toast.setView(customToastroot);
//                        toast.show();
                    }
                });

                alertDialog.show();

                break;
            case 3:
                alertDialog.setContentView(R.layout.disclaimer);
                Button done2 = (Button) alertDialog.findViewById(R.id.done);

                done2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
//                new CountDownTimer(150, 150) {
//                    public void onTick(long millisUntilFinished) {
//
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        // TODO Auto-generated method stub
//
//                    }
//
//
//                }.start();

                break;

            default:
                break;
        }

        if (fragment != null) {
            String backStateName = fragment.getClass().getName();


//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            //fragmentTransaction.addToBackStack(backStateName);
//            fragmentTransaction.commit();

            //drawerFragment.mDrawerToggle.setDrawerIndicatorEnabled(false);

            // set the toolbar title
            //getSupportActionBar().setTitle(title);
        }

    }

    private void initializeVariables(){

        listDataHeaderContainer = new HashMap<Integer,List<String>>();
        listDataChildContainer = new HashMap<Integer,HashMap<String, List<String>>>();

        ipcMap = new HashMap<Integer, List<LawBook>>();
        crpcMap = new HashMap<Integer, List<LawBook>>();
        cpcMap = new HashMap<Integer, List<LawBook>>();
        evidenceMap = new HashMap<Integer, List<LawBook>>();
        constitutionMap = new HashMap<Integer, List<LawBook>>();
    }

     /*
     * Preparing the list data for LawBook
     */
    private void prepareListData(int Type, HashMap<Integer, List<LawBook>> typeMap, int numberOfChapters) {


        List<String> listDataHeader = new ArrayList<String>();;
        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
        HashMap<Integer, List<String>> chapMap = new HashMap<Integer, List<String>>();


        List<Chapters> chapters = Chapters.getChapterDetails(numberOfChapters);

        for (int i=0; i< chapters.size(); i++) {

            //get all section rows for chapter i+1 and Type O (IPC)
            typeMap.put(i, LawBook.getChapterRows(i + 1, Type));
            // Adding child data
            List<String> chapter = new ArrayList<String>();
            for (LawBook row : typeMap.get(i)){
                if(Type!=4)
                    chapter.add("Section " + row.SectionNumber + "\n" + row.SectionParticulars);
                else
                    chapter.add("Article " + row.SectionNumber + "\n" + row.SectionParticulars);

            }
            chapMap.put(i, chapter);

            //add headers

            switch(Type) {
                case 0 :
                    listDataHeader.add("Chapter " + chapters.get(i).IPCChapterDenotion + " - " +
                        chapters.get(i).IPCChapterDescription + "\n" + chapters.get(i).IPCSectionRange);
                    break;
                case 1 :
                    listDataHeader.add("Chapter " + chapters.get(i).CrPCChapterDenotion + " - " +
                            chapters.get(i).CrPCChapterDescription + "\n" + chapters.get(i).CrPCSectionRange);
                    break;
                case 2 :
                    listDataHeader.add("Part " + chapters.get(i).CPCChapterDenotion + " - " +
                            chapters.get(i).CPCChapterDescription + "\n" + chapters.get(i).CPCSectionRange);
                    break;
                case 3 :
                    listDataHeader.add("Chapter " + chapters.get(i).EvidenceChapterDenotion + " - " +
                            chapters.get(i).EvidenceChapterDescription + "\n" + chapters.get(i).EvidenceSectionRange);
                    break;
                case 4 :
                    listDataHeader.add("Chapter " + chapters.get(i).ConstiChapterDenotion + " - " +
                            chapters.get(i).ConstiChapterDescription + "\n" + chapters.get(i).ConstiSectionRange);
                    break;
            }
            //linking child to header
            listDataChild.put(listDataHeader.get(i), chapMap.get(i)); // Header, Child data
        }

        listDataHeaderContainer.put(Type, listDataHeader);
        listDataChildContainer.put(Type, listDataChild);
    }


    class PrepareDatabase extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            LoadDialog = new ProgressDialog(MainActivity.this);
            LoadDialog.setMessage("Please wait for a moment ...");
            LoadDialog.setCancelable(false);
            LoadDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            prepareListData(0, ipcMap, 26);
            prepareListData(1, crpcMap, 40);
            prepareListData(2, cpcMap, 12);
            prepareListData(3, evidenceMap, 11);
            prepareListData(4, constitutionMap, 25);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles, Numboftabs);

            // Assigning ViewPager View and setting the adapter
            pager = (ViewPager) findViewById(R.id.pager);

            pager.setAdapter(adapter);

            // Assiging the Sliding Tab Layout View
            tabs = (SlidingTabLayout) findViewById(R.id.tabs);
            tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

            // Setting Custom Color for the Scroll bar indicator of the Tab View
            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.Black);
                }
            });

            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);
            LoadDialog.dismiss();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        //Check if ipcMAp is cleared by Garbage Collector, then prepareDB again, else setAdapter
//        if(!firstTime && listDataHeaderContainer.get(0).size()==0)
//            new PrepareDatabase().execute();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        firstTime = false;
//    }
}
