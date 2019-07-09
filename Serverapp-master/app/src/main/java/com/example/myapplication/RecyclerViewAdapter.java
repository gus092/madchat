package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.myapplication.Frag3.ImageAdapter.context;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {





    public interface OnItemClickListener {
        void onItemClick(ContactItem item);
    }

    private Context mContext;
    private List<ContactItem> items;
    private OnItemClickListener listener;



    public RecyclerViewAdapter(Context mContext, List<ContactItem> items, OnItemClickListener listener) {
        this.mContext = mContext;
        this.items = items;
        this.listener = listener;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v= LayoutInflater.from(mContext).inflate(R.layout.item_contact,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_name.setText(items.get(position).getUser_Name());


        holder.bind(items.get(position), listener);
        //holder.img.setImageResource(mData.get(position).getPhoto_id());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_phone;
        //private ImageView img;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.name_contact);
            //img = (ImageView) itemView.findViewById(R.id.img_contact);


            ImageView imageView = (ImageView) itemView.findViewById(R.id.img_contact);
            GradientDrawable drawable= (GradientDrawable) mContext.getDrawable(R.drawable.background_rounding);
            imageView.setBackground(drawable);
            imageView.setClipToOutline(true);

        }

        public void bind(final ContactItem item, final OnItemClickListener listener) {
            tv_name.setText(item.getUser_Name());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

        }
    }


}
