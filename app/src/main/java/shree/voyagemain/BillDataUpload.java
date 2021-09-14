package shree.voyagemain;

import java.util.ArrayList;

/**
 * Created by root on 10/2/17.
 */

public class BillDataUpload {
    public ArrayList<Integer> a;
    public ArrayList<String> users;

    public BillDataUpload(){}

    public BillDataUpload(ArrayList<String> it, ArrayList<Integer> b){
        users = it;


        a = b;
    }

    ArrayList<Integer> retBillEdge(){
        return a;
    }

    ArrayList<String> retUsers(){
        return users;
    }


}
