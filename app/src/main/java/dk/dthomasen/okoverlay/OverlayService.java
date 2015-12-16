package dk.dthomasen.okoverlay;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class OverlayService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    private IBinder mBinder = new OverlayBinder();


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class OverlayBinder extends Binder {
        public OverlayService getServerInstance() {
            return OverlayService.this;
        }
    }

    public void showOverlay(String title, String body) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View myView = inflater.inflate(R.layout.overlay, null);
        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // wm.removeView(myView);
                return true;
            }
        });

        ((TextView) myView.findViewById(R.id.titletextview)).setText(title);
        ((TextView) myView.findViewById(R.id.pointstextview)).setText(body) ;

        myView.findViewById(R.id.closebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(myView);
            }
        });

        // Add layout to window manager
        wm.addView(myView, params);
    }
}