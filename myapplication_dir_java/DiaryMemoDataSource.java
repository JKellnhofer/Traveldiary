package com.example.delli.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

    //= Data Access Object (DAO) (vom Typ Cursor)
    //Verwaltung der Daten -> Datenbankverbindung
    //Hinzufügen, Auslesen, Löschen von Datensätzen
    //Umwandlung der Datensätze in Java-Objekte

public class DiaryMemoDataSource {

    private static final String LOG_TAG = DiaryMemoDataSource.class.getSimpleName();
    private SQLiteDatabase database;      // -> Membervariablen
    private DiaryMemoDbHelper dbHelper;

    private String[] columns = {
            DiaryMemoDbHelper.COLUMN_DATE,
            DiaryMemoDbHelper.COLUMN_PLACE,
            DiaryMemoDbHelper.COLUMN_ENTRY,
            DiaryMemoDbHelper.COLUMN_LONGITUDE,
            DiaryMemoDbHelper.COLUMN_LATITUDE,
            DiaryMemoDbHelper.COLUMN_ID,
    };


    public DiaryMemo createDiaryMemo(String date, String place, String entry, double lng, double lat){

        ContentValues values = new ContentValues();
        values.put(DiaryMemoDbHelper.COLUMN_DATE, date);
        values.put(DiaryMemoDbHelper.COLUMN_PLACE, place);
        values.put(DiaryMemoDbHelper.COLUMN_ENTRY, entry);

        long insertId = database.insert(DiaryMemoDbHelper.TABLE_DIARY_LIST, null, values);

        Cursor cursor = database.query(DiaryMemoDbHelper.TABLE_DIARY_LIST, columns,
                DiaryMemoDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        DiaryMemo diaryMemo = cursorToDiaryMemo(cursor);
        cursor.close();

        return diaryMemo;
    }

    private DiaryMemo cursorToDiaryMemo(Cursor cursor){

        int idDate = cursor.getColumnIndex(DiaryMemoDbHelper.COLUMN_DATE);
        int idPlace = cursor.getColumnIndex(DiaryMemoDbHelper.COLUMN_PLACE);
        int idEntry = cursor.getColumnIndex(DiaryMemoDbHelper.COLUMN_ENTRY);
        int idLongitude = cursor.getColumnIndex(DiaryMemoDbHelper.COLUMN_LONGITUDE)
        int idLatitude = cursor.getColumnIndex(DiaryMemoDbHelper.COLUMN_LATITUDE)
        int idIndex = cursor.getColumnIndex(DiaryMemoDbHelper.COLUMN_ID);

        String date = cursor.getString(idDate);
        String place = cursor.getString(idPlace);
        String entry = cursor.getString(idEntry);
        double lng = cursor.getDouble(idLongitude);
        double lat = cursor.getDouble(idLatitude);
        long id = cursor.getLong(idIndex);

        DiaryMemo diaryMemo = new DiaryMemo(date, place, entry, lng, lat);

        return diaryMemo;
    }

    public List<DiaryMemo> getAllDiaryMemos(){

        List<DiaryMemo> diaryMemoList = new ArrayList<>();

        //Such-String = null -> alle in Tabelle existierenden Datensätze werden als Ergebnis
        //  zurückgeliefert:

        Cursor cursor = database.query(DiaryMemoDbHelper.TABLE_DIARY_LIST, columns,
                null, null, null, null, null);

        cursor.moveToFirst();
        DiaryMemo diaryMemo;

        while (!cursor.isAfterLast()){
            diaryMemo = cursorToDiaryMemo(cursor);
            diaryMemoList.add(diaryMemo);
            //Log.d(LOG_TAG, "ID: " + diaryMemo.getId() + ", Inhalt " + diaryMemo.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return diaryMemoList;  //DiaryMemo-Liste: enthält alle Datensätze der Tabelle
    }











    //Context = Umgebung, in der App ausgeführt wird
    //Instanz dbHelper = Herstellen einer Verbindung zur SQLite Datenbank

    public DiaryMemoDataSource(Context context){
        Log.d(LOG_TAG, "Die DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new DiaryMemoDbHelper(context);
    }

    public void open(){
        Log.d(LOG_TAG, "Eine REferenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: "
        + database.getPath());
    }

    public void close(){
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }
}
