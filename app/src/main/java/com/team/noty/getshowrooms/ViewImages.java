package com.team.noty.getshowrooms;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

public class ViewImages extends AppCompatActivity {
    ViewPager mViewPager;
    public String[] mThumbIds = new String[4];
    CustomPagerAdapter mCustomPagerAdapter;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_view_images);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mCustomPagerAdapter = new CustomPagerAdapter(this);
        Intent intent = getIntent();
        mThumbIds[0] = intent.getStringExtra("Image1");
        mThumbIds[1] = intent.getStringExtra("Image2");
        mThumbIds[2] = intent.getStringExtra("Image3");
        mThumbIds[3] = intent.getStringExtra("Image4");
        position = intent.getIntExtra("position", 0);
        setmViewPagerImage(position);

    }

    public void setmViewPagerImage(int position)
    {
        switch (position) {
            case 0:
                mViewPager.setAdapter(mCustomPagerAdapter);
                mViewPager.setCurrentItem(0);
                mViewPager.setVisibility(View.VISIBLE);
                break;
            case 1:
                mViewPager.setAdapter(mCustomPagerAdapter);
                mViewPager.setCurrentItem(1);
                mViewPager.setVisibility(View.VISIBLE);
                break;
            case 2:
                mViewPager.setAdapter(mCustomPagerAdapter);
                mViewPager.setCurrentItem(2);
                mViewPager.setVisibility(View.VISIBLE);
                break;
            case 3:
                mViewPager.setAdapter(mCustomPagerAdapter);
                mViewPager.setCurrentItem(3);
                mViewPager.setVisibility(View.VISIBLE);
                break;
        }
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            Glide.with(ViewImages.this).load(mThumbIds[position]).placeholder(R.drawable.imgpsh_fullsize).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}

