package shree.voyagemain;

/**
 * Created by Faizan-PC on 9/11/2017.
 */

public class MapDataUpload {
    public String username;
    public double lat;
    public double lng;

    public MapDataUpload(String username, double lat, double lng){
        this.username = username;
        this.lat = lat;
        this.lng = lng;
    }

    public MapDataUpload(){}

    public String retUserName(){
        return username;
    }

    public double retLatitude(){
        return lat;
    }

    public double retLongitude(){
        return lng;
    }


}
