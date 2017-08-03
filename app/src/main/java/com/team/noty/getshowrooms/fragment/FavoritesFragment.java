package com.team.noty.getshowrooms.fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team.noty.getshowrooms.R;
import com.team.noty.getshowrooms.adapter.NavDrawerItem;
import com.team.noty.getshowrooms.adapter.NavDrawerListAdapter;
import com.team.noty.getshowrooms.api.GetTerSetter;
import com.team.noty.getshowrooms.api.getHttpGet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {


    public FavoritesFragment() {
        // Required empty public constructor
    }
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    ListView listView;

    getHttpGet request = new getHttpGet();
    ArrayList<GetTerSetter> setTopShowRooms = new ArrayList<>();
    String httpTopShowRooms = "http://getshowroom.ru/api/showrooms";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favorites, container, false);

        listView = (ListView) view.findViewById(R.id.listView);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        getTopShowRooms();
        adapter = new NavDrawerListAdapter(getActivity(),
                navDrawerItems);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idShowRoom = setTopShowRooms.get(position).getId();
                replaceFragment(new DetailInfoFragment(), idShowRoom);
            }
        });

        return view;
    }
    public void getTopShowRooms()
    {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(request.getHttpGet(httpTopShowRooms));
            Gson gson = new Gson();
            setTopShowRooms = gson.fromJson(jsonArray.toString(),  new TypeToken<List<GetTerSetter>>(){}.getType());

            if(setTopShowRooms != null)
            {
                int i = 0;
                do {
                    if (setTopShowRooms.get(i).getBest().equals("1")) {
                        String address = setTopShowRooms.get(i).getMetro() + ", " + setTopShowRooms.get(i).getAddress();
                        navDrawerItems.add(new NavDrawerItem(setTopShowRooms.get(i).getId(), setTopShowRooms.get(i).getName(), setTopShowRooms.get(i).getRating(), address, setTopShowRooms.get(i).getWorktime()));
                        i++;
                    }
                    else
                    {
                        setTopShowRooms.remove(i);
                    }

                }
                while (i < setTopShowRooms.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void replaceFragment(Fragment fragment, String s) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        Bundle bundle = new Bundle();
        bundle.putString("id", s);
        fragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
