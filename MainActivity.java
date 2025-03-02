package com.example.officetracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        dateText = findViewById(R.id.dateText);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnLunchBreak = findViewById(R.id.btnLunchBreak);
        Button btnShortBreak = findViewById(R.id.btnShortBreak);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnReport = findViewById(R.id.btnReport);

        // Display current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        dateText.setText("Date: " + currentDate);

        btnLogin.setOnClickListener(v -> dbHelper.insertRecord("Login"));
        btnLogout.setOnClickListener(v -> dbHelper.insertRecord("Logout"));
        btnLunchBreak.setOnClickListener(v -> startBreak("Lunch", 30));
        btnShortBreak.setOnClickListener(v -> startBreak("Short", 15));

        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        btnReport.setOnClickListener(v -> startActivity(new Intent(this, ReportActivity.class)));
    }

    private void startBreak(String breakType, int duration) {
        dbHelper.insertRecord(breakType + " Break");
        setAlarm(duration - 4); // Trigger ringtone 4 mins before break ends
    }

    private void setAlarm(int minutes) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BreakReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerTime = System.currentTimeMillis() + (minutes * 60 * 1000);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }
}
