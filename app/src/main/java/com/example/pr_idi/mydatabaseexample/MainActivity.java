package com.example.pr_idi.mydatabaseexample;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener{
    protected BookData bookData;
    protected Toolbar toolbar;
    protected FloatingActionButton fab;
    protected ListView list;
    protected myAdapter adapter;
    protected SwipeRefreshLayout swipeLayout;
    private DrawerLayout mDrawerLayout;
    private ActionBar actionBar;
    protected String sorting;
    protected ArrayList<Book> values = new ArrayList<>();

    private FloatingActionButton myFloatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sorting = "Titles";
        //set the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //change icon?
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //setting the drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);


        //setting the main list
        bookData = new BookData(this);
        bookData.open();
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(R.color.colorAccent);
        list = (ListView) findViewById(R.id.list);
        adapter = new myAdapter(this, R.layout.row, values, this);
        list.setAdapter(adapter);
        fillList();
        list.setOnItemClickListener(this);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        list.setEmptyView(emptyText);
        fab = (FloatingActionButton) findViewById(R.id.plusButton);
        list.setOnScrollListener(new ListView.OnScrollListener() {
            int lastFirstItem = 0;
            boolean down = false;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(list != null && list.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = list.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = list.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeLayout.setEnabled(enable);
                if (lastFirstItem > firstVisibleItem) down = true;
                else if (lastFirstItem < firstVisibleItem) down = false;
                lastFirstItem = firstVisibleItem;
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (totalItemCount != 0 && firstVisibleItem > 0 && !down && fab.isShown())
                    fab.hide();
                else fab.show();
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && down) fab.show();
            }
        });
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        final Intent in = new Intent(this,AddNewBookActivity.class);
        myFloatingActionButton = (FloatingActionButton) findViewById(R.id.plusButton);
        myFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(in);
            }
        });

    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void fillList() {
        values.clear();
        values.addAll(bookData.getAllBooks(sorting));
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }

    // Basic method to add pseudo-random list of books so that
    // you have an example of insertion and deletion

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")

        Book book;
        switch (view.getId()) {
            case R.id.plusButton:
                String[] newBook = new String[]{"Miguel Strogoff", "Jules Verne", "Ulysses", "James Joyce", "Don Quijote", "Miguel de Cervantes", "Metamorphosis", "Kafka", "send", "nudes"};
                int nextInt = new Random().nextInt(5);
                // save the new book to the database
                book = bookData.createBook("no","no","no","no",13);
                book.setCategory("Pan");
                // After I get the book data, I add it to the adapter
                fillList();
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int integer, long l) {

        Toast.makeText(MainActivity.this, "send nudes", Toast.LENGTH_SHORT).show();
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
        adapter.notifyDataSetInvalidated();
        //fillList(); //DANGER THIS FUCK ALL UP NEVER UNCOMMENT OR YOU WILL WANT TO DIE
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onPause() {
        bookData.close();
        super.onPause();
    }

    public boolean deleteBook(Book b) {
        return bookData.deleteBook(b);
    }

    public void createBook(Book b) {
        bookData.createBook(b);
    }
    public void changeRating(Book b, float value){
        bookData.changeRating(b,value);
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
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        fillList();
        swipeLayout.setRefreshing(false);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_navigation_drawer_titles:
                menuItem.setChecked(true);
                sorting =  menuItem.getTitle().toString();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.item_navigation_drawer_authors:
                menuItem.setChecked(true);
                sorting =  menuItem.getTitle().toString();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.item_navigation_drawer_categories:
                menuItem.setChecked(true);

                Fragment fragment = new SortedByCategoryFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.swipe_refresh_layout, fragment)
                        .commit();

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.item_navigation_drawer_about:
                menuItem.setChecked(true);
                Toast.makeText(MainActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.item_navigation_drawer_help:
                menuItem.setChecked(true);
                Toast.makeText(MainActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }
}