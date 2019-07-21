package com.example.shoaib.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class bookAdapter extends ArrayAdapter<book>{


    public bookAdapter(Context context, int resource, List<book> arrayList) {
        super(context, resource, arrayList);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       View listView = convertView;

        if (listView == null) {

            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }

        final book current = getItem(position);

        TextView textView = listView.findViewById(R.id.text_authorName);
        if (current.getmAuthor() != null) {
            textView.setText(current.getmAuthor());
        }else {
            textView.setText("Not Available");
        }

        TextView textView1 = listView.findViewById(R.id.text_title);
        if (current.getmTitle() != null) {
            textView1.setText(current.getmTitle());
        }else {
            textView1.setText("Not Available");
        }

        TextView textView2 = listView.findViewById(R.id.text_describtion);
        if (current.getmDescribtion() != null) {
            textView2.setText(current.getmDescribtion());
        }else {
            textView2.setText("Not Available");
        }

        ImageView imageView = listView.findViewById(R.id.image_view);
        if (current.getmImage() != null) {
            Picasso.get().load(current.getmImage()).into(imageView);
        }else {
           imageView = null;
        }


        return listView;


    }

}
