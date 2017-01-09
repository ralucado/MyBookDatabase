package com.example.pr_idi.mydatabaseexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class AddNewBookActivity extends AppCompatActivity {

    private Button acceptButton;
    private EditText editText;
    private BookData bookData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        acceptButton = (Button) findViewById(R.id.accept);
        editText = (EditText) findViewById(R.id.editName);

        bookData = new BookData(this);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().isEmpty()){

                    bookData.open();
                    bookData.createBook(editText.getText().toString(), "jo");
                    bookData.close();
                }
            }
        });

    }
}
