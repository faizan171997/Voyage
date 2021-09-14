package shree.voyagemain;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.bal.ihsan.streetapi.api.base.CallBack;
import io.bal.ihsan.streetapi.api.base.StreetView;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by rahul on 23/09/17.
 */

public class popup extends Activity {

    EditText gname,  destname;
    ViewPager viewPager;

    Button cbutton, nextb, viewM;
    String path;

    boolean gnavail = false;

    final int MY_PERMISSION_FINE_LOCATION = 101;

    ImageView img;

    LatLng origLatLng;
    RelativeLayout mainLayout;
    ArrayList<String> mylist = new ArrayList<String>();

    //StreetView streetView;
    Activity a;

    //FloatingSearchView fsv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);
        a = this;


//        streetView= new StreetView.Builder("AIzaSyAtA3f4ylsRDvXzm0CeVJ8vUeLFEx4b9M0")
//                .pitch("-0.76")
//                .heading("80.0")
//                .size("600x400")
//                .fov("90")
//                .build();



// Get your layout set up, this is just an example
        mainLayout = (RelativeLayout)findViewById(R.id.rl);
        viewM = (Button) findViewById(R.id.viewMap);
        //actionBar.show();
        img = (ImageView)findViewById(R.id.imgeview);
        gname = (EditText)findViewById(R.id.gredit);
        destname = (EditText)findViewById(R.id.destedit);
        cbutton = (Button)findViewById(R.id.cancelb);
        nextb = (Button)findViewById(R.id.pictureb);
        //fsv = (FloatingSearchView)findViewById(R.id.floating_search_view);

        //fsv.setSearchBarTitle("jhvjknfvkrl");
        //fsv.




        viewM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gnavail) {
                    requestPermissions();
                    int PLACE_PICKER_REQUEST = 1;
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    try {
                        startActivityForResult(builder.build(a), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(a, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                }


            }
        });

        gname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!gname.getText().toString().trim().isEmpty()){
                    //destname.setEnabled(true);
                    viewM.setEnabled(true);
                    gnavail = true;
                    if(!destname.getText().toString().trim().isEmpty()){
                        nextb.setEnabled(true);
                    }
                }else if(gname.getText().toString().trim().isEmpty()){
                    //destname.setEnabled(false);
                    nextb.setEnabled(false);
                    viewM.setEnabled(false);
                    gnavail = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        destname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!destname.getText().toString().trim().isEmpty()){
                    nextb.setEnabled(true);
                }else if(destname.getText().toString().trim().isEmpty()){
                    nextb.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbutton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });


        nextb.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                double lat, lng;
                lat = lng = 0;



                    final LatLng latlng = randLoc(origLatLng.latitude, origLatLng.longitude);
                    lat = latlng.latitude;
                    lng = latlng.longitude;





// Then just use the following:
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                    Toast.makeText(getApplicationContext(),"lat : "+lat+", lng : "+lng,Toast.LENGTH_SHORT).show();



//                    streetView.getStreetView(lat, lng, new CallBack() {
//                        @Override
//                        public void onResponse(Response<ResponseBody> response, Retrofit retrofit, Bitmap bitmapStreetView) {
//                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                            bitmapStreetView.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//
//                            path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmapStreetView, "Title", null);
//                            //return Uri.parse(path);
//
//
//
//
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//                            t.printStackTrace();
//                        }
//                    });
//
/*

                    //viewPager = (ViewPager)findViewById(R.id.viewpager);
                    //adapter = new ViewPageAdapter(popup.this, images);
                    //viewPager.setAdapter(adapter);

                    Intent i=new Intent(getApplicationContext(),Panaroma.class);
                    Bundle b = new Bundle();
                    b.putDouble("lat", lat); //Your id
                    i.putExtras(b); //Put your id to your next Intent
                    b.putDouble("lng", lng);
                    i.putExtras(b);

                    startActivity(i);*/
                    img.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    int width = view.getMeasuredWidth();
                    int height = view.getMeasuredHeight();

                    String imageURL = "https://maps.googleapis.com/maps/api/streetview?size="+width*5+"x"+height*5+"&location="+lat+","+lng+"&fov=90&heading=235&pitch=10";




                    Picasso.with(getApplicationContext()).load(imageURL).into(img);
                    /*
                    Bundle b = new Bundle();
                    b.putDouble("lat", lat); //Your id
                    i.putExtras(b); //Put your id to your next Intent
                    b.putDouble("lng", lng);
                    i.putExtras(b);*/
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           Intent i=new Intent(getApplicationContext(),Panaroma.class);
                            Bundle b = new Bundle();
                            b.putDouble("lat", latlng.latitude); //Your id
                            i.putExtras(b); //Put your id to your next Intent
                            b.putDouble("lng", latlng.longitude);
                            i.putExtras(b);
                            b.putDouble("origlat", origLatLng.latitude); //Your id
                            i.putExtras(b); //Put your id to your next Intent
                            b.putDouble("origlng", origLatLng.longitude);
                            i.putExtras(b);
                            b.putString("gname", gname.getText().toString().trim());
                            i.putExtras(b);
                            b.putString("address", destname.getText().toString().trim());
                            i.putExtras(b);



                            startActivity(i);
                        }
                    });

            }
        });
    }

    private void requestPermissions() {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case MY_PERMISSION_FINE_LOCATION :
                if(grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"This App Requires Location Permission", Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Place p = PlacePicker.getPlace(this, data);
                destname.setText(p.getAddress());
                origLatLng = p.getLatLng();
                if(p.getAttributions() == null){

                }

            }
        }
    }

    public LatLng randLoc(double lat, double lng){
        double min = -0.0009000;
        double max = 0.0009000;
        double random = new Random().nextDouble();
        double result = min + (random * (max - min));
        lat = lat + result;

        random = new Random().nextDouble();
        result = min + (random * (max - min));
        lng = lng + result;

        return new LatLng(lat, lng);

    }




}
