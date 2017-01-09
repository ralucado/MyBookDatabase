package com.example.pr_idi.mydatabaseexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SortedByCategoryFragment extends Fragment {

    private RecyclerView myRecyclerVeiw;

    public SortedByCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_sorted_by_category, container, false);
        myRecyclerVeiw = (RecyclerView) v.findViewById(R.id.recycleview);
        return v;
    }

}
