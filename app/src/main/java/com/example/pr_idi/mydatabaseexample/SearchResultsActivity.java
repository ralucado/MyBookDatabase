package com.example.pr_idi.mydatabaseexample;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static java.lang.System.out;

/**
 * Created by raluca on 07/01/17.
 */

public class SearchResultsActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    private ActionBar actionBar;
    protected BookData bookData;
    protected FloatingActionButton fab;
    protected ListView list;
    protected myAdapter adapter;
    protected String sorting;
    protected ArrayList<Book> values = new ArrayList<>();
    private String lastQuery = "";
    private FuzzyScore fuzzyCalculator;
    private void fillList(){
        values.clear();
        List<Book> aux = bookData.getAllBooks("Author");
        for (Book book : aux) {
            String title = book.getTitle();
            if(fuzzyCalculator.fuzzyScore(title,lastQuery) > title.length()/2) values.add(book);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);
        fuzzyCalculator = new FuzzyScore(Locale.getDefault());
        sorting = "Titles";
        fab = (FloatingActionButton) findViewById(R.id.plusButton);
        fab.hide();
        bookData = new BookData(this);
        bookData.open();
        list = (ListView) findViewById(R.id.list);
        adapter = new myAdapter(this, R.layout.row, values, bookData);
        list.setAdapter(adapter);
        fillList();
        list.setAdapter(adapter);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        list.setEmptyView(emptyText);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setTitle("Results for \""+getIntent().getStringExtra(SearchManager.QUERY)+"\"");
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            lastQuery = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            fillList();
        }
    }

    @Override
    protected void onResume() {
        fillList();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    NavUtils.navigateUpTo(this, upIntent);
                    finish();
                } else {
                    finish();
                }
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

}
