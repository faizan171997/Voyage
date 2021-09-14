package shree.voyagemain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by rahul on 23/09/17.
 */

public class Panaroma extends FragmentActivity
        implements OnStreetViewPanoramaReadyCallback {

    Button nextb, backb;

    String gname, addr;

    double lat = 0, origLat = 0;
    double lng = 0, origLng = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panaroma);

        nextb = (Button) findViewById(R.id.conflb);
        backb = (Button) findViewById(R.id.backb);

        nextb.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),PostPanaDir.class);
                Bundle b = new Bundle();

                b.putDouble("origlat", origLat); //Your id
                i.putExtras(b); //Put your id to your next Intent
                b.putDouble("origlng", origLng);
                i.putExtras(b);
                b.putString("gname", gname);
                i.putExtras(b);
                b.putString("address", addr);
                i.putExtras(b);
                startActivity(i);
            }
        });

        backb.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });





        Bundle b = getIntent().getExtras();
        lat = b.getDouble("lat");
        lng = b.getDouble("lng");
        origLat = b.getDouble("origlat");
        origLng = b.getDouble("origlng");
        gname = b.getString("gname");
        addr = b.getString("address");

        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(new LatLng(lat, lng));
    }
}
