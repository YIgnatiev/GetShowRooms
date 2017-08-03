package com.team.noty.getshowrooms.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team.noty.getshowrooms.R;
import com.team.noty.getshowrooms.api.GetTerSetter;
import com.team.noty.getshowrooms.api.getHttpGet;
import com.team.noty.getshowrooms.customTextView.TextViewPlus;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements View.OnClickListener {


    public SearchFragment() {
        // Required empty public constructor
    }
    TextViewPlus buttonResult, city, region, underGroundStation, price, workTime;
    LinearLayout lineContentCity, lineContentRegion, lineContentUnderground,
            lineContentPrice, lineContentWorkTime, filter;
    String chooseString, countResult;
    SharedPreferences sharedPreferences;
    getHttpGet request = new getHttpGet();
    ArrayList<GetTerSetter> getAllShowRooms = new ArrayList<>();
    String httpAllShowRooms = "http://getshowroom.ru/api/showrooms";
    String [] id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Filter", Context.MODE_PRIVATE);

        lineContentCity = (LinearLayout) view.findViewById(R.id.lineContentCity);
        lineContentRegion = (LinearLayout) view.findViewById(R.id.lineContentRegion);
        lineContentUnderground = (LinearLayout) view.findViewById(R.id.lineContentUnderground);
        lineContentPrice = (LinearLayout) view.findViewById(R.id.lineContentPrice);
        lineContentWorkTime = (LinearLayout) view.findViewById(R.id.lineContentWorkTime);
        filter = (LinearLayout) view.findViewById(R.id.filter);

        buttonResult = (TextViewPlus) view.findViewById(R.id.buttonResult);
        city = (TextViewPlus) view.findViewById(R.id.city);
        region = (TextViewPlus) view.findViewById(R.id.region);
        underGroundStation = (TextViewPlus) view.findViewById(R.id.underGroundStation);
        price = (TextViewPlus) view.findViewById(R.id.price);
        workTime = (TextViewPlus) view.findViewById(R.id.workTime);



        lineContentCity.setOnClickListener(this);
        lineContentRegion.setOnClickListener(this);
        lineContentUnderground.setOnClickListener(this);
        lineContentPrice.setOnClickListener(this);
        lineContentWorkTime.setOnClickListener(this);

        buttonResult.setOnClickListener(this);



        if (sharedPreferences.contains("city")) {
            chooseString = sharedPreferences.getString("city", "");
            city.setText(chooseString);
        }
        if (sharedPreferences.contains("region")) {
            chooseString = sharedPreferences.getString("region", "");
            region.setText(chooseString);
        }
        if (sharedPreferences.contains("station")) {
            chooseString = sharedPreferences.getString("station", "");
            underGroundStation.setSelected(true);
            underGroundStation.setText(chooseString);
        }
        if (sharedPreferences.contains("price")) {
            chooseString = sharedPreferences.getString("price", "");
            price.setText(chooseString);
        }
        if (sharedPreferences.contains("workTime")) {
            chooseString = sharedPreferences.getString("workTime", "");
            workTime.setText(chooseString);
        }
        new MyTask().execute();


        return view;
    }
    private void replaceFragment(Fragment fragment, String[] id) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        Bundle bundle = new Bundle();
        bundle.putInt("Size", id.length);
        for (int i = 0; i < id.length; i++) {
            bundle.putString("id"+i, id[i]);
        }
        fragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void replaceChoiceFragment(Fragment fragment, String string) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        Bundle bundle = new Bundle();
        bundle.putString("NameItem", string);
        fragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.lineContentCity:
                replaceChoiceFragment(new ChooseListFragment(), "city");
                break;
            case R.id.lineContentRegion:
                replaceChoiceFragment(new ChooseListFragment(), "region");
                break;
            case R.id.lineContentUnderground:
                replaceChoiceFragment(new ChooseListFragment(), "station");
                break;
            case R.id.lineContentPrice:
                replaceChoiceFragment(new ChooseListFragment(), "price");
                break;
            case R.id.lineContentWorkTime:
                replaceChoiceFragment(new ChooseListFragment(), "workTime");
                break;
            case R.id.buttonResult:
                if (id.length != 0) {
                    replaceFragment(new ResultListFragment(), id);
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(),
                            "Результатов по вашему запросу нет!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }


    public void getSearchResultForFilter(ArrayList<GetTerSetter> getAllShowRooms )
    {
        String nameCity, nameRegion, nameStation, valuePrice, valueWorkTime;
        nameCity = city.getText().toString();
        nameRegion = region.getText().toString();
        nameStation = underGroundStation.getText().toString();
        valuePrice = price.getText().toString();
        valueWorkTime = workTime.getText().toString();
        if (!nameCity.equals("Не важно"))
        {
            int i = 0;
            do {
                if (nameCity.equals(getAllShowRooms.get(i).getCity()))
                {
                    i++;
                }
                else
                {
                    getAllShowRooms.remove(i);
                }
            }
            while (i != getAllShowRooms.size());
        }

        if (!nameRegion.equals("Не важно"))
        {
            int i = 0;
            if (getAllShowRooms.size() != 0) {
                do {
                    if (nameRegion.equals(getAllShowRooms.get(i).getDistrict())) {
                        i++;
                    } else {
                        getAllShowRooms.remove(i);
                    }
                }
                while (i != getAllShowRooms.size());
            }
        }

        if (!nameStation.equals("Не важно"))
        {
            int i = 0;
            if (getAllShowRooms.size() != 0) {

                do {
                    if (nameStation.equals(getAllShowRooms.get(i).getMetro())) {
                        i++;
                    } else {
                        getAllShowRooms.remove(i);
                    }
                }
                while (i != getAllShowRooms.size());
            }
        }
        if (!valuePrice.equals("Не важно")) {
            if (getAllShowRooms.size() != 0) {

                int i = 0;
                do {
                    Log.d("MyLog", "value " + valuePrice);
                    Log.d("MyLog", " string price " + getStringPrice(valuePrice));
                    Log.d("MyLog", "  price " + getAllShowRooms.get(i).getPrice());
                    if (getStringPrice(valuePrice).equals(getAllShowRooms.get(i).getPrice())) {
                        i++;
                    } else {
                        getAllShowRooms.remove(i);

                    }
                }
                while (i != getAllShowRooms.size());
            }
        }
        if (!valueWorkTime.equals("Не важно"))
        {
            int i = 0;
            if (getAllShowRooms.size() != 0) {
                do {
                    if (valueWorkTime.equals(getAllShowRooms.get(i).getWorktime())) {
                        i++;
                    } else {
                        getAllShowRooms.remove(i);

                    }
                }
                while (i != getAllShowRooms.size());
            }
        }
        countResult = "Найдено " + getAllShowRooms.size();
        buttonResult.setText(countResult);
        id = new String[getAllShowRooms.size()];
        Log.d("MyLog", "length id " + id.length);
        for (int j = 0; j < getAllShowRooms.size(); j++)
        {
            id[j] = getAllShowRooms.get(j).getId();

        }

    }

    public String getStringPrice(String valuePrice)
    {
        String temp = valuePrice.trim();
        String result = null;
        Log.d("MyLog","temp " + temp);
        switch (valuePrice)
        {
            case "\u20BD":
                result = "до 5000р";
                break;
            case "\u20BD \u20BD":
                result = "от 5000р до 14000р";
                break;
            case "\u20BD \u20BD \u20BD":
                result = "от 15000р до 25000р";
                break;
            case "\u20BD \u20BD \u20BD \u20BD":
                result = "от 25000р";
                break;

        }
        Log.d("MyLog", "result " +result);
        return result;
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(request.getHttpGet(httpAllShowRooms));

                Gson gson = new Gson();
                getAllShowRooms = gson.fromJson(jsonArray.toString(),  new TypeToken<List<GetTerSetter>>(){}.getType());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            getSearchResultForFilter(getAllShowRooms);
        }
    }
}
