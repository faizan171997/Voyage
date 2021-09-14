package shree.voyagemain;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {


   ProgressBar mProgressBar;
    ListView mMessageListView;
    ImageButton mPhotoPickerButton;
    EditText mMessageEditText;
    Button mSendButton;
    MessageAdapter mMsgAdapter;
    FirebaseUser user;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    ChildEventListener mChildEventListener, childAtRuntime[];
    MenuItem start;
    boolean starttrip = false;
    int usercount = 0;
    DatabaseReference d9 = FirebaseDatabase.getInstance().getReference().child("user");


    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Group");
    String gname;


    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        childAtRuntime = new ChildEventListener[20];

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        gname = b.getString("group");
        //TextView tv = (TextView) findViewById(R.id.tptexte);
        //tv.setText(mySongs);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle(gname);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dr = dr.child(gname).child("Messages");

        user = FirebaseAuth.getInstance().getCurrentUser();

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        List<GroupMessage> friendlyMessages = new ArrayList<>();
        mMsgAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMsgAdapter);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);



        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
            }
        });

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click

                // Clear input box
                mMessageEditText.setText("");
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click


                // Clear input box

                GroupMessage groupMsg = new GroupMessage(mMessageEditText.getText().toString(), user.getDisplayName(), null);
                dr.push().setValue(groupMsg);
                mMessageEditText.setText("");
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GroupMessage gMessage = dataSnapshot.getValue(GroupMessage.class);
                mMsgAdapter.add(gMessage);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_menu, menu);
        start = menu.findItem(R.id.pppp);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_user) {
            Intent i=new Intent(getApplicationContext(),AddUserPopUp.class).putExtra("gname",gname);
            startActivity(i);
            return true;

        }else if(id == R.id.loc){
            // User chose the "Favorite" action, mark the current item
            // as a favorite...

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                //return;
            }
            Intent i=new Intent(getApplicationContext(),MapsActivity.class);
            Bundle b = new Bundle();
            b.putString("gname", gname); //Your id
            i.putExtras(b);
            startActivity(i);

            return true;
        }else if(id ==android.R.id.home) {
            this.finish();

            return true;
        }
        else if(id==R.id.view_user)
        {
            Intent i=new Intent(getApplicationContext(),view_users.class).putExtra("groupnm",gname);
            startActivity(i);
        }
        else if (id==R.id.show_bill)
        {
            Intent i=new Intent(getApplicationContext(),BillSplitter.class).putExtra("gname", gname);
            startActivity(i);
        }else if(id==R.id.pppp){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
               // return;
            }
            Intent i = new Intent(this, PostService.class).putExtra("gname",gname).putExtra("uname", user.getDisplayName());
            Intent i1 = new Intent(this, GetService.class).putExtra("gname",gname);
            Intent i2 = new Intent(this, BgLoctionUpdateService.class).putExtra("gname",gname);
            if(starttrip == false) {
                starttrip =true;
                start.setIcon(R.drawable.stop);
                Toast.makeText(this, "Trip Started", Toast.LENGTH_SHORT).show();

                startService(i);
                startService(i1);
                startService(i2);
            }else{
                starttrip = false;
                start.setIcon(R.drawable.start);
                Toast.makeText(this, "Trip Stopped", Toast.LENGTH_SHORT).show();
                stopService(i);
                stopService(i1);
                stopService(i2);
            }

        }else if(id==R.id.leavegrp){

            final DatabaseReference ddr = FirebaseDatabase.getInstance().getReference().child("Group").child(gname).child("users");
            ChildEventListener cccel = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    UserData ud = dataSnapshot.getValue(UserData.class);
                    if(ud.retEmail().equalsIgnoreCase(user.getEmail())){
                        ddr.child(dataSnapshot.getKey()).removeValue();
                    }

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

            ddr.addChildEventListener(cccel);


            final DatabaseReference d2 = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("groups");
            ChildEventListener gh = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    GroupData gd = dataSnapshot.getValue(GroupData.class);
                    if(gd.retGroupName().trim().equalsIgnoreCase(gname)){
                        d2.child(dataSnapshot.getKey()).removeValue();
                        Intent i=new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }

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

            d2.addChildEventListener(gh);

        }else if(id==R.id.deletegrp){
            DatabaseReference dddr = FirebaseDatabase.getInstance().getReference().child("Group").child(gname).child("users");







            final DatabaseReference d6 = FirebaseDatabase.getInstance().getReference().child("Groups");

            ChildEventListener fg = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    GroupData dg = dataSnapshot.getValue(GroupData.class);
                    if(dg.retGroupName().equalsIgnoreCase(gname)){
                        d6.child(dataSnapshot.getKey()).removeValue();
                        //Toast.makeText(getApplicationContext(),"Groups Deleted", Toast.LENGTH_SHORT).show();
                    }
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


            ChildEventListener noch = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    UserData ud = dataSnapshot.getValue(UserData.class);
                    deleteGroupFromUser(ud.retUID());
                    //Toast.makeText(getApplicationContext(),"Groups from user Deleted", Toast.LENGTH_SHORT).show();
                    if(usercount==dataSnapshot.getChildrenCount()){
                        DatabaseReference gi = FirebaseDatabase.getInstance().getReference().child("Group").child(gname);
                        gi.removeValue();
                        Intent i=new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
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

            dddr.addChildEventListener(noch);

            Intent i=new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);


        }



        return super.onOptionsItemSelected(item);
    }


    public void deleteGroupFromUser(String uid){

        final String yoyi = uid;



        childAtRuntime[usercount] = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GroupData dg = dataSnapshot.getValue(GroupData.class);
                if(dg.retGroupName()!=null){
                    if(dg.retGroupName().equalsIgnoreCase(gname)){
                        d9.child(yoyi).child("groups").child(dataSnapshot.getKey()).removeValue();
                    }
                }
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

        d9.child(uid).child("groups").addChildEventListener(childAtRuntime[usercount]);
        usercount++;



    }


}
