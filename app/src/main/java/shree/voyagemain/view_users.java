package shree.voyagemain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class view_users extends AppCompatActivity {

    ListView lve;
    UserAdapter uAdap;
    ChildEventListener mChildEventListener;

    String gname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        gname =b.getString("groupnm");
        Toast.makeText(getApplication()," "+gname,Toast.LENGTH_LONG).show();


        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Group").child(gname).child("users");
        lve = (ListView)findViewById(R.id.ulist);

        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Group Members");
//        actionBar.setDisplayHomeAsUpEnabled(true);

        List<UserData> userdl = new ArrayList<>();
        uAdap = new UserAdapter(this, R.layout.members_list, userdl);
        lve.setAdapter(uAdap);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                UserData ud = dataSnapshot.getValue(UserData.class);
                uAdap.add(ud);
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

        dr.addChildEventListener(mChildEventListener);

    }


}
