package shree.voyagemain;

/**
 * Created by rahul on 17/09/17.
 */

public class UserData {
    public String username;
    public String uid;
    public String email;

    public UserData(String username, String uid, String email){
        this.username = username;
        this.uid = uid;
        this.email = email;
    }

    public UserData(){}

    public String retUID(){
        return uid;
    }

    public String retUsername(){ return username; }

    public String retEmail(){ return email; }
}
