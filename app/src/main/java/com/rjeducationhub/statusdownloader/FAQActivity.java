package com.rjeducationhub.statusdownloader;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    Toolbar toolbar;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("FAQs");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
               /* .addTestDevice(getResources().getString(R.string.test_device_id))*/
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void prepareListData() {

        Boolean isGreateThenM;

        isGreateThenM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Why all status are not display in recent?");
        listDataHeader.add("Why recent status removed?");
        listDataHeader.add("How to download status?");
        if (isGreateThenM) {
            listDataHeader.add("Why Storage permission is required?");
        }

        List<String> f1 = new ArrayList<>();
        List<String> f2 = new ArrayList<>();
        List<String> f3 = new ArrayList<>();
        List<String> f4 = new ArrayList<>();

        f1.add("To display status in our application you need to open status in whatsapp once.");
        f2.add("Status are removed automatically from recent when user removed his/her status from whatsapp.");
        f3.add("Please click on status in our app and there are option to download status in top right corner.");

        if (isGreateThenM) {
            f4.add("We need storage permission to save status on your device.");
        }

        listDataChild.put(listDataHeader.get(0), f1);
        listDataChild.put(listDataHeader.get(1), f2);
        listDataChild.put(listDataHeader.get(2), f3);
        if (isGreateThenM) {
            listDataChild.put(listDataHeader.get(3), f4);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
    }
}
