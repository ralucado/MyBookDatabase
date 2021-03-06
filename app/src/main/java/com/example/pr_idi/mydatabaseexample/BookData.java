package com.example.pr_idi.mydatabaseexample;

/**
 * BookData
 * Created by pr_idi on 10/11/16.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.MutableBoolean;

public class BookData {

    // Database fields
    private SQLiteDatabase database;

    // Helper to manipulate table
    private MySQLiteHelper dbHelper;

    // Here we only select Title and Author, must select the appropriate columns
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_AUTHOR, MySQLiteHelper.COLUMN_PUBLISHER, MySQLiteHelper.COLUMN_YEAR, MySQLiteHelper.COLUMN_CATEGORY, MySQLiteHelper.COLUMN_PERSONAL_EVALUATION};

    public BookData(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Book createBook(String title, String author, String category, String publisher, String year, String rating) {
        ContentValues values = new ContentValues();
        Log.d("Creating", "Creating " + title + " " + author);

        // Add data: Note that this method only provides title and author
        // Must modify the method to add the full data
        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_AUTHOR, author);
        values.put(MySQLiteHelper.COLUMN_CATEGORY, category);
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, publisher);
        values.put(MySQLiteHelper.COLUMN_YEAR, String.valueOf(year));
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, rating);

        // Actual insertion of the data using the values variable
        long insertId = database.insert(MySQLiteHelper.TABLE_BOOKS, null,
                values);


        // Main activity calls this procedure to create a new book
        // and uses the result to update the listview.
        // Therefore, we need to get the data from the database
        // (you can use this as a query example)
        // to feed the view.

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Book newBook = cursorToBook(cursor);

        // Do not forget to close the cursor
        cursor.close();
        Log.d("Created Book", "Created " + newBook.getId() + " " + newBook.getTitle() + " " + newBook.getAuthor() + " " + newBook.getPublisher() + " " + newBook.getYear() + " " + newBook.getCategory() + " " + newBook.getPersonal_evaluation());
        // Return the book
        return newBook;
    }


    public void editBook(long id, String title, String author, String category, String publisher, String year, String rating){

        ContentValues values = new ContentValues();

        // Add data: Note that this method only provides title and author
        // Must modify the method to add the full data
        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_AUTHOR, author);
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, publisher);
        values.put(MySQLiteHelper.COLUMN_YEAR, String.valueOf(year));
        values.put(MySQLiteHelper.COLUMN_CATEGORY, category);
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, String.valueOf(rating));
        if(database.update(MySQLiteHelper.TABLE_BOOKS,values,MySQLiteHelper.COLUMN_ID
                + " = " + id,null) > 0) {
            System.out.println("Book updated with id: " + id);
        }

    }

    public boolean deleteBook(Book book) {
        long id = book.getId();
        if (database.delete(MySQLiteHelper.TABLE_BOOKS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null) > 0) {
            System.out.println("Book deleted with id: " + id);
            return true;
        }

        else return false;
    }

    public void changeRating(Book book, float val){
        long id = book.getId();
        ContentValues values = new ContentValues();

        // Add data: Note that this method only provides title and author
        // Must modify the method to add the full data
        values.put(MySQLiteHelper.COLUMN_TITLE, book.getTitle());
        values.put(MySQLiteHelper.COLUMN_AUTHOR, book.getAuthor());
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, book.getPublisher());
        values.put(MySQLiteHelper.COLUMN_YEAR, String.valueOf(book.getYear()));
        values.put(MySQLiteHelper.COLUMN_CATEGORY, book.getCategory());
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, String.valueOf(val));
        if(database.update(MySQLiteHelper.TABLE_BOOKS,values,MySQLiteHelper.COLUMN_ID
                + " = " + id,null) > 0) {
            System.out.println("Book updated with id: " + id);
        }

    }


    public List<Book> getBooksByAuthor(String author) {

        List<Book> books = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS, allColumns, MySQLiteHelper.COLUMN_AUTHOR + "='" + author + "'", null, null, null, MySQLiteHelper.COLUMN_TITLE);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        cursor.close();

        return books;
    }

    public List<Book> getAllBooks(String sortBy) {
        List<Book> books = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, null, null, null, null, MySQLiteHelper.COLUMN_TITLE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        if (sortBy.equals("Authors")){
            Collections.sort(books, new Comparator<Book>() {
                @Override
                public int compare(Book b1, Book b2) {
                    return b1.getAuthor().toLowerCase().compareTo(b2.getAuthor().toLowerCase());
                }
            });
        }
        else if (sortBy.equals("Categories")){
            Collections.sort(books, new Comparator<Book>() {
                @Override
                public int compare(Book b1, Book b2) {
                    return b1.getCategory().toLowerCase().compareTo(b2.getCategory().toLowerCase());
                }
            });
        }
        else{
            Collections.sort(books, new Comparator<Book>() {
                @Override
                public int compare(Book b1, Book b2) {
                    return b1.getTitle().toLowerCase().compareTo(b2.getTitle().toLowerCase());
                }
            });
        }
        return books;
    }

    public List<Book> getBooksByCategory(String category) {
        List<Book> books = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS, allColumns, MySQLiteHelper.COLUMN_CATEGORY + "='" + category + "'", null, null, null, MySQLiteHelper.COLUMN_TITLE);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        cursor.close();

        return books;
    }

    private Book cursorToBook(Cursor cursor) {
        Book book = new Book();
        book.setId(cursor.getLong(0));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));
        book.setPublisher(cursor.getString(3));
        book.setYear(cursor.getInt(4));
        book.setCategory(cursor.getString(5));
        book.setPersonal_evaluation(cursor.getFloat(6));

        return book;
    }
}