package com.example.pr_idi.mydatabaseexample;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SortedByCategoryFragment extends Fragment {

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private FloatingActionButton myFloatingActionButton;
    private BookData bookData;

    public SortedByCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_sorted_by_category, container, false);
        myRecyclerView = (RecyclerView) v.findViewById(R.id.recycleview);

        myLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        bookData = new BookData(getActivity().getApplicationContext());
        bookData.open();
        ArrayList<Book> myTempArrayList = (ArrayList<Book>) bookData.getAllBooks("Categories");
        bookData.close();
        // specify an adapter (see also next example)
        myAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), myTempArrayList, bookData);
        myRecyclerView.setAdapter(myAdapter);

        myFloatingActionButton = (FloatingActionButton) v.findViewById(R.id.plusButton);
        myFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(),AddNewBookActivity.class);
                startActivity(in);
            }
        });

        // falta comprovar que el hide i el show funcionen adequadament
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 ||dy<0 && myFloatingActionButton.isShown())
                {
                    myFloatingActionButton.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    myFloatingActionButton.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return v;
    }


}
