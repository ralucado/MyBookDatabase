package com.example.pr_idi.mydatabaseexample;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by raluca on 10/01/17.
 */

public class mainFragment extends Fragment {
    protected BookData bookData;
    protected FloatingActionButton fab;
    protected ListView list;
    protected myAdapter adapter;
    protected String sorting;
    protected ArrayList<Book> values = new ArrayList<>();

    public mainFragment(){
        //required
    }

    private void fillList() {
        bookData.open();
        values.clear();
        values.addAll(bookData.getAllBooks(sorting));
        adapter.notifyDataSetChanged();
        bookData.close();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.content_scrolling, container, false);

        //setting the main list
        sorting = "Titles";
        bookData = new BookData(getActivity().getApplicationContext());
        bookData.open();

        list = (ListView) v.findViewById(R.id.list);
        adapter = new myAdapter(getActivity(), R.layout.row, values, bookData);
        list.setAdapter(adapter);
        fillList();

        TextView emptyText = (TextView) v.findViewById(android.R.id.empty);
        list.setEmptyView(emptyText);
        fab = (FloatingActionButton) v.findViewById(R.id.plusButton);
        list.setOnScrollListener(new ListView.OnScrollListener() {
            int lastFirstItem = 0;
            boolean down = false;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
        final Intent in = new Intent(getActivity(),AddNewBookActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(in);
            }
        });
        bookData.close();
        return v;
    }

}
