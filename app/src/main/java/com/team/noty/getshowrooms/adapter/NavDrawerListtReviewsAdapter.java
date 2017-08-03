package com.team.noty.getshowrooms.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;

import com.team.noty.getshowrooms.R;
import com.team.noty.getshowrooms.customTextView.TextViewPlus;

import java.util.ArrayList;


public class NavDrawerListtReviewsAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	public NavDrawerListtReviewsAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_reviews_item, null);
        }

		TextViewPlus txtTitle = (TextViewPlus) convertView.findViewById(R.id.reviews);
		RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);

        txtTitle.setText(navDrawerItems.get(position).getTitle());
		ratingBar.setRating(Float.parseFloat(navDrawerItems.get(position).getRating()));




		return convertView;
	}

}
