package com.rjeducationhub.statusdownloader.my_collections;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rjeducationhub.statusdownloader.R;


import java.io.File;
import java.util.ArrayList;

class ViewPagerAdepterForMyCollection extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mFileArray;

    ViewPagerAdepterForMyCollection(Context context, ArrayList<String> FileArray) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFileArray = FileArray;
    }

    @Override
    public int getCount() {
        return mFileArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        ImageView isVideo = (ImageView) itemView.findViewById(R.id.isVideo);
        final String filePath = mFileArray.get(position);

        if (filePath.contains(".mp4")) {
            isVideo.setVisibility(View.VISIBLE);
            isVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri VideoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", new File(filePath));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.setDataAndType(VideoURI, "video/*");
                    try {
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        Uri URI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", new File(filePath));
        Glide.with(mContext)
                .load(URI)
                .crossFade()
                .into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
