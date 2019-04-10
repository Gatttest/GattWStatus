package com.rjeducationhub.statusdownloader;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ViewStoryActivity extends AppCompatActivity {

    ViewPagerAdepter mCustomPagerAdapter;
    ViewPager mViewPager;
    Integer Current;
    File[] FilePathStrings;
    Toolbar toolbar;
    String FolderPath;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.view_status));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FolderPath = Environment.getExternalStorageDirectory() + File.separator + getString(R.string.save_directory_name) + File.separator;

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + ".Statuses" + File.separator);
        if (file.isDirectory()) {
            FilePathStrings = file.listFiles();
        }
        Current = this.getIntent().getIntExtra("current", 0);

        mCustomPagerAdapter = new ViewPagerAdepter(this, FilePathStrings);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setPageTransformer(true, new RotateDownTransformer());
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setCurrentItem(Current, true);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Current = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(getResources().getString(R.string.test_device_id)).build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();

        } else if (id == R.id.repost) {

            Current = mViewPager.getCurrentItem();
            Uri share_uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(FilePathStrings[Current].getAbsolutePath()));
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            if (FilePathStrings[Current].getAbsolutePath().contains(".mp4")) {
                whatsappIntent.setType("video/*");
            } else {
                whatsappIntent.setType("image/*");
            }
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, share_uri);
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "To download this app https://play.google.com/store/apps/details?id=" + getPackageName());
            try {
                startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "Whatsapp have not been installed", Toast.LENGTH_LONG).show();
            }


        } else if (id == R.id.download) {

            File source = new File(FilePathStrings[Current].getAbsolutePath());
            String filename = FilePathStrings[Current].getAbsolutePath().substring(FilePathStrings[Current].getAbsolutePath().lastIndexOf("/") + 1);
            String destinationPath = FolderPath + filename;
            File destination = new File(destinationPath);
            try {
                copyFile(source, destination);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(destination));
            sendBroadcast(intent);
            Toast.makeText(getApplicationContext(), "Status save successfully to " + destinationPath, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.share) {

            Uri share_uri = Uri.fromFile(new File(FilePathStrings[Current].getAbsolutePath()));

            final Intent shareIntent = new Intent(Intent.ACTION_SEND);

            if (FilePathStrings[Current].getAbsolutePath().contains(".mp4")) {
                shareIntent.setType("video/*");
            } else {
                shareIntent.setType("image/*");
            }
            shareIntent.putExtra(Intent.EXTRA_STREAM, share_uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "To download this app https://play.google.com/store/apps/details?id=" + getPackageName());

            startActivity(Intent.createChooser(shareIntent, "Share image using"));

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean copyFile(File src, File dst) throws IOException {
        if (src.getAbsolutePath().equals(dst.getAbsolutePath())) {
            return true;
        } else {
            InputStream is = new FileInputStream(src);
            OutputStream os = new FileOutputStream(dst);
            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) > 0) {
                os.write(buff, 0, len);
            }
            is.close();
            os.close();
        }
        return true;
    }
}
