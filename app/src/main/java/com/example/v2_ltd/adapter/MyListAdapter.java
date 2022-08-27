package com.example.v2_ltd.adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.v2_ltd.R;
import com.example.v2_ltd.database.entity.SaveData;

import java.util.List;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private List<SaveData> listdata;

    // RecyclerView recyclerView;
    public MyListAdapter(List<SaveData> listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SaveData myListData = listdata.get(position);
        holder.textView_id.setText("ID: " + listdata.get(position).getId());
        holder.textView_address.setText("Address: " + listdata.get(position).getAddress());
        holder.textView_amount.setText(listdata.get(position).getAddress() + " taka");
        String s = listdata.get(position).getImgData();
        if (!s.equals("")) {
            byte[] decodedString = Base64.decode(listdata.get(position).getImgData(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(decodedByte);
        }
//        holder.imageView.setImageResource(bmp);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click on item: " + myListData.getAddress(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView_id, textView_address, textView_amount;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView_id = (TextView) itemView.findViewById(R.id.item_id);
            this.textView_address = (TextView) itemView.findViewById(R.id.item_address);
            this.textView_amount = (TextView) itemView.findViewById(R.id.item_amount);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }
    }
}