package com.example.pr_idi.mydatabaseexample;


import java.util.List;
import java.util.Random;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    protected BookData bookData;
    protected Toolbar toolbar;
    protected FloatingActionButton fab;
    protected ListView list;
    protected myAdapter adapter;
    protected SwipeRefreshLayout swipeLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private void addDrawerItems() {
        String[] optionArray = {"Categories", "Help", "About"};
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, optionArray);
        mDrawerList.setAdapter(mAdapter);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookData = new BookData(this);
        bookData.open();
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(R.color.colorAccent);
        list = (ListView) findViewById(R.id.list);
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
        setUpDrawer();
    }

    private void fillList() {
      swipeLayout.setRefreshing(true);
        List<Book> values = bookData.getAllBooks();
        adapter = new myAdapter(this, R.layout.row, values, this);
        list.setAdapter(adapter);
        swipeLayout.setRefreshing(false);
    }

    private void setUpDrawer() {
        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "send nudes", Toast.LENGTH_SHORT).show();
            }
        });
        //mDrawerList.setBackgroundColor(getResources().getColor(R.color.background_floating_material_dark));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
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
                book = bookData.createBook(newBook[nextInt * 2], newBook[nextInt * 2 + 1]);
                book.setCategory("Pan");
                // After I get the book data, I add it to the adapter
                adapter.add(book);
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
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        fillList();
        swipeLayout.setRefreshing(false);
    }
}