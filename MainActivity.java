package fr.eurecom.android.preferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    android.content.SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = preferences.getString("username", "n/a");
                String password = preferences.getString("password", "n/a");
                showPrefs(username, password);
            }
        });
        Button button3= (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
            }
    public void openDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Reset username and password?");
        builder.setCancelable(true);
        builder.setPositiveButton("I agree", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Resetting Credential",Toast.LENGTH_LONG).show();
                reset_preferences();
                createNotification();
            }
        });
        builder.setNegativeButton("No, no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Keep Credential", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create(); dialog.show();
    }
    private final String CHANNEL_ID= "personal_notifications";
    private final int NOTIFICATION_ID= 001;
    public void createNotification() {
        createNotificationChannel();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this,CHANNEL_ID)
                .setContentTitle("Hello Preferences")
                .setContentText("Successfully reset user Credential")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(activity)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0,notification);
    }
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personal Notifications";
            String description = "Include all the personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public void reset_preferences() {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("username", null);
        edit.putString("password", null);
        edit.commit();
        Toast.makeText(MainActivity.this, "Reset user name and password", Toast.LENGTH_LONG).show();
    }
    private void showPrefs(String username, String password){
        Toast.makeText(MainActivity.this, "You kept user: " + username + " and password: " + password, Toast.LENGTH_LONG).show();
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this, Preferences.class);
                startActivity(i);
                Toast.makeText(MainActivity.this, "Here you can store your user credentials.", Toast.LENGTH_LONG).show();
                Log.i("Main", "sent an intent to the Preference class!");
                break;

        }
        return true;
    }
}