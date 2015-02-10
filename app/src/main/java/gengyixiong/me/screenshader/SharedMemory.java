package gengyixiong.me.screenshader;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gengyixiong on 11/24/14.
 */
public class SharedMemory {
    SharedPreferences prefs;

    SharedMemory(Context ctx){
        prefs=ctx.getSharedPreferences("SCREEN_SETTINGS", Context.MODE_PRIVATE);
    }

    void setAlpha(int value){
        prefs.edit().putInt("alpha", value).apply();
    }

    void setRed(int value){
        prefs.edit().putInt("red", value).apply();
    }

    void setGreen(int value){
        prefs.edit().putInt("green", value).apply();
    }

    void setBlue(int value){
        prefs.edit().putInt("blue", value).apply();
    }

    void setArea(int value){
        prefs.edit().putInt("area", value).apply();
    }

    void setPosition(int value){
        prefs.edit().putInt("position", value).apply();
    }

    void setFilterType(int value){ prefs.edit().putInt("filterType", value).apply();}

    int getBlue(){
        return prefs.getInt("blue", 0x00);
    }

    int getGreen(){
        return prefs.getInt("green", 0x00);
    }

    int getRed(){
        return prefs.getInt("red", 0x00);
    }

    int getAlpha(){
        return prefs.getInt("alpha", 0x33);
    }

    int getArea(){ return prefs.getInt("area", 100);}

    int getPosition(){return prefs.getInt("position", 100);}

    int getFilterType(){return prefs.getInt("filterType", 0);}

    public static int getColor(int alpha, int red, int green, int blue){
        String hex = String.format("%02x%02x%02x%02x", alpha, red, green, blue);
        int color=(int)Long.parseLong(hex,16);
        return color;
    }

    public int getColor(){
        String hex = String.format("%02x%02x%02x%02x", getAlpha(), getRed(), getGreen(), getBlue());
        int color=(int)Long.parseLong(hex,16);
        return color;
    }
}