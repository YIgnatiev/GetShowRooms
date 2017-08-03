package com.team.noty.getshowrooms.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.team.noty.getshowrooms.R;
import com.team.noty.getshowrooms.customTextView.TextViewPlus;

import java.util.HashMap;
import java.util.Map;

public class CommentFragment extends Fragment {


    public CommentFragment() {
        // Required empty public constructor
    }
    TextViewPlus review;

    String url = "http://getshowroom.ru/api/reviews";
    TextViewPlus nameShowRoom, countReviews;
    RatingBar viewRatingBar, ratingBar;
    String idShowRoom, nameRoom, countReview, rating;
    EditText textComment;
    String originalMessage = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            idShowRoom = bundle.getString("id");
            nameRoom = bundle.getString("name");
            countReview = bundle.getString("reviews");
            rating = bundle.getString("rating");
            Log.d("MyLog", "id show room " + idShowRoom);
        }

        review = (TextViewPlus) view.findViewById(R.id.review);
        nameShowRoom = (TextViewPlus) view.findViewById(R.id.nameShowRoom);
        countReviews = (TextViewPlus) view.findViewById(R.id.countReviews);

        textComment = (EditText) view.findViewById(R.id.textComment);

        viewRatingBar = (RatingBar) view.findViewById(R.id.viewRatingBar);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        nameShowRoom.setText(nameRoom);
        countReviews.setText(countReview + " отзывов");

        viewRatingBar.setRating(Float.parseFloat(rating));

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!textComment.getText().toString().equals("")) {
                    RequestQueue queue = Volley.newRequestQueue(getActivity());

                    String text =  textComment.getText().toString();
                    originalMessage = convertToUTF8(text);
                    StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    replaceFragment(new DetailInfoFragment());
                                    Toast toast = Toast.makeText(getActivity(),
                                            "Спасибо за Ваш отзыв! \n Он будет добавлен после проверки.", Toast.LENGTH_LONG);
                                    toast.show();
                                    Log.d("Mylog", originalMessage);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Mylog", error.toString());
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", idShowRoom);
                            params.put("comment", originalMessage);
                            params.put("rating",  String.valueOf((int) ratingBar.getRating()));
                            return params;
                        }
                    };
                    queue.add(strRequest);
                }
                else
                {

                    Toast toast = Toast.makeText(getActivity(),
                            "Поле отзыва должно быть заполнено!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        return view;
    }
    public static String convertToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("id", idShowRoom);
        fragment.setArguments(bundle);
        transaction.replace(R.id.main_frame, fragment);
        transaction.commit();
    }

}
