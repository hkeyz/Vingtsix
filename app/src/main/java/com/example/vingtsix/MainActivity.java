package com.example.vingtsix;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    TabLayout mTabLayout;
    TabItem curMusic;
    TabItem allMusic;
    TabItem playList;
    ViewPager mPager;
    PagerController mPagerController;
    ImageView recherche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Tab Music");

        mTabLayout = findViewById(R.id.tabLayout);
        curMusic = findViewById(R.id.currentMusic);
        allMusic = findViewById(R.id.allmusic);
        playList = findViewById(R.id.playlist);
        mPager = findViewById(R.id.viewpager);
        recherche = findViewById(R.id.recherche);

        mPagerController = new PagerController(getSupportFragmentManager(), mTabLayout.getTabCount());
        mPager.setAdapter(mPagerController);



        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

//    public void createDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        final View view = getLayoutInflater().inflate(R.layout.dialog_recherche, null);
//        builder.setTitle(R.string.rechercher);
//        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                EditText et_recherche =(EditText) view.findViewById(R.id.et_recherche);
//                String recherche = et_recherche.getText().toString().trim();
//                if (recherche.length()>0){
//
//                }else {
//                    Toast.makeText(MainActivity.this, "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

}
