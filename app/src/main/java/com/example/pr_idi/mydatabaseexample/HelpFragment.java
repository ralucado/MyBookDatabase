package com.example.pr_idi.mydatabaseexample;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
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
        View v = inflater.inflate(R.layout.fragment_help, container, false);
        mImageViewPager = (ViewPager) v.findViewById(R.id.pager);
        FragmentPagerAdapter myAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return null;
            }

            @Override
            public int getCount() {
                return 0;
            }
        };
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mImageViewPager);
        return v;
    }

}
