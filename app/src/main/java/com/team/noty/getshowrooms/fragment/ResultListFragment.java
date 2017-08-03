package com.team.noty.getshowrooms.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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

public class ResultListFragment extends Fragment {


    public ResultListFragment() {
        // Required empty public constructor
    }

    SearchView searchView;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    ListView listView;

    getHttpGet request = new getHttpGet();
    ArrayList<GetTerSetter> getAllShowRooms = new ArrayList<>();
    String httpAllShowRooms = "http://getshowroom.ru/api/showrooms";
    String [] id;
    int sizeId;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result_list, container, false);

        searchView = (SearchView) view.findViewById(R.id.searchView);

        listView = (ListView) view.findViewById(R.id.resultList);
        Bundle bundle = getArguments();
        if (bundle != null) {
            sizeId = bundle.getInt("Size");

            id = new String[sizeId];
            Log.d("MyLog", "length id " + id.length);
            for (int i = 0; i < sizeId; i++)
            {
                id[i] = bundle.getString("id"+i);
            }
        }

        navDrawerItems = new ArrayList<NavDrawerItem>();

        if (!searchView.getQuery().toString().trim().equals(""))
        {
            navDrawerItems.clear();
            adapter.notifyDataSetChanged();
            searchMatch(searchView.getQuery().toString());
        }
        else {
            new MyTask().execute();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idShowRoom = navDrawerItems.get(position).getId();
                replaceFragment(new DetailInfoFragment(), idShowRoom);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("MyLog", "query " + query);
                navDrawerItems.clear();
                adapter.notifyDataSetChanged();
                searchMatch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("MyLog", "query " + newText);
                if (newText.equals(""))
                {
                    navDrawerItems.clear();
                    adapter.notifyDataSetChanged();
                    new MyTask().execute();
                }
                else
                {
                    navDrawerItems.clear();
                    adapter.notifyDataSetChanged();
                    searchMatch(newText);
                }
                return false;
            }
        });

        adapter = new NavDrawerListAdapter(getActivity(), navDrawerItems);
        adapter.notifyDataSetChanged();
        return view;


    }
    private void replaceFragment(Fragment fragment, String id) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void searchMatch(String query)
    {
        query = query.toLowerCase();
        Log.d("MyLog", "query 2 " + query);
        navDrawerItems.clear();
        Log.d("MyLog", "query 2 size " + navDrawerItems.size());
        for (int i = 0; i < getAllShowRooms.size(); i++)
        {

           if (getAllShowRooms.get(i).getName().toLowerCase().contains(query))
           {
               Log.d("MyLog", "matches found " + getAllShowRooms.get(i).getName());
               String address = getAllShowRooms.get(i).getMetro() + ", " + getAllShowRooms.get(i).getAddress();
               navDrawerItems.add(new NavDrawerItem(getAllShowRooms.get(i).getId(), getAllShowRooms.get(i).getName(), getAllShowRooms.get(i).getRating(), address, getAllShowRooms.get(i).getWorktime()));

           }
            else
           {
               if (getAllShowRooms.get(i).getAddress().toLowerCase().contains(query))
               {
                   String address = getAllShowRooms.get(i).getMetro() + ", " + getAllShowRooms.get(i).getAddress();
                   navDrawerItems.add(new NavDrawerItem(getAllShowRooms.get(i).getId(), getAllShowRooms.get(i).getName(), getAllShowRooms.get(i).getRating(), address, getAllShowRooms.get(i).getWorktime()));

               }
           }
        }
        adapter.notifyDataSetChanged();

        if (navDrawerItems.size()==0)
        {
            Toast toast = Toast.makeText(getActivity(),
                    "Результатов по вашему запросу нет!", Toast.LENGTH_SHORT);
            toast.show();
            new MyTask().execute();
        }
    }

    public boolean searchStreet(String query, String nameStreet)
    {
        String[] tempQuery = query.split(" ");
        String[] splitCity = nameStreet.split(",");
        String[] tempStreet = splitCity[1].split(" ");

        for (int i = 0; i < tempQuery.length; i++)
        {
            for (int j = 0; j < tempStreet.length; j++)
            {
                if (tempQuery[i].equals(tempStreet[j]))
                {
                    return true;
                }
            }
        }
        return false;
    }
    class MyTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Загрузка...");

            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            JSONArray jsonArray = null;
            navDrawerItems.clear();
            try {
                jsonArray = new JSONArray(request.getHttpGet(httpAllShowRooms));

                Gson gson = new Gson();
                getAllShowRooms = gson.fromJson(jsonArray.toString(),  new TypeToken<List<GetTerSetter>>(){}.getType());
                if(getAllShowRooms != null)
                {
                    int j = 0;
                    int i = 0;
                    if (getAllShowRooms.size() != 0) {
                        if (sizeId == getAllShowRooms.size())
                        {

                            for (i = 0; i < getAllShowRooms.size(); i++)
                            {
                                String address = getAllShowRooms.get(i).getMetro() + ", " + getAllShowRooms.get(i).getAddress();
                                navDrawerItems.add(new NavDrawerItem(getAllShowRooms.get(i).getId(), getAllShowRooms.get(i).getName(), getAllShowRooms.get(i).getRating(), address, getAllShowRooms.get(i).getWorktime()));

                            }
                        }
                        else {
                            do {
                                if (id[j].equals(getAllShowRooms.get(i).getId())) {
                                    String address = getAllShowRooms.get(i).getMetro() + ", " + getAllShowRooms.get(i).getAddress();
                                    navDrawerItems.add(new NavDrawerItem(getAllShowRooms.get(i).getId(), getAllShowRooms.get(i).getName(), getAllShowRooms.get(i).getRating(), address, getAllShowRooms.get(i).getWorktime()));
                                    i++;
                                    if (j < sizeId - 1) {
                                        j++;
                                    }

                                } else {
                                    getAllShowRooms.remove(i);
                                }
                            }
                            while (sizeId != getAllShowRooms.size());
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter = new NavDrawerListAdapter(getActivity(),
                    navDrawerItems);
            listView.setAdapter(adapter);
            pDialog.dismiss();
        }
    }


}
