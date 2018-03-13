package edu.tensorflow.client.camera.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.tensorflow.client.R;
import edu.tensorflow.client.model.Result;

/**
 * Created by Zoltan Szabo on 3/12/18.
 */

public class CustomArrayAdapter extends ArrayAdapter<Result> {
    private final List<Result> values;
    private final LayoutInflater inflater;

    public CustomArrayAdapter(final Context context, final int resource, final List<Result> values) {
        super(context, resource, values.toArray(new Result[]{}));
        this.values = values;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder.listItemImage = convertView.findViewById(R.id.list_item_image);
            holder.listItemText = convertView.findViewById(R.id.list_item_text);
            holder.listItemTitle = convertView.findViewById(R.id.list_item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.listItemTitle.setText(values.get(position).getTitle());
        holder.listItemText.setText(values.get(position).getDescription());
        Picasso.get().load(values.get(position).getURL()).placeholder(R.drawable.placeholder).into(holder.listItemImage);
        return convertView;
    }

    public static class ViewHolder {
        TextView listItemTitle;
        TextView listItemText;
        ImageView listItemImage;
    }
}
