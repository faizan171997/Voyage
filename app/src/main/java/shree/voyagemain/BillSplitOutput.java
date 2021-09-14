package shree.voyagemain;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

public class BillSplitOutput extends AppCompatActivity {

    SimpleArrayAdapter sa;
    Button ShowAct;
    boolean st=true;
    //    public BillSplitOutput(String arr[])
//    {
//        name1=arr;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_split_output);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        Bundle b = i.getExtras();
//        name1=b.getStringArray("bund");
        ArrayList<String> name1 = null;


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));


        try

        {
            name1=b.getStringArrayList("bund");
        }
        catch (Exception E)
        {

        }

        //Log.d("USERS",name1.toString());

        ShowAct=(Button)findViewById(R.id.show);
        ListView lv= (ListView)findViewById(R.id.list);
        sa=null;
        sa=new SimpleArrayAdapter(name1,getApplicationContext());
        // Toast.makeText(getApplicationContext(),"y",Toast.LENGTH_LONG).show();

        st=false;

        lv.setAdapter(sa);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

    }



}
