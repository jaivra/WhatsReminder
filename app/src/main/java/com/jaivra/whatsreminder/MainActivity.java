package com.jaivra.whatsreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.jaivra.whatsreminder.gui.fragment.HistoryTabFragment;
import com.jaivra.whatsreminder.gui.fragment.PeriodicReminderTabFragment;
import com.jaivra.whatsreminder.gui.fragment.SingleReminderTabFragment;
import com.jaivra.whatsreminder.inc.gui.MyPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private MyPagerAdapter myPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.viewPager = findViewById(R.id.pager);
        this.tabLayout = findViewById(R.id.tab_layout);
        this.myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        initTabs();


        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initTabs() {
        myPagerAdapter.addFragment(new SingleReminderTabFragment());
        myPagerAdapter.addFragment(new PeriodicReminderTabFragment());
        myPagerAdapter.addFragment(new HistoryTabFragment());
    }

    private void toUse() {
        String url = "https://api.whatsapp.com/send?phone=" + "+393477063199" + "&text=" + "voglio inviare questo messaggio";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}