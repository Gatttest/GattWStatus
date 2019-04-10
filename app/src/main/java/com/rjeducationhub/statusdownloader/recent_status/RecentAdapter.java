package com.rjeducationhub.statusdownloader.recent_status;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rjeducationhub.statusdownloader.R;


import java.io.File;
import java.util.ArrayList;

class RecentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> mFiles;

    RecentAdapter(Context context, ArrayList<String> Files) {
        this.context = context;
        mFiles = Files;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        ViewHolderItem viewHolder = new ViewHolderItem();
        gridView = inflater.inflate(R.layout.recents_grid_item, null);
        viewHolder.imageView = (ImageView) gridView.findViewById(R.id.img_theme);
        ImageView isVideo = (ImageView) gridView.findViewById(R.id.isVideo);
        gridView.setTag(viewHolder);

        final String filePath = mFiles.get(position);

        if (filePath.contains(".mp4")) {
            isVideo.setVisibility(View.VISIBLE);
        }

        Uri URI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(filePath));


        Glide.with(context)
                .load(URI)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .crossFade()
                .skipMemoryCache(true)
                .into(viewHolder.imageView);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(new GridView.LayoutParams(params));

        return gridView;
    }

    @Override
    public int getCount() {
        return mFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolderItem {
        ImageView imageView;
    }

}
