package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.BoolRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class myAdapter extends ArrayAdapter<Book> {
    private static LayoutInflater inflater = null;
    final myAdapter THIS = this;
    List<Book> myObjects;
    Context myContext;
    BookData bookData;

    public myAdapter(Context context, int resource, List<Book> objects, final BookData bookdata) {
        super(context, resource, objects);
        myObjects = objects;
        myContext = context;
        bookData = bookdata;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setObjects(List<Book> objects) {
        myObjects = objects;
    }

    @Override
    public int getCount() {
        return myObjects.size();
    }

    @Override
    public Book getItem(int position) {
        return myObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myObjects.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final Book book = getItem(position);

        if (vi == null) vi = inflater.inflate(R.layout.row, null);
        TextView title = (TextView) vi.findViewById(R.id.titleView);
        Book b = myObjects.get(position);
        title.setText(b.getTitle());
        TextView author = (TextView) vi.findViewById(R.id.authorView);
        author.setText(b.getAuthor());
        TextView category = (TextView) vi.findViewById(R.id.categoryView);
        category.setText(b.getCategory());
        final RatingBar bar = (RatingBar) vi.findViewById(R.id.ratingBar);

        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar r, final float value, boolean fromUser) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        new ContextThemeWrapper(myContext, R.style.AlertDialog_AppCompat));
                // set title
                if (fromUser) {
                    alertDialogBuilder.setTitle("Changed Rating");

                    alertDialogBuilder
                            .setMessage("Do you want to change the book rating?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    bookData.changeRating(myObjects.get(position), value);
                                    myObjects.get(position).setPersonal_evaluation(value);
                                    notifyDataSetChanged();
                                }


                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    bar.setRating(book.getPersonal_evaluation());
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }

            }
        });
        bar.setRating(b.getPersonal_evaluation());
        ImageView imgView = (ImageView) vi.findViewById(R.id.imageview);
        try {
            imgView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    switch (v.getId()) {
                        case R.id.imageview:

                            PopupMenu popup = new PopupMenu(myContext, v, Gravity.NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);
                            popup.getMenuInflater().inflate(R.menu.popup_menu,
                                    popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.edit:

                                            //Or Some other code you want to put here.. This is just an example.
                                            Toast.makeText(myContext, "Edit not implemented for now", Toast.LENGTH_SHORT).show();

                                            break;
                                        case R.id.delete:
                                            if (bookData.deleteBook(book)) remove(book);
                                            else
                                                Toast.makeText(myContext, "Book does not exist, please swipe up to refresh.", Toast.LENGTH_LONG).show();
                                            break;

                                        default:
                                            break;
                                    }

                                    return true;
                                }
                            });

                            break;

                        default:
                            break;
                    }


                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

        return vi;
    }
}
