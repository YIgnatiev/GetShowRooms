package com.team.noty.getshowrooms.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team.noty.getshowrooms.R;
import com.team.noty.getshowrooms.ViewImages;
import com.team.noty.getshowrooms.adapter.DataBasesHelper;
import com.team.noty.getshowrooms.api.GetTerSetter;
import com.team.noty.getshowrooms.api.getHttpGet;
import com.team.noty.getshowrooms.customTextView.TextViewPlus;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DetailInfoFragment extends Fragment implements View.OnClickListener {


    public DetailInfoFragment() {
        // Required empty public constructor
    }

    int PERMISSION_ALL = 1;
    getHttpGet request = new getHttpGet();
    ArrayList<GetTerSetter> setDetailInformation = new ArrayList<>();
    String httpDetailInfo = "http://getshowroom.ru/api/showrooms";
    String httpDescription = "http://getshowroom.ru/api/description";
    String httpImage = "http://getshowroom.ru/uploads/showroom/";
    public String[] mThumbIds = new String[4];

    private static final String INSTAGRAM_APP_PACKAGE_ID = "com.instagram.android";
    private static final String VK_APP_PACKAGE_ID = "com.vkontakte.android";
    private static final String FACEBOOK_APP_PACKAGE_ID = "com.facebook.katana";

    DataBasesHelper dataBasesHelper;


    ImageView imageView1, imageView2, imageView3, imageView4,
            viewReviews, like, call, viewOnMap;



    String idShowRoom, nameRoom, countReview, rating, addressDb, workTimeDb,
            instagramLink, facebookLink, phoneNumber;

    TextViewPlus nameShowRoom, countReviews, instagram, facebook, station, address, workTime, description;
    RatingBar ratingBar;
    String lat, lon;

    ArrayList IdList = new ArrayList();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_info, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            idShowRoom = bundle.getString("id");
            Log.d("MyLog", "id show room " + idShowRoom);
        }

        dataBasesHelper = new DataBasesHelper(getActivity());


        imageView1 = (ImageView) view.findViewById(R.id.image1);
        imageView2 = (ImageView) view.findViewById(R.id.image2);
        imageView3 = (ImageView) view.findViewById(R.id.image3);
        imageView4 = (ImageView) view.findViewById(R.id.image4);
        viewReviews = (ImageView) view.findViewById(R.id.viewReviews);
        like = (ImageView) view.findViewById(R.id.like);
        call = (ImageView) view.findViewById(R.id.call);
        viewOnMap = (ImageView) view.findViewById(R.id.viewOnMap);

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        nameShowRoom = (TextViewPlus) view.findViewById(R.id.nameShowRoom);
        countReviews = (TextViewPlus) view.findViewById(R.id.countReviews);
        instagram = (TextViewPlus) view.findViewById(R.id.instagram);
        facebook = (TextViewPlus) view.findViewById(R.id.facebook);
        station = (TextViewPlus) view.findViewById(R.id.station);
        address = (TextViewPlus) view.findViewById(R.id.address);
        workTime = (TextViewPlus) view.findViewById(R.id.workTime);
        description = (TextViewPlus) view.findViewById(R.id.description);

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        viewReviews.setOnClickListener(this);
        like.setOnClickListener(this);
        instagram.setOnClickListener(this);
        facebook.setOnClickListener(this);
        call.setOnClickListener(this);
        countReviews.setOnClickListener(this);
        viewOnMap.setOnClickListener(this);

        getDetailInformation(idShowRoom);
        setDescription(idShowRoom);
        setImageShowRooms(idShowRoom);

        if (checkContainsDb()) {
            like.setImageResource(R.drawable.heart);
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image1:
                intentOnViewImage(0);
                break;
            case R.id.image2:
                intentOnViewImage(1);
                break;
            case R.id.image3:
                intentOnViewImage(2);
                break;
            case R.id.image4:
                intentOnViewImage(3);
                break;

            case R.id.viewReviews:
                replaceReviewsFragment(new ReviewsListFragment());
                break;
            case R.id.countReviews:
                replaceReviewsFragment(new ReviewsListFragment());
                break;

            case R.id.instagram:
                openLink(getActivity(), "https://www.instagram.com/" + instagramLink);
                break;

            case R.id.viewOnMap:
                replaceMapFragment(new MapViewFragment());
                break;

            case R.id.facebook:
                openLink(getActivity(), facebookLink);
                break;

            case R.id.like:
                if (checkContainsDb()) {
                    like.setImageResource(R.drawable.call);
                    delFromDB();
                } else {
                    like.setImageResource(R.drawable.heart);
                    insertToDB();
                }
                break;
            case R.id.call:
                String[] PERMISSIONS = {Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE};
                if (!hasPermissions(getActivity(), PERMISSIONS)) {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                } else {
                    callNumber(phoneNumber);
                }
                break;

        }
    }
    public void intentOnViewImage(int position)
    {
        Intent intent = new Intent(getActivity(), ViewImages.class);
        intent.putExtra("Image1", mThumbIds[0]);
        intent.putExtra("Image2", mThumbIds[1]);
        intent.putExtra("Image3", mThumbIds[2]);
        intent.putExtra("Image4", mThumbIds[3]);
        intent.putExtra("position", position);
        startActivity(intent);

    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void callNumber(String number) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        try {
            callIntent.setPackage("com.android.phone");
            startActivity(callIntent);
        } catch (Exception e) {
            callIntent.setPackage("com.android.server.telecom");
            startActivity(callIntent);
        }
    }

    public static void openLink(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(intent, 0);

        if (resInfo.isEmpty()) return;

        for (ResolveInfo info : resInfo) {
            if (info.activityInfo == null) continue;
            if (INSTAGRAM_APP_PACKAGE_ID.equals(info.activityInfo.packageName) ||
                    VK_APP_PACKAGE_ID.equals(info.activityInfo.packageName)
                    || FACEBOOK_APP_PACKAGE_ID.equals(info.activityInfo.packageName)) {
                Log.d("MyLog", "open " + info.activityInfo.packageName);
                intent.setPackage(info.activityInfo.packageName);
                break;
            }
        }
        activity.startActivity(intent);
    }

    public void delFromDB() {
        DataBasesHelper databaseshelper = new DataBasesHelper(getActivity());
        SQLiteDatabase db = databaseshelper.getWritableDatabase();
        db.delete("ShowRooms", "id = " + idShowRoom, null);
        databaseshelper.close();
    }

    public boolean checkContainsDb() {
        IdList = dataBasesHelper.getidRow();
        for (int i = 0; i < IdList.size(); i++) {
            if (idShowRoom.equals(IdList.get(i))) {
                return true;
            }
        }
        return false;

    }

    private void insertToDB() {

        dataBasesHelper.insertShowRooms(idShowRoom, nameRoom, rating, addressDb, workTimeDb);
        dataBasesHelper.close();
    }

    public void getDetailInformation(String id) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(request.getHttpGet(httpDetailInfo));

            Gson gson = new Gson();
            setDetailInformation = gson.fromJson(jsonArray.toString(), new TypeToken<List<GetTerSetter>>() {
            }.getType());

            if (setDetailInformation != null) {
                String title = "";
                for (int i = 0; i < setDetailInformation.size(); i++) {
                    if (setDetailInformation.get(i).getId().equals(id)) {
                        nameRoom = setDetailInformation.get(i).getName();
                        countReview = setDetailInformation.get(i).getReviews();
                        rating = setDetailInformation.get(i).getRating();
                        workTimeDb = setDetailInformation.get(i).getWorktime();
                        instagramLink = setDetailInformation.get(i).getInstagram();
                        facebookLink = setDetailInformation.get(i).getFacebook().trim();
                        phoneNumber = setDetailInformation.get(i).getTelephone();
                        lat = setDetailInformation.get(i).getLat();
                        lon = setDetailInformation.get(i).getLon();

                        nameShowRoom.setText(nameRoom);
                        countReviews.setText(countReview + " отзывов");
                        instagram.setText("Instagram: " + instagramLink);
                        Log.d("MyLog", facebookLink);
                        if (!facebookLink.equals("")) {
                            String link = getLastChar(facebookLink);
                            facebook.setText(link);
                        }
                        else
                        {
                            facebook.setText(facebookLink);
                        }
                        station.setText(setDetailInformation.get(i).getMetro());
                        address.setText(setDetailInformation.get(i).getAddress());
                        workTime.setText("Сегодня работаем " + workTimeDb);

                        ratingBar.setRating(Float.parseFloat(rating));

                        addressDb = setDetailInformation.get(i).getMetro() + ", " + setDetailInformation.get(i).getAddress();

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getLastChar(String str) {
        String newString = str;
        String[] spitStr;
        if (str.contains("http://"))
        {
            spitStr = str.split("//");
            str = spitStr[1];
        }
        else if (str.contains("https://"))
        {
            spitStr = str.split("//");
            str = spitStr[1];
        }

        char[] c;
        c = str.toCharArray();
        if (c[str.length() - 1] == '/')
        {
            newString = str.substring(0, str.length() - 1);
        }
        Log.d("MyLog", "new str " + newString);
        return newString;
    }
    public void setDescription(String id) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray("[" + request.getHttpGet(httpDescription + "?id=" + id) + "]");

            Gson gson = new Gson();
            setDetailInformation = gson.fromJson(jsonArray.toString(), new TypeToken<List<GetTerSetter>>() {
            }.getType());

            if (setDetailInformation != null) {
                description.setText(setDetailInformation.get(0).getDescription());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setImageShowRooms(String id) {
        for (int i = 1; i < 5; i++) {
            mThumbIds[i - 1] = "http://getshowroom.ru/uploads/showroom/" + id + "_" + i + ".jpg";
            Log.d("MyLog", "url " + mThumbIds[i - 1]);
        }
        Glide.with(getActivity()).load(mThumbIds[0]).placeholder(R.drawable.imgpsh_fullsize).into(imageView1);
        Glide.with(getActivity()).load(mThumbIds[1]).placeholder(R.drawable.imgpsh_fullsize).into(imageView2);
        Glide.with(getActivity()).load(mThumbIds[2]).placeholder(R.drawable.imgpsh_fullsize).into(imageView3);
        Glide.with(getActivity()).load(mThumbIds[3]).placeholder(R.drawable.imgpsh_fullsize).into(imageView4);
    }

    private void replaceReviewsFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        Bundle bundle = new Bundle();
        bundle.putString("id", idShowRoom);
        bundle.putString("name", nameRoom);
        bundle.putString("reviews", countReview);
        bundle.putString("rating", rating);

        fragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void replaceMapFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        Bundle bundle = new Bundle();
        bundle.putString("lat", lat);
        bundle.putString("lon", lon);
        fragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}
