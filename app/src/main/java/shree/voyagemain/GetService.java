package shree.voyagemain;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by root on 9/10/17.
 */

public class GetService extends Service {

    String gname, uname;

    ChildEventListener cel;
    FirebaseUser us;

    AtomicInteger c1;

    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Group");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {


        Bundle b = intent.getExtras();
        gname = b.getString("gname");
        //uname = b.getString("uname");

        dr = dr.child(gname).child("CrashUser");
        us = FirebaseAuth.getInstance().getCurrentUser();



        cel = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserData u = dataSnapshot.getValue(UserData.class);

                if(us.getDisplayName()!=u.retUsername()) {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                    mBuilder.setSmallIcon(getApplicationInfo().icon);
                    mBuilder.setContentTitle("Your Trip : " + gname);
                    mBuilder.setContentText(u.retUsername() + "Has Probably Crashed");
                    c1 = new AtomicInteger(0);

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
                    mNotificationManager.notify(c1.incrementAndGet(), mBuilder.build());
                }

                //dr.removeValue();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        dr.addChildEventListener(cel);


        // Create our Sensor Manager
        //SM = (SensorManager)getSystemService(SENSOR_SERVICE);





        // Assign TextView


        return super.onStartCommand(intent, flags, startId);
    }

}
