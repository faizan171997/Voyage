package shree.voyagemain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final int RC_SIGN_IN = 1;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    String userID;
    FirebaseUser user;
    ImageButton floating;
    SharedPreferences pref;
    ListView  lv;
    SharedPreferences.Editor editor;
    public static final String TAG = MapsActivity.class.getSimpleName();
    ChildEventListener mChildEventListener;
    boolean userExistsinDB = false;
    ListView groupList;
    GroupAdapter groupAdapter;
    DatabaseReference groupDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user");
    boolean b = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        floating=(ImageButton) findViewById(R.id.action_favorite);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),popup.class);
                startActivity(i);
            }
        });

        floating.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "Add new Group", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("Voyage");

        //lv = (ListView)findViewById(R.id.groupListView);


        /*lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.maIntent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);keText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/

        groupList = (ListView)findViewById(R.id.groupListView);
        final List<GroupData> groupData = new ArrayList<>();
        groupAdapter = new GroupAdapter(this, R.layout.grouplist, groupData);
        groupList.setAdapter(groupAdapter);
        user = FirebaseAuth.getInstance().getCurrentUser();



        groupList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                startActivity(new Intent(getApplicationContext(), GroupActivity.class));
                startActivity(new Intent(getApplicationContext(),GroupActivity.class).putExtra("group",groupData.get(position).groupname.toString()));
            }
        });


        groupList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub



                //Log.v("long clicked","pos: " + pos);

                return true;
            }
        });






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GroupData groupData1 = dataSnapshot.getValue(GroupData.class);

                    groupAdapter.add(groupData1);

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



        //

        TextView tu = (TextView)findViewById(R.id.usernamemain);
        TextView ue = (TextView)findViewById(R.id.useremail);

        if(user!=null){
            // if signed in
            userID = user.getUid();
            if(!pref.getBoolean("useradded", false)){
                userCheck();
                if(!userExistsinDB){
                    addUserToDB();
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
            Toast.makeText(getApplicationContext(), "Welcome "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
            //
            groupDatabaseReference = groupDatabaseReference.child(user.getUid()).child("groups");
            groupDatabaseReference.addChildEventListener(mChildEventListener);
//            tu.setText(user.getDisplayName());3
//            tu.setText(user.getDisplayName());3
//            ue.setText(user.getEmail());


        }else{
            // if signed out
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setTheme(R.style.AppTheme)
                            .setLogo(R.drawable.voyage_main)
                            .setAvailableProviders(getSelectedProviders())
                            // .setPrivacyPolicyUrl(getSelectedPrivacyPolicyUrl())
                            .setIsSmartLockEnabled(false)
                            //.setAllowNewEmailAccounts(mAllowNewEmailAccounts.isChecked())
                            .build(),
                    RC_SIGN_IN);

            //


        }


    }




    public void addUserToDB(){

        FirebaseDatabase.getInstance().getReference().child("users").push().setValue(new UserData(user.getDisplayName(),user.getUid(),user.getEmail()));

        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("details").push().setValue(new UserData(user.getDisplayName(),user.getUid(),user.getEmail()));

        editor.putBoolean("useradded", true);
        editor.commit();
    }

    public void userCheck(){


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserData mData = dataSnapshot.getValue(UserData.class);

                //Toast.makeText(getApplicationContext(),""+dataSnapshot.getRef(),Toast.LENGTH_SHORT).show();


                if(mData.retEmail()==user.getEmail()){
                    userExistsinDB = true;
                }
                //mData.toString();
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

        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(mChildEventListener);


    }


    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            //startSignedInActivity(response);

            Toast.makeText(getApplicationContext(),"Signed In",Toast.LENGTH_SHORT).show();
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            //finish();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                Toast.makeText(getApplicationContext(),R.string.sign_in_cancelled,Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {

                Toast.makeText(getApplicationContext(),R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
                finish();
                return;

            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {

                Toast.makeText(getApplicationContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }

        //showSnackbar(R.string.unknown_response);
    }

    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();


        selectedProviders.add(
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                            .build());



        /*selectedProviders.add(
                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
                            .setPermissions(getFacebookPermissions())
                            .build());*/



        //selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());
        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        //selectedProviders.add(
        //           new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build());


        return selectedProviders;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_favorite){
        // User chose the "Favorite" action, mark the current item
        // as a favorite...

            Intent i=new Intent(getApplicationContext(),popup.class);
            startActivity(i);

        return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
