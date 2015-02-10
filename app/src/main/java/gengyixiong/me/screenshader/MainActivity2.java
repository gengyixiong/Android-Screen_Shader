package gengyixiong.me.screenshader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;


public class MainActivity2 extends Activity {

    int filterType;            //0 --> part screen, 1 --> full screen
    int alpha;                 //value of transparency
    int red;                   //value of red
    int green;                 //value of green
    int blue;                  //value of blue
    RadioGroup colorRadioGroup;
    RadioGroup filterTypeRadioGroup;
    SharedMemory shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        initialize();
    }

    public void initialize(){
        stopServiceIfActive();
        shared=new SharedMemory(this);

        colorRadioGroup = (RadioGroup) findViewById(R.id.colorRadioGroup);
        filterTypeRadioGroup = (RadioGroup) findViewById(R.id.filterType);

        colorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.black:
                        break;
                    case R.id.orange:
                        break;
                    case R.id.blue:
                        break;
                    case R.id.mix:
                        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
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

        updateColor();
    }

    private void updateColor(){
        int color = SharedMemory.getColor(alpha, red, green, blue); //get color value from shared

        ColorDrawable cd = new ColorDrawable(color);             //pass color value to ColorDrawable
        getWindow().setBackgroundDrawable(cd);
    }

    private void stopServiceIfActive(){

        if(MainService.STATE == MainService.ACTIVE){
            Intent i = new Intent(MainActivity2.this, MainService.class);
            stopService(i);
        }
    }

    public void cancelClick(View v){            //remove button listener
        finish();
    }

    public void applyClick(View v){             //apply button listener
        Intent i=new Intent(MainActivity2.this, MainService.class);
        startService(i);
        finish();
    }
}
