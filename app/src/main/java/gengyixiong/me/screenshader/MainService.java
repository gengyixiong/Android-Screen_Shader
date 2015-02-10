package gengyixiong.me.screenshader;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by gengyixiong on 11/24/14.
 */
public class MainService extends Service {

    LinearLayout    topFilterLayout;
    LinearLayout    bottomFilterLayout;
    LinearLayout    filterLayout;
    RelativeLayout  rootLayout;
    SharedMemory    shared;

    int area;
    int position;

    public static int STATE;
    public static final int INACTIVE    = 0;
    public static final int ACTIVE      = 0;

    static{
        STATE = INACTIVE;
    }

    @Override
    public IBinder onBind(Intent i){
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        shared              = new SharedMemory(this);
        topFilterLayout     = new LinearLayout(this);
        bottomFilterLayout  = new LinearLayout(this);
        filterLayout        = new LinearLayout(this);
        rootLayout          = new RelativeLayout(this);
        switch (shared.getFilterType()){
            case 0:
                partScreenShaderUpdate();
                break;
            case 1:
                fullScreenShaderUpdate();
                break;
        }
    }

    public void partScreenShaderUpdate(){

        topFilterLayout.setBackgroundColor(shared.getColor());            //set color to LinearLayout
        bottomFilterLayout.setBackgroundColor(shared.getColor());
        area = shared.getArea();
        position = shared.getPosition();

        WindowManager.LayoutParams rootParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,                //shader width
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,         //display on top of everything
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,    //cannot get focused
                PixelFormat.TRANSLUCENT);

        RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                500 + position - 400
        );
        topParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        topFilterLayout.setLayoutParams(topParams);
        rootLayout.addView(topFilterLayout);

        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                500 - area - position + 400
        );
        bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        bottomFilterLayout.setLayoutParams(bottomParams);
        rootLayout.addView(bottomFilterLayout);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(rootLayout, rootParams);

    }

    public void fullScreenShaderUpdate(){

        filterLayout.setBackgroundColor(shared.getColor());

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,                //shader width
                WindowManager.LayoutParams.MATCH_PARENT,                //shader height
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,         //display on top of everything
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,    //cannot get focused
                PixelFormat.TRANSLUCENT);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(filterLayout, params);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if(shared.getFilterType() == 0){
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            //wm.removeView(topFilterLayout);
            //wm.removeView(bottomFilterLayout);
            wm.removeView(rootLayout);
        }else{
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(filterLayout);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        topFilterLayout.setBackgroundColor(shared.getColor());
        bottomFilterLayout.setBackgroundColor(shared.getColor());
        filterLayout.setBackgroundColor(shared.getColor());
        return super.onStartCommand(intent, flags, startId);
    }
}

