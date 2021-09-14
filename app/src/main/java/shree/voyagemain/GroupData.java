package shree.voyagemain;

/**
 * Created by rahul on 25/09/17.
 */

public class GroupData {

    public String groupname;
    public double destLng;
    public double destLat;
    public String ownerName, ownerEmail, ownerUID;

    public GroupData(String groupname, double destLng, double destLat, String ownerName, String ownerEmail, String ownerUID){
        this.groupname = groupname;
        this.destLat = destLat;
        this.destLng = destLng;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.ownerUID = ownerUID;

    }

    public GroupData(){}

    public String retOwnerUID(){
        return ownerUID;
    }

    public String retOwnerName(){ return ownerName; }

    public String retOwnerEmail(){ return ownerEmail; }

    public String retGroupName(){ return groupname; }

    public double retDestlng(){ return destLng; }

    public double retDestlat(){ return destLat; }


}

