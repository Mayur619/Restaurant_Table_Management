package com.example.dell.sdl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dell on 9/22/2017.
 */

public class Custom extends ArrayAdapter<holder>{
    List<holder> list;
    Activity context;

    public Custom(@NonNull Activity context, List<holder> list) {
        super(context,R.layout.spinner_row,list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflater=context.getLayoutInflater().inflate(R.layout.spinner_row, parent,true);
        TextView item=(TextView)inflater.findViewById(R.id.item);
        TextView cost=(TextView)inflater.findViewById(R.id.cost);
        holder Holder=list.get(position);
        item.setText(Holder.getName());
        cost.setText(Holder.getCost().toString());
        return inflater;
    }
}
