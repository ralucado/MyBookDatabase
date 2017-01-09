package com.example.pr_idi.mydatabaseexample;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class SearchResultsActivity extends MainActivity {
    private String lastQuery = "";
    private FuzzyScore fuzzyCalculator;
    private void fillList(){
        values.clear();
        List<Book> aux = bookData.getAllBooks("Titles");
        for (Book book : aux) {
            String author = book.getAuthor();
            if(fuzzyCalculator.fuzzyScore(author,lastQuery) > author.length()/2) values.add(book);
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
        adapter = new myAdapter(this, R.layout.row, values, this);
        list.setAdapter(adapter);
        fillList();
        list.setAdapter(adapter);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        list.setEmptyView(emptyText);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(R.color.colorAccent);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list.setOnScrollListener(new ListView.OnScrollListener() {
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

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        fillList();
        swipeLayout.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

}
