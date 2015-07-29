package com.mindgames.dailylaw.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.adapter.ExpandableListAdapter;
import com.mindgames.dailylaw.external.AnimatedExpandableListView;
import com.mindgames.dailylaw.model.LawBook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LawBookFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    AnimatedExpandableListView expListView;
    HashMap<Integer, List<LawBook>> typeMap;
    int spinnerPosition =0, Type = 0;
    android.support.v4.app.FragmentTransaction ft;

    public LawBookFragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_lawbook, container, false);

        // get the listview
        expListView = (AnimatedExpandableListView) rootView.findViewById(R.id.lvExp);

        typeMap = new HashMap<Integer, List<LawBook>>();

        // Spinner element
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Indian Penal Code, 1860");
        categories.add("Code of Criminal Procedure, 1973");
        categories.add("Code Of Civil Procedure, 1908");
        categories.add("Evidence Act, 1872");
        categories.add("Constitution of India, 1949");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getActivity(), R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item

                spinnerPosition = position;

                switch(position){
                    case 0:
                        if(typeMap.size()!=0)
                            typeMap.clear();
                        Type = 0;
                        typeMap.putAll(MainActivity.ipcMap);
                        listAdapter = new ExpandableListAdapter(getActivity(), MainActivity.listDataHeaderContainer.get(spinnerPosition), MainActivity.listDataChildContainer.get(spinnerPosition));

                        // setting list adapter
                        expListView.setAdapter(listAdapter);

                        break;

                    case 1:
                        if(typeMap.size()!=0)
                            typeMap.clear();
                        Type = 1;
                        typeMap.putAll(MainActivity.crpcMap);
                        listAdapter = new ExpandableListAdapter(getActivity(), MainActivity.listDataHeaderContainer.get(spinnerPosition), MainActivity.listDataChildContainer.get(spinnerPosition));

                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        break;

                    case 2:
                        if(typeMap.size()!=0)
                            typeMap.clear();
                        Type = 2;
                        typeMap.putAll(MainActivity.cpcMap);
                        listAdapter = new ExpandableListAdapter(getActivity(), MainActivity.listDataHeaderContainer.get(spinnerPosition), MainActivity.listDataChildContainer.get(spinnerPosition));

                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        break;
                    case 3:
                        if(typeMap.size()!=0)
                            typeMap.clear();
                        Type = 3;
                        typeMap.putAll(MainActivity.evidenceMap);
                        listAdapter = new ExpandableListAdapter(getActivity(), MainActivity.listDataHeaderContainer.get(spinnerPosition), MainActivity.listDataChildContainer.get(spinnerPosition));

                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        break;
                    case 4:
                        if(typeMap.size()!=0)
                            typeMap.clear();
                        Type = 4;
                        typeMap.putAll(MainActivity.constitutionMap);
                        listAdapter = new ExpandableListAdapter(getActivity(), MainActivity.listDataHeaderContainer.get(spinnerPosition), MainActivity.listDataChildContainer.get(spinnerPosition));

                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        break;
                    default:
                        Toast.makeText(parent.getContext(), "No Type Selected .. " + position, Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        // Listview on child click listener

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {



                final String message1 =
                        MainActivity.listDataChildContainer.get(spinnerPosition).get(
                        MainActivity.listDataHeaderContainer.get(spinnerPosition).get(groupPosition)).get(
                        childPosition) + "\n\n" + typeMap.get(groupPosition).get(childPosition).SectionDisplay;

                final Dialog alertDialog = new Dialog(getActivity());
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

                txtView.setText(message1);

                final ToggleButton bookmark = (ToggleButton) alertDialog.findViewById(R.id.bookmark);
                final int bm = typeMap.get(groupPosition).get(childPosition).Bookmark;
                final int Id = typeMap.get(groupPosition).get(childPosition).Id;

                String title = "";
                String BareAct = "";
                switch(Type){
                    case 0:
                        title = "Chapter " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.IPCChapterDenotion +
                                " - " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.IPCChapterDescription;
                        BareAct = "IPC";
                        break;
                    case 1:
                        title = "Chapter " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.CrPCChapterDenotion +
                                " - " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.CrPCChapterDescription;
                        BareAct = "CrPC";
                        break;
                    case 2:
                        title = "Part " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.CPCChapterDenotion +
                                " - " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.CPCChapterDescription;
                        BareAct = "CPC";
                        break;
                    case 3:
                        title = "Chapter " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.EvidenceChapterDenotion +
                                " - " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.EvidenceChapterDescription;
                        BareAct = "Evidence Act";
                        break;
                    case 4:
                        title = "Part " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.ConstiChapterDenotion +
                                " - " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.ConstiChapterDescription;
                        BareAct = "Constitution of India";
                        break;
                }

                if(bm==0) {
//                    bookmark.setTextOff("Add to Favorites");
                    bookmark.setChecked(false);
                }
                else {
//                    bookmark.setTextOn("Remove from Favorites");
                    bookmark.setChecked(true);
                }

                bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) { // toggle is on
                            LawBook.updateBookmarks(Id, 1);
//                            bookmark.setTextOn("Remove from Favorites");
                        } else { // toggle is off
                            LawBook.updateBookmarks(Id, 0);
//                            bookmark.setTextOff("Add to Favorites");
                        }
                    }
                });


                final String shareTitle = title;
                final String shareBareAct = BareAct;
                final Button share = (Button) alertDialog.findViewById(R.id.share);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try
                        { Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            String sAux = shareBareAct + "\n" + shareTitle + "\n\n" + message1 + "\n";
                            sAux = sAux + "- - -\nShared using Daily Laws Android app. " +
                                    "Get it on Google Play store:\n\n https://play.google.com/store/apps/details?id="
                                    + getActivity().getPackageName() + "\n";
                            i.putExtra(Intent.EXTRA_TEXT, sAux);
                            startActivity(Intent.createChooser(i, "Choose One"));
                        }
                        catch(Exception e)
                        { //e.toString();
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

        return rootView;
    }
}
