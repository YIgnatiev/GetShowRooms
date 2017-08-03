package com.team.noty.getshowrooms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.team.noty.getshowrooms.fragment.FavoritesFragment;
import com.team.noty.getshowrooms.fragment.LikedFragment;
import com.team.noty.getshowrooms.fragment.MapViewFragment;
import com.team.noty.getshowrooms.fragment.SearchFragment;

public class FragmentActivity extends AppCompatActivity implements View.OnClickListener {


    TabLayout tabLayout;
    AppCompatImageView back, getshowroom;
    FragmentManager fragmentManager;
    LinearLayout besideContent, bestContent, likedContent, searchContent;
    int tab_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        tab_position = intent.getIntExtra("tab", 0);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        back = (AppCompatImageView) findViewById(R.id.back);
        getshowroom = (AppCompatImageView) findViewById(R.id.getShowroom);

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_beside));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_best));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_liked));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.layout_tab_search));

        besideContent = (LinearLayout) tabLayout.findViewById(R.id.besideContent);
        bestContent = (LinearLayout) tabLayout.findViewById(R.id.bestContent);
        likedContent = (LinearLayout) tabLayout.findViewById(R.id.likedContent);
        searchContent = (LinearLayout) tabLayout.findViewById(R.id.searchContent);

        besideContent.setOnClickListener(this);
        bestContent.setOnClickListener(this);
        likedContent.setOnClickListener(this);
        searchContent.setOnClickListener(this);

        tabLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        checkSelectedTab(tab_position);

        back.setOnClickListener(this);
        getshowroom.setOnClickListener(this);

    }

    public  void checkSelectedTab(int i)
    {
        switch (i)
        {
            case 0:
                tabLayout.getTabAt(0).select();
                replaceFragment(new MapViewFragment());
                break;
            case 1:
                tabLayout.getTabAt(1).select();
                replaceFragment(new FavoritesFragment());
                break;
            case 2:
                tabLayout.getTabAt(2).select();
                replaceFragment(new LikedFragment());
                break;
            case 3:
                tabLayout.getTabAt(3).select();
                replaceFragment(new SearchFragment());
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {

        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        if(fragmentManager.getBackStackEntryCount() != 1) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.getShowroom:
                finish();
                break;

            case R.id.besideContent:
                tabLayout.getTabAt(0).select();
                replaceFragment(new MapViewFragment());
                break;
            case R.id.bestContent:
                tabLayout.getTabAt(1).select();
                replaceFragment(new FavoritesFragment());
                break;
            case R.id.likedContent:
                tabLayout.getTabAt(2).select();
                replaceFragment(new LikedFragment());
                break;
            case R.id.searchContent:
                tabLayout.getTabAt(3).select();
                replaceFragment(new SearchFragment());
                break;
        }
    }
}
