package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class EditBookActivity extends AppCompatActivity {

    private Button acceptButton;
    private EditText editName;
    private EditText editAuthor;
    private EditText editCategory;
    private EditText editEditorial;
    private EditText editYear;
    private RatingBar ratingBar;
    private BookData bookData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        Intent i = getIntent();
        Book book = (Book)i.getSerializableExtra("mybook");

        acceptButton = (Button) findViewById(R.id.accept);
        editName.setText(book.getTitle());
        editAuthor.setText(book.getAuthor());
        editCategory.setText(book.getCategory());
        editEditorial.setText(book.getPublisher());
        editYear.setText(book.getYear());
        ratingBar.setRating(book.getPersonal_evaluation());

        bookData = new BookData(this);
        final Activity THIS = this;
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editName.getText().toString().isEmpty()){

                    bookData.open();
                    bookData.createBook(editName.getText().toString(), editAuthor.getText().toString(),
                            editCategory.getText().toString(), editEditorial.getText().toString(),
                            Integer.valueOf(editYear.getText().toString()), String.valueOf(ratingBar.getRating()));
                    bookData.close();
                }
                NavUtils.navigateUpFromSameTask(THIS);
            }
        });

    }
}
