package com.example.pr_idi.mydatabaseexample;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by raluca on 07/01/17.
 */

public class SearchResultsActivity extends MainActivity {

    private void fillList(List<Book> values) {
        adapter = new myAdapter(this, R.layout.row, values, this);
        list.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_scrolling);
        fab = (FloatingActionButton) findViewById(R.id.plusButton);
        fab.hide();
        bookData = new BookData(this);
        bookData.open();
        adapter = new myAdapter(this, R.layout.row, Collections.EMPTY_LIST, this);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            List<Book> values = bookData.getBooksByAuthor(query);
            fillList(values);
        }
    }
}
