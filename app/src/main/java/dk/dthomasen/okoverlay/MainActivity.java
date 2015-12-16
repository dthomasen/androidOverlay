package dk.dthomasen.okoverlay;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean bounded;
    private OverlayService overlayService;

    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, OverlayService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    };

    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this, "Service is disconnected", Toast.LENGTH_SHORT).show();
            bounded = false;
            overlayService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            bounded = true;
            OverlayService.OverlayBinder overlayBinder = (OverlayService.OverlayBinder)service;
            overlayService = overlayBinder.getServerInstance();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(bounded) {
            unbindService(mConnection);
            bounded = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, OverlayService.class));

        Button mailButton = (Button) findViewById(R.id.ookeemailbutton);

        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayService.showOverlay("Mail", "* Point #1\n* Point #2\n* Point #3\n* Point #4");
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.cloudmagic.mail");
                startActivity(launchIntent);
            }
        });

        Button messengerButton = (Button) findViewById(R.id.ookeemessengerbutton);

        messengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayService.showOverlay("Messenger", "* Point #1\n* Point #2\n* Point #3\n* Point #4");
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.messaging");
                startActivity(launchIntent);
            }
        });
    }
}
