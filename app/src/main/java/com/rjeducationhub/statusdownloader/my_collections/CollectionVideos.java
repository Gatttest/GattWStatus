package com.rjeducationhub.statusdownloader.my_collections;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.rjeducationhub.statusdownloader.R;
import com.rjeducationhub.statusdownloader.recent_status.ViewRecentStatusActivity;

import java.io.File;
import java.util.ArrayList;

public class CollectionVideos extends Fragment {

    GridView grid_view;
    CollectionAdapter downloaderAdapter;
    private File[] FilePathStrings;
    ArrayList<String> VideoArray = new ArrayList<>();
    Boolean isFileAvalable = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frags_collection, container, false);

        grid_view = (GridView) view.findViewById(R.id.grid_view);

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + getString(R.string.save_directory_name) + File.separator);
        if (file.isDirectory()) {
            FilePathStrings = file.listFiles();
            if (FilePathStrings.length > 1) {
                isFileAvalable = true;
            } else {
                isFileAvalable = false;
            }
        } else {
            isFileAvalable = false;
        }

        if (isFileAvalable) {
            if (FilePathStrings.length > 1) {
                for (int i = 0; i < FilePathStrings.length; i++) {
                    if (FilePathStrings[i].getAbsolutePath().contains(".mp4")) {
                        VideoArray.add(FilePathStrings[i].getAbsolutePath());
                    }
                }
            }
        }

        if (isFileAvalable) {
            if (FilePathStrings.length > 1) {
                downloaderAdapter = new CollectionAdapter(getActivity(), VideoArray);
                grid_view.setAdapter(downloaderAdapter);
            }
        }

        if (isFileAvalable) {
            if (FilePathStrings.length > 1) {
                grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), ViewRecentStatusActivity.class);
                        intent.putExtra("FileArray", VideoArray);
                        intent.putExtra("Position", position);
                        startActivity(intent);
                    }
                });
            }
        }

        return view;
    }

}
