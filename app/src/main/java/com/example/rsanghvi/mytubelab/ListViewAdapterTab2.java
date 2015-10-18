package com.example.rsanghvi.mytubelab;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rsanghvi on 10/17/15.
 */
public class ListViewAdapterTab2 extends ArrayAdapter<VideoItem>{

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<VideoItem> VideoItemList;
    private SparseBooleanArray mSelectedItemsIds;

    public ListViewAdapterTab2(Context context, int resource, List<VideoItem> objects) {
        super(context, resource, objects);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.VideoItemList = objects;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        TextView titletitle;
        TextView publishedDate;
        ImageView thumbnail;
        TextView views;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.tab_fragment_2, null);
            // Locate the TextViews in listview_item.xml
            holder.thumbnail = (ImageView)view.findViewById(R.id.video_thumbnail);
            holder.titletitle = (TextView)view.findViewById(R.id.video_title);
            holder.views = (TextView)view.findViewById(R.id.video_views);
            holder.publishedDate = (TextView)view.findViewById(R.id.video_published_date);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Capture position and set to the TextViews
        holder.titletitle.setText(VideoItemList.get(position).getTitle());
        holder.publishedDate.setText(VideoItemList.get(position).getPublishedAt());
        holder.views.setText(VideoItemList.get(position)
                .getViewCount().toString());

        // Capture position and set to the ImageView
        Picasso.with(context).load(VideoItemList.get(position).getThumbnailURL()).into(holder.thumbnail);
        return view;
    }

    @Override
    public void remove(VideoItem object) {
        VideoItemList.remove(object);
        notifyDataSetChanged();
    }

    public List<VideoItem> getVideoItem() {
        return VideoItemList;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
