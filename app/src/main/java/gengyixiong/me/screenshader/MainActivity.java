package gengyixiong.me.screenshader;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;


public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    int          alpha;                 //value of transparency
    int          red;                   //value of red
    int          green;                 //value of green
    int          blue;                  //value of blue
    int          area;
    int          position;
    int          filterType;
    SeekBar      alphaSeek;             //transparency seek bar
    SeekBar      redSeek;               //red seek bar
    SeekBar      greenSeek;             //green seek bar
    SeekBar      blueSeek;              //blue seek bar
    SeekBar      areaSeek;              //area
    SeekBar      positionSeek;          //position
    SharedMemory shared;
    RadioGroup   filterTypeRadioGroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize(){

        stopServiceIfActive();
        shared=new SharedMemory(this);

        filterTypeRadioGroup  = (RadioGroup) findViewById(R.id.filterType);

        filterTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.partScreen:
                        filterType = 0;
                        break;
                    case R.id.fullScreen:
                        filterType = 1;
                        break;
                }
            }
        });

        alphaSeek   = (SeekBar) findViewById(R.id.alphaControl);    //find
        redSeek     = (SeekBar) findViewById(R.id.redControl);      //SeekBar
        greenSeek   = (SeekBar) findViewById(R.id.greenControl);    //view
        blueSeek    = (SeekBar) findViewById(R.id.blueControl);     //by ID
        areaSeek    = (SeekBar) findViewById(R.id.areaControl);
        positionSeek= (SeekBar) findViewById(R.id.positionControl);

        alphaSeek.setOnSeekBarChangeListener(this);    	            //set
        redSeek.setOnSeekBarChangeListener(this);    	            //OnSeekBarChangeListener
        greenSeek.setOnSeekBarChangeListener(this);                 //implemented
        blueSeek.setOnSeekBarChangeListener(this);                  //to this activity
        areaSeek.setOnSeekBarChangeListener(this);
        positionSeek.setOnSeekBarChangeListener(this);
        
        alpha   = shared.getAlpha();                                //get stored value
        red     = shared.getRed();                                  //of alpha
        green   = shared.getGreen();                                //red blue and green
        blue    = shared.getBlue();
        area    = shared.getArea();
        position= shared.getPosition();

        alphaSeek.setProgress(alpha);
        redSeek.setProgress(red);
        greenSeek.setProgress(green);
        blueSeek.setProgress(blue);
        areaSeek.setProgress(area);
        positionSeek.setProgress(position);

        if (shared.getFilterType() == 0){
            filterTypeRadioGroup.check(R.id.partScreen);
        }else{
            filterTypeRadioGroup.check(R.id.fullScreen);
        }

        updateColor();
    }

    private void stopServiceIfActive(){

        if(MainService.STATE == MainService.ACTIVE){
            Intent i = new Intent(MainActivity.this, MainService.class);
            stopService(i);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar,
                                  int progress,             //SeekBar changed method
                                  boolean fromUser){
        if(seekBar == alphaSeek){
            alpha  =  seekBar.getProgress();                //set alpha value
        }
        if(seekBar == redSeek){
            red    =  seekBar.getProgress();                //set red value
        }
        if(seekBar == greenSeek){
            green  =  seekBar.getProgress();                //set green value
        }
        if(seekBar == blueSeek){
            blue   =  seekBar.getProgress();                //set blue value
        }
        if(seekBar == areaSeek){
            area   =  seekBar.getProgress();
        }
        if(seekBar == positionSeek){
            position   =  seekBar.getProgress();
        }
        updateColor();
    }

    private void updateColor(){
        int color = SharedMemory.getColor(alpha, red, green, blue); //get color value from shared

        ColorDrawable cd = new ColorDrawable(color);             //pass color value to ColorDrawable
        getWindow().setBackgroundDrawable(cd);
    }

    @Override
    public void onStartTrackingTouch(SeekBar sb){
        //SeekBar Override method. do nothing here
    }

    @Override
    public void onStopTrackingTouch(SeekBar sb){
        //SeekBar Override method. do nothing here
    }

    public void cancelClick(View v){            //remove button listener
        finish();
    }

    public void applyClick(View v){             //apply button listener
        shared.setAlpha(alpha);
        shared.setRed(red);
        shared.setGreen(green);
        shared.setBlue(blue);
        shared.setArea(area);
        shared.setPosition(position);
        shared.setFilterType(filterType);

        Intent i=new Intent(MainActivity.this, MainService.class);
        startService(i);

        makeNotification();
        finish();
    }

    private void makeNotification(){
        //NotificationCompat.Builder nb   = new NotificationCompat.Builder(this);
        Notification.Builder nb = new Notification.Builder(this);
        nb.setSmallIcon(R.drawable.notif);                              //notif bar icon
        nb.setContentTitle("Screen Shader");                            //notif bar title
        nb.setContentText("Active");                                    //notif bar text
        nb.setAutoCancel(true);

        Intent           resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        nb.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0x355, nb.build());
    }
}
