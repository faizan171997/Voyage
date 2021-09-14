package shree.voyagemain;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by rahul on 25/09/17.
 */

public class GroupAdapter extends ArrayAdapter<GroupData> {

    public GroupAdapter(Context context, int resource, List<GroupData> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.grouplist, parent, false);
        }

        //ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView groupTextView = (TextView) convertView.findViewById(R.id.groupTextView);
        TextView creatorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        GroupData gdata = getItem(position);



        groupTextView.setVisibility(View.VISIBLE);

        groupTextView.setText(gdata.retGroupName());

        creatorTextView.setText(gdata.retOwnerName());

        return convertView;
    }

}
