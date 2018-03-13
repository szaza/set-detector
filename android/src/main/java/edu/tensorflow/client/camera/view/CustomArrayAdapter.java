package edu.tensorflow.client.camera.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import edu.tensorflow.client.R;
import edu.tensorflow.client.api.dto.ValidSetDTO;

/**
 * Created by Zoltan Szabo on 3/12/18.
 */

public class CustomArrayAdapter extends ArrayAdapter<ValidSetDTO> {

    private final Context context;
    private final ValidSetDTO[] values;
    private final LayoutInflater inflater;

    public CustomArrayAdapter(Context context, int resource, ValidSetDTO[] values) {
        super(context, resource, values);
        this.context = context;
        this.values = values;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder.imageView = convertView.findViewById(R.id.image);
            holder.textView = convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(values[position].getSetOfCards().toString());
        final String URL = values[position].getImagePath();
        Glide.with(context)
                .load(URL)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher)
                .crossFade()
                .into(holder.imageView);

        return convertView;
    }

    public static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
