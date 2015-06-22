package com.mindgames.dailylaw.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.adapter.IPCListAdapter;
import com.mindgames.dailylaw.model.LawBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class BookmarksFragment extends Fragment {

    IPCListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<Integer,List<LawBook>> lawbook;

    public BookmarksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize ActiveAndroid
        ActiveAndroid.initialize(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_lawbook, container, false);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new IPCListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                View view = LayoutInflater.from(getActivity()).inflate(R.layout.display, null);
                final TextView txtView = (TextView) view.findViewById(R.id.display);
                String message1;
                if(groupPosition!=4)
                     message1 = "Section " + lawbook.get(groupPosition).get(childPosition).SectionNumber + " - " + lawbook.get(groupPosition).get(childPosition).SectionDisplay;
                else
                     message1 = "Part " + lawbook.get(groupPosition).get(childPosition).SectionNumber + " - " + lawbook.get(groupPosition).get(childPosition).SectionDisplay;

                txtView.setText(message1);

                final ToggleButton bookmark = (ToggleButton) view.findViewById(R.id.bookmark);
                final int bm = lawbook.get(groupPosition).get(childPosition).Bookmark;
                final int Id = lawbook.get(groupPosition).get(childPosition).Id;

                String title = "";

                switch(groupPosition){
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
                            LawBook.updateBookmarks(Id, 1);
                            bookmark.setTextOn("Remove from Favorites");
                        } else { // toggle is off
                            LawBook.updateBookmarks(Id, 0);
                            bookmark.setTextOff("Add to Favorites");
                        }
                    }
                });

                final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity()).setContentView(view);
                mMaterialDialog.setTitle(title)
                        .setMessage(message1)
                        .setPositiveButton("DONE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });
//                        .setNegativeButton("CANCEL", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mMaterialDialog.dismiss();
//                            }
//                        });
                mMaterialDialog.setCanceledOnTouchOutside(false);
                mMaterialDialog.show();

                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                // do something
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                // do something

            }
        });


        return rootView;
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
}
