package shree.voyagemain;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 10/6/17.
 */

public class UserAdapter extends ArrayAdapter<UserData> {

    public UserAdapter(Context context, int resource, List<UserData> objects) {
        super(context, resource, objects);
    }

    TextView memTextView;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.members_list, parent, false);
        }


        memTextView = (TextView) convertView.findViewById(R.id.mem_name);

        UserData gdata = getItem(position);


        memTextView.setVisibility(View.VISIBLE);
        memTextView.setText(gdata.retUsername());
        //creatorTextView.setText(gdata.retOwnerName());

        return convertView;
    }

}
