package shree.voyagemain;/*
package shree.voyagemain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BillSplitter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_splitter);
    }
}
*/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class BillSplitter extends AppCompatActivity {

    int payer;
    int amount;
    TextView tv;
    EditText amnt;
    Button next;
    ArrayList<String> items;
    ArrayList<String> items1;
    ArrayList<String> finale;
    int backup[][];
    TextView exclude;
    int dnp[];
    boolean honor[];
    static int dnpCount=0;
    Spinner dropdown1, dropdown;
    String gname;
    long usercount = 0;

    ArrayCode ac = new ArrayCode();

    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Group"), druser;
    ChildEventListener cel, ucel;




    int po=0;
    boolean justStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_splitter);
        dropdown = (Spinner) findViewById(R.id.payer);
        dropdown1 = (Spinner) findViewById(R.id.did_not_pay);
        amnt = (EditText) findViewById(R.id.amount);
        next = (Button) findViewById(R.id.Next);
        exclude = (TextView) findViewById(R.id.list_of_Excludes);
        items = new ArrayList<>();
        items1 = new ArrayList<>();
        Intent i1 = getIntent();
        Bundle b = i1.getExtras();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bill Split");
//        actionBar.setDisplayHomeAsUpEnabled(true);


        gname = b.getString("gname");
        druser = FirebaseDatabase.getInstance().getReference().child("Group").child(gname).child("users");
        dr = dr.child(gname).child("BillSplit");



        items1.add("");
        ucel = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserData ud = dataSnapshot.getValue(UserData.class);
                usercount = dataSnapshot.getChildrenCount();
                items.add(ud.retUsername());
                items1.add(ud.retUsername());
                if(usercount>0 && items.size() == usercount){
                    dynADD();
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

        druser.addChildEventListener(ucel);
//create a list of items for the spinner.
//        final String[] items = new String[]{"Faizan", "Mayur", "Yogesh","Rahul"};

//        items.add("Faizan");
//        items.add("yogesh");
//        items.add("Rahul");
//        items.add("Mayur");

//        items1.add("Faizan");
//        items1.add("yogesh");
//        items1.add("Rahul");
//        items1.add("Mayur");
    //    Log.d("USR",items.toString());

//        final String finale[]=new String[100];
        finale = new ArrayList<>();
        backup=new int[20][20];
        dnp=new int[20];
        honor=new boolean[20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                backup[i][j] = 0;
                honor[i]=false;
                dnp[i]=99;
            }
        }


        cel = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BillDataUpload bdu = dataSnapshot.getValue(BillDataUpload.class);
                int c[][] = new int[bdu.retUsers().size()][bdu.retUsers().size()];

                c = ac.decode(bdu.retBillEdge(), bdu.retUsers().size());


                for(int i=0;i<bdu.retUsers().size();i++){
                    for(int j=0;j<bdu.retUsers().size();j++){
                        backup[i][j] = c[i][j];
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                BillDataUpload bdu = dataSnapshot.getValue(BillDataUpload.class);
                int c[][] = new int[20][20];

                c = ac.decode(bdu.retBillEdge(), bdu.retUsers().size());


                for(int i=0;i<bdu.retUsers().size();i++){
                    for(int j=0;j<bdu.retUsers().size();j++){
                        backup[i][j] = c[i][j];
                    }
                }
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

        dr.limitToLast(1).addChildEventListener(cel);




//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.


        amnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (po==0){
                    amnt.setText("");
                }
                po++;
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(), items.toString(),Toast.LENGTH_SHORT).show();

                if(items.size()>0) {

                    if (!amnt.getText().toString().isEmpty()) {
                        amount = Integer.parseInt(amnt.getText().toString());
                        if (amount == 0) {
                            Toast.makeText(getApplication(), "Please Enter an Amount", Toast.LENGTH_LONG).show();
                        } else {

                            //
                            int k = 0;
                            int x = items.size();
                            int n = items.size();
                            int a[][] = new int[x][x];
                            for (int i = 0; i < n; i++) {
                                for (int j = 0; j < n; j++) {
                                    a[i][j] = 0;
                                }
                            }
                            int y = x - 1;
                            int xy = x * y;


//                boolean P[] = new boolean[n];
//                for (int j = 0; j < n; j++) {
//                    P[j] = false;
//                }
//                P[payer] = true;



                            finale = new ArrayList<String>();
                            int share = 0;
                            dnpCount--;
                            share = amount / (x-dnpCount);
                            Log.d("SHARE", share + "");
                            for (int i = 0; i < n; i++) {
                                for (int j = 0; j < n; j++) {
                                    a[i][j] = backup[i][j];
                                }

                            }
//                            for(int j=0;j<n;j++) {
//                                for (int i=0;i<n;i++)
//                                {
//                                    if (dnp[i]!=j)
//                                    {
//                                        honor[j]=true;
//                                    }
//                                }
//                            }

                                for (int i = 0; i < n; i++) {
                                    //Log.d("dnp2Value", "" + dnp[i+1]);
                                    if (i != payer && (99 == dnp[i+1])) {
                                        Log.d("dnpValue", "" + dnp[i+1]);
                                        a[i][payer] = a[i][payer] + share;
                                    }
                                }


                            for (int i = 0; i < n; i++) {
                                for (int j = 0; j < n; j++) {
                                    if (i != j) {
                                        if (a[i][j] > a[j][i]) {
                                            a[i][j] = a[i][j] - a[j][i];
                                            a[j][i] = 0;
                                        } else {
                                            a[j][i] = a[j][i] - a[i][j];
                                            a[i][j] = 0;

                                        }
                                    }
                                }
                            }

                            dr.removeValue();

                            dr.push().setValue(new BillDataUpload(items, ac.encode(a, items.size())));

                            for (int i = 0; i < x; i++) {
                                for (int j = 0; j < x; j++) {
                                    if (i != j)

                                        //finale.remove(j);
                                        if (a[i][j] != 0) {
                                            finale.add(items.get(i) + " to " + items.get(j) + "  " + a[i][j]);
                                        }
                                    Log.d("VALUES", a[i][j] + "");

                                }
                                k++;
                            }

//                        if(justStart) {
                            Intent startNewActivity = new Intent(BillSplitter.this, BillSplitOutput.class).putExtra("bund", finale);
                            finish();
                            startActivity(startNewActivity);
//                        }else{
//                            Intent startNewActivity = new Intent(MainActivity.this, BillSplitOutput.class).putExtra("bund", new ArrayList<String>());
//                            startActivity(startNewActivity);
//                        }

                        }
                        //tv.setText(amount);*/
                    } else {
                        Toast.makeText(getApplication(), "Please Enter an Amount", Toast.LENGTH_LONG).show();
                    }

                }
            }

        });

    }


    public void dynADD(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1);
//set the spinners adapter to the previously created one.


        dropdown.setAdapter(adapter);
        dropdown1.setAdapter(adapter1);



        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                payer = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dropdown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i-1>-1){
                    dnp[i] = i;
                }
                if (dnp[dnpCount]==-1)
                {
                    dnp[dnpCount]=99;
                }
                Log.d("bhau",dnpCount+"   "+dnp[dnpCount]+"");
                dnpCount++;

                exclude.setText(exclude.getText() + "\n" + items1.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*dropdown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dnp[dnpCount++] = i;
                exclude.setText(exclude.getText() + "\n" + items.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
    }

}







