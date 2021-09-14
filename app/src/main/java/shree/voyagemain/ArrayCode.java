package shree.voyagemain;

/**
 * Created by root on 10/7/17.
 */
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by root on 10/2/17.
 */

public class ArrayCode {

    public ArrayList<Integer> encode(int a[][], int size){
        ArrayList<Integer> al = new ArrayList<>();
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                al.add(a[i][j]);
            }
        }
        return al;

    }

    public int[][] decode(ArrayList<Integer> ae, int size){

        int a[][] = new int[20][20];
        int pos = 0;

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                a[i][j] = ae.get(pos);
                //Log.d("POS", ae.get(pos)+"");
                pos++;
            }
        }

        return a;

    }
}
