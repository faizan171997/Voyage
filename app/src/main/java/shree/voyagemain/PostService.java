package shree.voyagemain;

/**
 * Created by root on 9/10/17.
 */

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.abs;


/**
 * Created by Faizan-PC on 9/22/2017.
 */

public class PostService extends Service {

    private Sensor mySensor;
    private SensorManager SM;

    SensorListen listen;

    String gname, uname;





    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"I'm Service",Toast.LENGTH_SHORT).show();

        Bundle b = intent.getExtras();
        gname = b.getString("gname");
        uname = b.getString("uname");



        // Create our Sensor Manager
        //SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        SM = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        listen = new SensorListen();
        listen.a = this;
        listen.gname = gname;
        listen.uname = uname;
        // Register sensor Listener
        SM.registerListener(listen, mySensor, SensorManager.SENSOR_DELAY_NORMAL);



        // Assign TextView


        return super.onStartCommand(intent, flags, startId);
    }

}

class SensorListen implements SensorEventListener {


    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Group");
    FirebaseUser u;

    float c = 0;
    float temp_c =0;
    public Service a;
    public String gname = null, uname = null;
    AtomicInteger c1;
    boolean executedOnce = false;
    long coi = 0;


    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub

        if(coi == 10000){
            coi = 0;
            executedOnce = false;
        }else{
            coi++;
        }

        if (abs(event.values[0]) > c || abs(event.values[1]) > c || abs(event.values[2]) > c) {

            if (abs(event.values[0]) > abs(event.values[1]) && abs(event.values[0]) > abs(event.values[2])) {
                temp_c = abs(event.values[0]);
            } else if (abs(event.values[1]) > abs(event.values[0]) && abs(event.values[1]) > abs(event.values[2])) {
                temp_c = abs(event.values[1]);
            } else {
                temp_c = abs(event.values[1]);
            }
            if (temp_c > c) {
                c = temp_c;

            }
            if (c > 60) {

                if(!executedOnce) {
                    dr = dr.child(gname).child("CrashUser");
                    u = FirebaseAuth.getInstance().getCurrentUser();

                    dr.push().setValue(new UserData(u.getDisplayName(), u.getUid(), u.getEmail()));


                    c = 0;
                    temp_c = 0;
                    executedOnce = true;

                }


            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

}