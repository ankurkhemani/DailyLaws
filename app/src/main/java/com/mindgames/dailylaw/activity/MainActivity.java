package com.mindgames.dailylaw.activity;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;


import com.activeandroid.ActiveAndroid;
import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.model.Chapters;
import com.mindgames.dailylaw.model.LawBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    static HashMap<Integer, List<LawBook>> ipcMap, crpcMap, cpcMap, evidenceMap, constitutionMap;
    static HashMap<Integer,List<String>> listDataHeaderContainer;
    static HashMap<Integer,HashMap<String, List<String>>> listDataChildContainer;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity()).setContentView(view);
//        mMaterialDialog.setTitle("Exit")
//                .setMessage("")
//                .setPositiveButton("DONE", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mMaterialDialog.dismiss();
//                    }
//                });
////                        .setNegativeButton("CANCEL", new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                mMaterialDialog.dismiss();
////                            }
////                        });
//        mMaterialDialog.setCanceledOnTouchOutside(false);
//        mMaterialDialog.show();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize ActiveAndroid
        ActiveAndroid.initialize(this);

        // preparing list data
        initializeVariables();
        prepareIPCListData(0, ipcMap, 26);
        prepareIPCListData(1, crpcMap, 40);
        prepareIPCListData(2, cpcMap, 12);
        prepareIPCListData(3, evidenceMap, 11);
        prepareIPCListData(4, constitutionMap, 25);



        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
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
        int id = item.getItemId();

        if(id == R.id.action_search) {
            Intent myIntent = new Intent(MainActivity.this, SearchResultsActivity.class);
            this.startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
            displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new LawBookFragment();
                title = getString(R.string.title_lawbook);
                break;
            case 2:
                fragment = new BookmarksFragment();
                title = getString(R.string.title_bookmarks);
                break;
            case 3:
                fragment = new MessagesFragment();
                title = getString(R.string.title_settings);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
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
    private void prepareIPCListData(int Type, HashMap<Integer, List<LawBook>> typeMap, int numberOfChapters) {


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

        listDataHeaderContainer.put(Type,listDataHeader);
        listDataChildContainer.put(Type, listDataChild);
    }


}
