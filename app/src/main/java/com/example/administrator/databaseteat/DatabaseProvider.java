package com.example.administrator.databaseteat;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

    public static final int BOOK_DIR = 0;//Book表的所有数据
    public static final int BOOK_ITEM = 1;//Book表的单条数据
    public static final int CATEGRY_DIR = 2;//Category表的所有数据
    public static final int CATEGRY_ITRM = 3;//Category表的单条数据

    public static final String AUTHORITY = "com.example.administrator.databaseteat.provider";

    private static UriMatcher uriMatcher;
    private  MyDatabaseHepler dbHepler;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEGRY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#",CATEGRY_ITRM);
    }

    public DatabaseProvider() {



    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHepler.getReadableDatabase();
        int deleteRows = 0;

        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRows = db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRows =db.delete("Book","id=?",new String[]{bookId});
                break;

            case CATEGRY_DIR:
                deleteRows = db.delete("Category",selection,selectionArgs);
                break;
            case CATEGRY_ITRM:
                String cateId = uri.getPathSegments().get(1);
                deleteRows =db.delete("Category","id=?",new String[]{cateId});
                break;

            default:
                break;
        }

        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.administrator.databaseteat.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.administrator.databaseteat.provider.book";
            case CATEGRY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.administrator.databaseteat.provider.category";
            case CATEGRY_ITRM:
                return "vnd.android.cursor.item/vnd.com.example.administrator.databaseteat.provider.category";


        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHepler.getReadableDatabase();
        Uri uri1 = null;

        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                long newBookId = db.insert("Book",null,values);
                uri1 = Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
                break;
            case BOOK_ITEM:
                break;

            case CATEGRY_DIR:
                 long newCateId = db.insert("Category",null,values);
                uri1 = Uri.parse("content://"+AUTHORITY+"/category/"+newCateId);
                break;
            case CATEGRY_ITRM:
                break;

            default:
                break;
        }


        return uri1;

    }

    @Override
    public boolean onCreate() {
        dbHepler = new MyDatabaseHepler(getContext(),"BookStoreTest.db",null,1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHepler.getReadableDatabase();
        Cursor cursor = null;

        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor = db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                String BookId = uri.getPathSegments().get(1);//得到id
              cursor=db.query("Book",projection,"id=?",new String[]{BookId},null,null,sortOrder);
                break;

            case CATEGRY_DIR:
                cursor = db.query("Category",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGRY_ITRM:
               String CateId = uri.getPathSegments().get(1);//得到id
                cursor=db.query("Category",projection,"id=?",new String[]{CateId},null,null,sortOrder);
                break;

             default:
                 break;
        }



        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = dbHepler.getReadableDatabase();
        int updateRows = 0;

        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows = db.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows =db.update("Book",values,"id=?",new String[]{bookId});
                break;

            case CATEGRY_DIR:
                updateRows = db.update("Category",values,selection,selectionArgs);
                break;
            case CATEGRY_ITRM:
                String cateId = uri.getPathSegments().get(1);
                updateRows =db.update("Category",values,"id=?",new String[]{cateId});
                break;

            default:
                break;
        }

        return updateRows;
    }
}
