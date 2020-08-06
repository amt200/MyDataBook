package com.myapplicationdev.android.mydatabook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerItem[] drawerItems;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    DrawerItemAdapter drawerItemArrayAdapter;
    String currentTitle;
    ActionBar ab;
    private View drawer;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.left_drawer);

        fragment = new BioFragment();
        currentTitle = "Bio";
        Button btn = findViewById(R.id.btnEdit);

        drawerItems = new DrawerItem[]{new DrawerItem("Bio", android.R.drawable.ic_menu_info_details), new DrawerItem("Vaccination", android.R.drawable.ic_menu_edit),
        new DrawerItem("Anniversary", android.R.drawable.ic_menu_my_calendar), new DrawerItem("About Us", android.R.drawable.star_on)};
        ab = getSupportActionBar();

        drawerItemArrayAdapter = new DrawerItemAdapter(this, R.layout.drawer_item_row, drawerItems);
        drawerList.setAdapter(drawerItemArrayAdapter);

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        final TextView textView = dialog.findViewById(R.id.tvEdit);
        final EditText editText = dialog.findViewById(R.id.etData);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnOK = dialog.findViewById(R.id.btnOk);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().length() < 1){
                    Toast.makeText(MainActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(currentTitle, editText.getText().toString());
                editor.apply();
                displayInFragment();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Edit "+currentTitle);
                dialog.show();
            }
        });
        // Set the list's click listener
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                fragment = null;
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

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, 	  /* DrawerLayout object */
                R.string.app_name, /* "open drawer" description */
                R.string.bio /* "close drawer" description */
        ) {

            /** Would be called when a drawer has completely closed */
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                ab.setTitle(currentTitle);
            }

            /** Would be called when a drawer has completely open */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawer = drawerView;
                ab.setTitle("Make a selection");
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(drawerToggle);
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //action
                if (drawer != null) {
                    drawerLayout.openDrawer(drawer);
                }
            }
        });

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync toggle state so the indicator is shown properly.
        //  Have to call in onPostCreate()
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
    private void displayInFragment(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String data = preferences.getString(currentTitle, "");
        TextView tv = null;
        if(currentTitle.equals("Bio")){
            fragment = new BioFragment();
            tv = (TextView) fragment.getView().findViewById(R.id.tvBio);
        }
        else if(currentTitle.equals("Vaccination")){
            tv = (TextView) fragment.getView().findViewById(R.id.tvVaccine);
        }
        else if(currentTitle.equals("About Us")){
            tv = (TextView) fragment.getView().findViewById(R.id.tvAbout);
        }
        else {
            tv = (TextView) fragment.getView().findViewById(R.id.tvAnni);
        }
        if(tv != null){
            tv.setText(data);
        }
    }

}