package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import java.util.ArrayList;

public class AddNewBookActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_add_new_book);

        acceptButton = (Button) findViewById(R.id.accept);
        editName = (EditText) findViewById(R.id.editName);
        editAuthor = (EditText) findViewById(R.id.editAuthor);
        editCategory = (EditText) findViewById(R.id.editCategory);
        editEditorial = (EditText) findViewById(R.id.editEditorial);
        editYear = (EditText) findViewById(R.id.editYear);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

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
