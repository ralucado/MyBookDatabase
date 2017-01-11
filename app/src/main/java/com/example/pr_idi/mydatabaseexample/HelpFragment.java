package com.example.pr_idi.mydatabaseexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {
ViewPager mImageViewPager;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int position = (int) getArguments().get("position");
        switch (position) {
            case 0:
                return(ViewGroup) inflater.inflate(R.layout.fragment_help_1, container, false);
            case 1:
                return(ViewGroup) inflater.inflate(R.layout.fragment_help_2, container, false);
            case 2:
                return(ViewGroup) inflater.inflate(R.layout.fragment_help_4, container, false);
            case 3:
                return(ViewGroup) inflater.inflate(R.layout.fragment_help_3, container, false);
            case 4:
                return (ViewGroup) inflater.inflate(R.layout.fragment_help_5, container, false);
            case 5:
                return (ViewGroup) inflater.inflate(R.layout.fragment_help_6, container, false);
            default:
                return (ViewGroup) inflater.inflate(R.layout.fragment_help_6, container, false);
        }
    }

}
