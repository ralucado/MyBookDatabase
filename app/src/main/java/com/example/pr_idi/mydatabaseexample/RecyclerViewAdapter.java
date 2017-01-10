package com.example.pr_idi.mydatabaseexample;

import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private ArrayList<Book> myBooks;

    public RecyclerViewAdapter(ArrayList<Book> list) {
        myBooks = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.advanced_row, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book tmp = myBooks.get(position);
        holder.title.setText(tmp.getTitle());
        holder.author.setText(tmp.getAuthor());
        holder.category.setText(tmp.getCategory());
        holder.publisher.setText(tmp.getPublisher());
        holder.year.setText(String.valueOf(tmp.getYear()));
        holder.ratingBar.setRating(tmp.getPersonal_evaluation());
        holder.category.setText(tmp.getCategory());
    }

    @Override
    public int getItemCount() {
        return myBooks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView author;
        public TextView category;
        public TextView publisher;
        public TextView year;
        public RatingBar ratingBar;
        public ImageView imageview;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titleView);
            author = (TextView) itemView.findViewById(R.id.authorView);
            category = (TextView) itemView.findViewById(R.id.categoryView);
            publisher = (TextView) itemView.findViewById(R.id.publisherView);
            year = (TextView) itemView.findViewById(R.id.yearView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            imageview = (ImageView) itemView.findViewById(R.id.imageview);


            // do the same with all

        }
    }

}
