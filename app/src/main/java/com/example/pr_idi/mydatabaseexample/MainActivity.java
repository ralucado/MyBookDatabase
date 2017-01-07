package com.example.pr_idi.mydatabaseexample;


import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private BookData bookData;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private void addDrawerItems() {
        String[] optionArray = {"Categories", "Author", "Title", "Help", "About"};
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

        List<Book> values = bookData.getAllBooks();

        ListView list = (ListView) findViewById(R.id.list);
        myAdapter adapter = new myAdapter(this, R.layout.row, values, this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        setUpDrawer();
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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
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
    }

    // Basic method to add pseudo-random list of books so that
    // you have an example of insertion and deletion

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")

        Book book;
        ListView list = (ListView) findViewById(R.id.list);
        ArrayAdapter<Book> adapter = (ArrayAdapter<Book>) list.getAdapter();
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
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onPause() {
        bookData.close();
        super.onPause();
    }

    public void deleteBook(Book b) {
        bookData.deleteBook(b);
    }

    public void createBook(Book b) {
        bookData.createBook(b);
    }
}