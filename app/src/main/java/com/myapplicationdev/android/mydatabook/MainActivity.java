package com.myapplicationdev.android.mydatabook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private DrawerItem[] drawerItems;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    DrawerItemAdapter drawerItemArrayAdapter;
    String currentTitle;
    ActionBar ab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.left_drawer);

        drawerItems = new DrawerItem[]{new DrawerItem("Bio", android.R.drawable.ic_menu_info_details), new DrawerItem("Vaccination", android.R.drawable.ic_menu_edit),
        new DrawerItem("Anniversary", android.R.drawable.ic_menu_my_calendar), new DrawerItem("About Us", android.R.drawable.star_on)};
        ab = getSupportActionBar();

        drawerItemArrayAdapter = new DrawerItemAdapter(this, R.layout.drawer_item_row, drawerItems);
        drawerList.setAdapter(drawerItemArrayAdapter);

        // Set the list's click listener
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Fragment fragment = null;
                if (position == 0) {
                    fragment = new BioFragment();
                }
                else if (position == 1){
                    fragment = new VaccinationFragment();
                }
                else if (position == 2){
                    fragment = new AnniversaryFragment();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                    startActivity(intent);
                    return;
                }

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.content_frame, fragment);
                trans.commit();

                // Highlight the selected item,
                //  update the title, and close the drawer
                drawerList.setItemChecked(position, true);
                currentTitle = drawerItems[position].getTitle();
                ab.setTitle(currentTitle);
                drawerLayout.closeDrawer(drawerList);
            }
        });
    }
}