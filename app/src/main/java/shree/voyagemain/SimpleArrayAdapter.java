package shree.voyagemain;

/**
 * Created by root on 10/7/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 10/2/17.
 */
public class SimpleArrayAdapter extends BaseAdapter {
    ArrayList<String> name = new ArrayList<>();
    Context ctx;
    LayoutInflater myinflator;
    public SimpleArrayAdapter(ArrayList <String> arr,Context c)
    {
        name=arr;
        ctx=c;
        myinflator=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }    @Override
    public int getCount() {
        if (name.size()==0)
        {
            return 0;
        }
        else
        {
            return name.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return name.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        if(arg1==null)
            arg1= myinflator.inflate(android.R.layout.simple_list_item_1,arg2,false);
        TextView name1=(TextView)arg1.findViewById(android.R.id.text1);
        name1.setText(name.get(arg0));

        return arg1;
    }
}

