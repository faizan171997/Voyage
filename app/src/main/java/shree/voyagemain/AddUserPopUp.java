package shree.voyagemain;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddUserPopUp extends AppCompatActivity {

    EditText InviteEmail;
    Button addUser;
    boolean chk=false,chk2=false;
    ChildEventListener chl;
    ChildEventListener chl2;
    ChildEventListener notRep;
    String gname;
    int orj;
    static int mem;
    FirebaseUser user;
    ArrayList <GroupData> grpDetails;
    ArrayList members;
    ArrayList <String> alreadyMembere;

    ArrayList <UserData> membersOBJ;

    Boolean forEmailText=false;
    static int memberSize;
    DatabaseReference groupDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    DatabaseReference toaddUser =FirebaseDatabase.getInstance().getReference().child("Group");
    DatabaseReference toaddUserX =FirebaseDatabase.getInstance().getReference().child("Group");
    DatabaseReference useradd =FirebaseDatabase.getInstance().getReference().child("user");
    DatabaseReference toCheck =FirebaseDatabase.getInstance().getReference().child("Group");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        alreadyMembere = new ArrayList<String>();
        gname = b.getString("gname");
        Toast.makeText(getApplication(),""+gname,Toast.LENGTH_LONG).show();
        toaddUser= toaddUser.child(gname).child("users");
        toCheck= toCheck.child(gname).child("users");
        toaddUserX= toaddUserX.child(gname).child("details");
        setContentView(R.layout.activity_add_user_pop_up);
        user= FirebaseAuth.getInstance().getCurrentUser();
        //useradd = useradd.child(user.getUid()).child("groups");
        InviteEmail =(EditText)findViewById(R.id.InviteEmail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Members");


        addUser = (Button)findViewById(R.id.AddUser);
        InviteEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!forEmailText)
                {
                    InviteEmail.setText("");
                }
                forEmailText=true;


            }
        });
        members =new ArrayList();
        membersOBJ =new ArrayList();
        grpDetails=new ArrayList<>();
        InviteEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                chk=false;
                chk2=false;
                for (int i=0;i<memberSize;i++)
                {
//                    Toast.makeText(getApplicationContext(),members.get(0).toString(), Toast.LENGTH_SHORT).show();
                    if(i<members.size()) {
                        if (members.get(i).toString().equalsIgnoreCase(InviteEmail.getText().toString())) {
                            chk = true;
                            orj = i;
                            break;
                        }
                    }else if(i>=members.size()){

                    }
                }
//                Toast.makeText(getApplicationContext(),""+mem,Toast.LENGTH_SHORT).show();
                for (int i=0;i<mem;i++)
                {
//                    Toast.makeText(getApplicationContext(),members.get(0).toString(), Toast.LENGTH_SHORT).show();
                    if(i<alreadyMembere.size()) {
                        if (alreadyMembere.get(i).toString().equalsIgnoreCase(InviteEmail.getText().toString())) {
                            chk2 = true;
                            break;
                        }
                    }
                }

                if(chk)
                {
//                    Toast.makeText(getApplicationContext(), "User Found", Toast.LENGTH_SHORT).show();
                    if (chk2)
                    {
                        Toast.makeText(getApplicationContext(),"Member Already Added",Toast.LENGTH_SHORT).show();
                        addUser.setEnabled(false);
                    }else{
                        addUser.setEnabled(true);
                    }

                }
                else {
//                    Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_SHORT).show();
                    addUser.setEnabled(false);
                }


            }
        });
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                toaddUser.push().setValue(membersOBJ.get(orj));
                Toast.makeText(getApplicationContext(), "User Added", Toast.LENGTH_SHORT).show();

                chl2 =new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        GroupData GroupInfo=dataSnapshot.getValue(GroupData.class);
                        //Toast.makeText(getApplicationContext(),""+GroupInfo.retGroupName(),Toast.LENGTH_LONG).show();
                        //grpDetails.add(GroupInfo);
                        useradd = useradd.child(membersOBJ.get(orj).retUID()).child("groups");
                        useradd.push().setValue(GroupInfo);


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
                };toaddUserX.addChildEventListener(chl2);
                finish();
            }

        });

        notRep = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                UserData ud= dataSnapshot.getValue(UserData.class);
//                if(ud.retEmail().trim().equalsIgnoreCase(EmailAdd.trim()))
//                {
//                    chk=true;
//                }


                UserData already = dataSnapshot.getValue(UserData.class);
                alreadyMembere.add(already.retEmail());
                mem++;
//                membersOBJ.add(MemberINfo);
//                members.add(MemberINfo.retEmail().toString());
//                memberSize++;


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

        toCheck.addChildEventListener(notRep);

        chl = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                UserData ud= dataSnapshot.getValue(UserData.class);
//                if(ud.retEmail().trim().equalsIgnoreCase(EmailAdd.trim()))
//                {
//                    chk=true;
//                }

                UserData MemberINfo = dataSnapshot.getValue(UserData.class);
                membersOBJ.add(MemberINfo);
                members.add(MemberINfo.retEmail().toString());
                memberSize++;


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

        groupDatabaseReference.addChildEventListener(chl);


    }

    public void onBackPressed() {
        //moveTaskToBack(true);
        //new Intent().FLAG_ACTIVITY_CLEAR_TOP;
        finish();
    }



}
