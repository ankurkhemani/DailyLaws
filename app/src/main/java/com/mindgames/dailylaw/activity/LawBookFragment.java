package com.mindgames.dailylaw.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mindgames.dailylaw.R;
import com.mindgames.dailylaw.adapter.IPCListAdapter;
import com.mindgames.dailylaw.model.LawBook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;
import me.drakeet.materialdialog.MaterialDialog;


public class LawBookFragment extends Fragment {

    IPCListAdapter listAdapter;
    AnimatedExpandableListView expListView;
    HashMap<Integer, List<LawBook>> typeMap;
    int spinnerPosition =0, Type = 0;
    DisplayFragment displayFragment;
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
                        listAdapter = new IPCListAdapter(getActivity(), MainActivity.listDataHeaderContainer.get(spinnerPosition), MainActivity.listDataChildContainer.get(spinnerPosition));

                        // setting list adapter
                        expListView.setAdapter(listAdapter);

                        break;

                    case 1:
                        if(typeMap.size()!=0)
                            typeMap.clear();
                        Type = 1;
                        typeMap.putAll(MainActivity.crpcMap);
                        listAdapter = new IPCListAdapter(getActivity(), MainActivity.listDataHeaderContainer.get(spinnerPosition), MainActivity.listDataChildContainer.get(spinnerPosition));

                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        break;

                    case 2:
                        if(typeMap.size()!=0)
                            typeMap.clear();
                        Type = 2;
                        typeMap.putAll(MainActivity.cpcMap);
                        listAdapter = new IPCListAdapter(getActivity(), MainActivity.listDataHeaderContainer.get(spinnerPosition), MainActivity.listDataChildContainer.get(spinnerPosition));

                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        break;
                    case 3:
                        if(typeMap.size()!=0)
                            typeMap.clear();
                        Type = 3;
                        typeMap.putAll(MainActivity.evidenceMap);
                        listAdapter = new IPCListAdapter(getActivity(), MainActivity.listDataHeaderContainer.get(spinnerPosition), MainActivity.listDataChildContainer.get(spinnerPosition));

                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        break;
                    case 4:
                        if(typeMap.size()!=0)
                            typeMap.clear();
                        Type = 4;
                        typeMap.putAll(MainActivity.constitutionMap);
                        listAdapter = new IPCListAdapter(getActivity(), MainActivity.listDataHeaderContainer.get(spinnerPosition), MainActivity.listDataChildContainer.get(spinnerPosition));

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



                String message1 =
                        MainActivity.listDataChildContainer.get(spinnerPosition).get(
                        MainActivity.listDataHeaderContainer.get(spinnerPosition).get(groupPosition)).get(
                        childPosition) + " - " + typeMap.get(groupPosition).get(childPosition).SectionDisplay;

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.display, null);
                final TextView txtView = (TextView) view.findViewById(R.id.display);
                txtView.setText(message1);

                final ToggleButton bookmark = (ToggleButton) view.findViewById(R.id.bookmark);
                final int bm = typeMap.get(groupPosition).get(childPosition).Bookmark;
                final int Id = typeMap.get(groupPosition).get(childPosition).Id;

                String title = "";
                switch(Type){
                    case 0:
                        title = "Chapter " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.IPCChapterDenotion +
                                " - " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.IPCChapterDescription;
                        break;
                    case 1:
                        title = "Chapter " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.CrPCChapterDenotion +
                                " - " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.CrPCChapterDescription;
                        break;
                    case 2:
                        title = "Part " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.CPCChapterDenotion +
                                " - " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.CPCChapterDescription;
                        break;
                    case 3:
                        title = "Chapter " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.EvidenceChapterDenotion +
                                " - " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.EvidenceChapterDescription;
                        break;
                    case 4:
                        title = "Part " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.ConstiChapterDenotion +
                                " - " + typeMap.get(groupPosition).get(childPosition).ChapterNumber.ConstiChapterDescription;
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

//                String message = MainActivity.listDataHeaderContainer.get(spinnerPosition).get(groupPosition)
//                        + "\n\n"
//                        + MainActivity.listDataChildContainer.get(spinnerPosition).get(
//                        MainActivity.listDataHeaderContainer.get(spinnerPosition).get(groupPosition)).get(
//                        childPosition) + " - " + typeMap.get(groupPosition).get(childPosition).SectionDisplay;

//                displayFragment = new DisplayFragment(); //  object of next fragment
//                Bundle bundle = new Bundle();
//                bundle.putString("message", message);
//                bundle.putInt("Id", typeMap.get(groupPosition).get(childPosition).Id);
//                bundle.putInt("bookmark", typeMap.get(groupPosition).get(childPosition).Bookmark);
//                displayFragment.setArguments(bundle);
//
//                FragmentManager fm = getFragmentManager();
//                ft = fm.beginTransaction();
//
//                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_body);
//
//                ft.replace(R.id.container_body, displayFragment);//add display fragment to container
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//
//                ft.addToBackStack(null);//add the transaction to the back stack so the user can navigate back
//                ft.commit();

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
