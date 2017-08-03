package com.team.noty.getshowrooms.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.team.noty.getshowrooms.R;
import com.team.noty.getshowrooms.adapter.ShowRoomsHelper;
import com.team.noty.getshowrooms.adapter.DataBasesHelper;
import com.team.noty.getshowrooms.adapter.NavDrawerItem;
import com.team.noty.getshowrooms.adapter.NavDrawerListAdapter;

import java.util.ArrayList;

public class LikedFragment extends Fragment {


    public LikedFragment() {
        // Required empty public constructor
    }

    SearchView searchView;
    private ArrayList<NavDrawerItem> navDrawerItems;
    ArrayList IdList = new ArrayList();
    private NavDrawerListAdapter adapter;
    ListView listView;

    DataBasesHelper dataBasesHelper;
    ShowRoomsHelper showRoomsHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liked, container, false);
        searchView = (SearchView) view.findViewById(R.id.searchView);

        listView = (ListView) view.findViewById(R.id.listView);

        dataBasesHelper = new DataBasesHelper(getActivity());

        IdList = dataBasesHelper.getidRow();

        navDrawerItems = new ArrayList<NavDrawerItem>();

        for (int i = 0; i < IdList.size(); i++)
        {
            showRoomsHelper = dataBasesHelper.getShowRooms(IdList.get(i).toString());
            navDrawerItems.add(new NavDrawerItem(showRoomsHelper.getId(), showRoomsHelper.getName_showrooms(), showRoomsHelper.getRating(), showRoomsHelper.getAddress(), showRoomsHelper.getWork_time()));
        }
        adapter = new NavDrawerListAdapter(getActivity(),
                navDrawerItems);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                replaceFragment(new DetailInfoFragment(), navDrawerItems.get(position).getId());
            }
        });

        return view;
    }
    private void replaceFragment(Fragment fragment, String string) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        Bundle bundle = new Bundle();
        bundle.putString("id", string);
        fragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
