package com.team.noty.getshowrooms.fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team.noty.getshowrooms.R;
import com.team.noty.getshowrooms.adapter.NavDrawerItem;
import com.team.noty.getshowrooms.adapter.NavDrawerListtReviewsAdapter;
import com.team.noty.getshowrooms.api.GetTerSetter;
import com.team.noty.getshowrooms.api.getHttpGet;
import com.team.noty.getshowrooms.customTextView.TextViewPlus;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ReviewsListFragment extends Fragment {


    public ReviewsListFragment() {
        // Required empty public constructor
    }
    getHttpGet request = new getHttpGet();
    ArrayList<GetTerSetter> setComennt = new ArrayList<>();
    String httpGetComment = "http://getshowroom.ru/api/reviews?id=";
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListtReviewsAdapter adapter;
    ListView listView;
    TextViewPlus comment;
    TextViewPlus nameShowRoom, countReviews;
    RatingBar ratingBar;

    String idShowRoom, nameRoom, countReview, rating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews_list, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            idShowRoom = bundle.getString("id");
            nameRoom = bundle.getString("name");
            countReview = bundle.getString("reviews");
            rating = bundle.getString("rating");
            Log.d("MyLog", "id show room " + idShowRoom);
        }
        listView = (ListView) view.findViewById(R.id.resultList);

        nameShowRoom = (TextViewPlus) view.findViewById(R.id.nameShowRoom);
        countReviews = (TextViewPlus) view.findViewById(R.id.countReviews);
        comment = (TextViewPlus) view.findViewById(R.id.comment);

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        nameShowRoom.setText(nameRoom);
        countReviews.setText(countReview + " отзывов");

        ratingBar.setRating(Float.parseFloat(rating));

        navDrawerItems = new ArrayList<NavDrawerItem>();

       getDetailInformation(idShowRoom);

        adapter = new NavDrawerListtReviewsAdapter(getActivity(),
                navDrawerItems);

        listView.setAdapter(adapter);

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new CommentFragment());
            }
        });

        return view;
    }

    public void getDetailInformation(String id)
    {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(request.getHttpGet(httpGetComment + id));
            Gson gson = new Gson();
            setComennt = gson.fromJson(jsonArray.toString(),  new TypeToken<List<GetTerSetter>>(){}.getType());

            if(setComennt != null)
            {
                for (int i = 0; i < setComennt.size(); i++)
                {
                    navDrawerItems.add(new NavDrawerItem(setComennt.get(i).getComment(), setComennt.get(i).getRating()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        Bundle bundle = new Bundle();
        bundle.putString("id", idShowRoom);
        bundle.putString("name", nameRoom);
        bundle.putString("reviews", countReview);
        bundle.putString("rating", rating);
        fragment.setArguments(bundle);
        transaction.commit();
    }

}
