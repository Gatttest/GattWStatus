package com.rjeducationhub.statusdownloader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import com.rjeducationhub.statusdownloader.recent_status.RecentStatusActivity;
import com.rjeducationhub.statusdownloader.my_collections.MyCollectionActivityActivity;
import com.rjeducationhub.statusdownloader.horizontal_listview.TwoWayAdapterView;
import com.rjeducationhub.statusdownloader.horizontal_listview.TwoWayGridView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
public class MainActivity extends AppCompatActivity {

    TwoWayGridView gridForWhatsapp;
    File[] listFile;
    Animation JumpAnimation;
    ImageView imageThumb;
    Toolbar toolbar;
    Button RecentStatus;
    List<String> permissionsNeeded = new ArrayList<>();
    List<String> permissionsList = new ArrayList<>();
    AlertDialog.Builder alert_permission;
    Boolean isFileAvalable = false;
    TextView txtNoData;
    ImageView ImageIcon;
    InterstitialAd mInterstitialAd;
    AdRequest adRequestint;
    NativeExpressAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

        gridForWhatsapp = (TwoWayGridView) findViewById(R.id.gridForWhatsapp);
        RecentStatus = (Button) findViewById(R.id.RecentStatus);
        txtNoData = (TextView) findViewById(R.id.txtNoData);
        ImageIcon = (ImageView) findViewById(R.id.ImageIcon);

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        File path = new File(Environment.getExternalStorageDirectory() + File.separator + getString(R.string.save_directory_name) + File.separator);
        if (!path.exists()) {
            path.mkdirs();
        }
        setUpGridView();
        JumpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.jump);
        gridForWhatsapp.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(TwoWayAdapterView<?> parent, View view, final int position, long id) {
                imageThumb = (ImageView) view.findViewById(R.id.thumbImage);
                imageThumb.startAnimation(JumpAnimation);
                JumpAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent i = new Intent(getApplicationContext(), ViewStoryActivity.class);
                        i.putExtra("current", position);
                        startActivity(i);
                        showInterstitial();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        MobileAds.initialize(this, "ca-app-pub-7096183949138416~8069357321");
        adView = (NativeExpressAdView) findViewById(R.id.NativeadView);
        AdRequest adRequestnative = new AdRequest.Builder().addTestDevice(getResources().getString(R.string.test_device_id)).build();
        adView.loadAd(adRequestnative);
        RecentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RecentStatusActivity.class);
                startActivity(intent);
                showInterstitial();
            }
        });

       // loadInterstitialAd();
    }

    private void showInterstitial() {
        if (mInterstitialAd != null) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
        loadInterstitialAd();
    }
    private void setUpGridView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + ".Statuses" + File.separator);
                if (file.isDirectory()) {
                    listFile = file.listFiles();
                    isFileAvalable = true;
                } else {
                    isFileAvalable = false;
                }

                if (isFileAvalable) {
                    ImageAdapter imageAdapter = new ImageAdapter();
                    gridForWhatsapp.setAdapter(imageAdapter);
                } else {
                    txtNoData.setVisibility(View.VISIBLE);
                    gridForWhatsapp.setVisibility(View.GONE);
                    RecentStatus.setVisibility(View.GONE);
                }


            } else {
                setuppermission();
            }
        } else {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + ".Statuses" + File.separator);
            if (file.isDirectory()) {
                listFile = file.listFiles();
                isFileAvalable = true;
            } else {
                isFileAvalable = false;
            }

            if (isFileAvalable) {
                ImageAdapter imageAdapter = new ImageAdapter();
                gridForWhatsapp.setAdapter(imageAdapter);
            } else {
                txtNoData.setVisibility(View.VISIBLE);
                gridForWhatsapp.setVisibility(View.GONE);
                RecentStatus.setVisibility(View.GONE);
            }
        }

    }

    private void setuppermission() {
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                alert_permission = new AlertDialog.Builder(MainActivity.this);
                alert_permission.setMessage(message);
                alert_permission.setCancelable(false);
                alert_permission.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]), 1);
                            }
                        });

                alert_permission.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                alert_permission.show();
                return;
            }
            ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]), 1);

        }
    }

    private class ImageAdapter extends BaseAdapter {

        ImageAdapter() {
        }

        public int getCount() {
            return listFile.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("InflateParams")
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View view;
            view = new View(getApplicationContext());
            LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.storyitem, null);
            holder.imageview = (ImageView) view.findViewById(R.id.thumbImage);
            holder.isVideo = (ImageView) view.findViewById(R.id.isVideo);
            view.setTag(holder);

            if (listFile[position].getAbsolutePath().contains(".mp4")) {
                holder.isVideo.setVisibility(View.VISIBLE);

            }

            Glide.with(getApplicationContext())
                    .load(listFile[position])
                    .centerCrop()
                    .crossFade()
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(holder.imageview);

            return view;
        }
    }

    private class ViewHolder {
        ImageView imageview, isVideo;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.collection) {
            Intent intent = new Intent(getApplicationContext(), MyCollectionActivityActivity.class);
            startActivity(intent);
            showInterstitial();
        } else if (id == R.id.setting) {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            showInterstitial();
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                } else {
                    setUpGridView();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loadInterstitialAd() {
        adRequestint = new AdRequest.Builder()
               // .addTestDevice(getResources().getString(R.string.test_device_id))
                .build();
        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_interstitial));
        mInterstitialAd.loadAd(adRequestint);
    }
}

