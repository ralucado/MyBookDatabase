package com.example.pr_idi.mydatabaseexample;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{
    protected Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout mDrawerLayout;
    private Fragment fragment = null;
    private String sorting;
    private BookData bookData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Creating", "Creating main activity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set the toolbar
        sorting = "Titles";
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //change icon?
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(sorting);

        //setting the drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);

        displayFragment(mainFragment.class);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            bookData = new BookData(this);
            bookData.open();
            bookData.createBook("Metamorphosis","Kafka","Slipstream","Franz Kafka","1915","4");
            bookData.createBook("The Pillars Of The Earth","Ken Follet","Fiction","Macmillan Publishers","1989","3");
            bookData.createBook("The Name Of The Wind","Patrick Rothfuss", "Fantasy", "The Kingkiller Cronicles", "2007", "5");
            bookData.createBook("Breaking Dawn","Stephenie Meyer","Paranormal Romance","Little, Brown and Co.", "2007", "2");
            bookData.close();
            startActivity(new Intent(this, HelpActivity.class));
        }




    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(this);

    }



    // Basic method to add pseudo-random list of books so that
    // you have an example of insertion and deletion


    // Life cycle methods. Check whether it is necessary to reimplement them


    @Override
    protected void onResume() {
        if(fragment != null) fragment.onResume();
        actionBar.setTitle(sorting);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.item_navigation_drawer_titles:
                fragmentClass = mainFragment.class;
                sorting = "Titles";
                break;
            case R.id.item_navigation_drawer_authors:
                fragmentClass = mainFragment.class;
                sorting = "Authors";
                break;
            case R.id.item_navigation_drawer_categories:
                fragmentClass = SortedByCategoryFragment.class;
                sorting = "Categories";
                break;
            case R.id.item_navigation_drawer_about:
                fragmentClass = mainFragment.class;
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.item_navigation_drawer_help:
                fragmentClass = mainFragment.class;
                startActivity(new Intent(this, HelpActivity.class));
                break;
            default:
                fragmentClass = mainFragment.class;
        }
        displayFragment(fragmentClass);

        //Toast.makeText(MainActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayFragment(Class fragmentClass) {
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            Bundle args = new Bundle();
            args.putString("Sorting",sorting);
            fragment.setArguments(args);
            actionBar.setTitle(sorting);
        }catch(Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.recycler_view_frame, fragment).commit();
    }
}