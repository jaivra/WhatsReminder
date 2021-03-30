package com.jaivra.whatsreminder.gui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.database.MyDbHelper;
import com.jaivra.whatsreminder.gui.fragment.HistoryTabFragment;
import com.jaivra.whatsreminder.gui.fragment.PeriodicReminderTabFragment;
import com.jaivra.whatsreminder.gui.fragment.SingleReminderTabFragment;
import com.jaivra.whatsreminder.gui.listener.MyChangeListener;
import com.jaivra.whatsreminder.inc.gui.MyPagerAdapter;
import com.jaivra.whatsreminder.model.ContactMessage;
import com.jaivra.whatsreminder.model.ProgrammedMessage;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements MyChangeListener {

    private MyPagerAdapter myPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.viewPager = findViewById(R.id.pager);
        this.tabLayout = findViewById(R.id.tab_layout);
        this.myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        this.addButton = findViewById(R.id.add_button);

        initTabs();

        MyDbHelper dbHelper = new MyDbHelper(this);
        for (ProgrammedMessage programmedMessage : dbHelper.getAllSingleReminders())
            System.out.println("PROGRAMMED MESSAGE " + programmedMessage.getBody());

        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddReminderActivity.class);
            startActivityForResult(intent, 1);
        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            int result = data.getIntExtra(AddReminderActivity.ADD_REMINDER_RESULT, 0);
            System.out.println("RESULT : " + result);

            if (result == AddReminderActivity.ADD_REMINDER)
                myPagerAdapter.refreshAll();
        }
    }


    @Override
    public void onChange() {
        myPagerAdapter.refreshAll();
    }
}