package com.jaivra.whatsreminder.gui;

import static android.Manifest.permission.READ_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
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
import com.jaivra.whatsreminder.model.ProgrammedMessage;

public class MainActivity extends AppCompatActivity implements MyChangeListener, ActivityResultCallback<Boolean>{

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
        
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), this);

        addButton.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS) == PERMISSION_GRANTED) {

               launchAddReminderActivity();
            } else {
                activityResultLauncher.launch(READ_CONTACTS);
            }
        });
    }

    private void launchAddReminderActivity() {
        Intent intent = new Intent(this, AddReminderActivity.class);
        startActivityForResult(intent, 1);
    }

    private void initTabs() {
        myPagerAdapter.addFragment(new SingleReminderTabFragment());
        myPagerAdapter.addFragment(new PeriodicReminderTabFragment());
        myPagerAdapter.addFragment(new HistoryTabFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            int result = data.getIntExtra(AddReminderActivity.ADD_REMINDER_RESULT, 0);

            if (result == AddReminderActivity.ADD_REMINDER)
                myPagerAdapter.refreshAll();
        }
    }


    @Override
    public void onChange() {
        myPagerAdapter.refreshAll();
    }

    @Override
    public void onActivityResult(Boolean result) {
        if (result)
            launchAddReminderActivity();
    }
}