package com.example.officetracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dbHelper = new DatabaseHelper(this);
        TextView reportText = findViewById(R.id.reportText);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM records", null);

        StringBuilder report = new StringBuilder();
        while (cursor.moveToNext()) {
            report.append(cursor.getString(1)).append(" - ").append(cursor.getString(2)).append("\n");
        }
        cursor.close();
        reportText.setText(report.toString());
    }
}
