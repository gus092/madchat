package com.example.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterText extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(ContactItem item);
    }
    private Context mContext;
    private List<TextItem> items;
    private OnItemClickListener listener;
    private String user_name;

    private static int TYPE_ENTER = 2;
    private static int TYPE_MY = 1 ;
    private static int TYPE_OTHER = 0;


    public RecyclerViewAdapterText(Context mContext, List<TextItem> items,String name) {
        this.mContext = mContext;
        this.items = items;
        this.user_name =name;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_MY) {
            View v2 = LayoutInflater.from(mContext).inflate(R.layout.my_text_contact, parent, false);
            return new MyChatText(v2);

        }

        if (viewType == TYPE_OTHER){
                View v1 = LayoutInflater.from(mContext).inflate(R.layout.text_contact, parent, false);
                return new OtherTextViewHolder(v1);

        }
        if(viewType == TYPE_ENTER) {
            View v3 = LayoutInflater.from(mContext).inflate(R.layout.chating_enter, parent, false);
            return new EnterChatText(v3);
        }
        View v3 = LayoutInflater.from(mContext).inflate(R.layout.chating_enter, parent, false);
        return new EnterChatText(v3);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {


        if (getItemViewType(position) == TYPE_MY) {
            ((MyChatText) viewHolder).my_chat.setText(items.get(position).getUser_Text());
        }
        if(getItemViewType(position) == TYPE_OTHER){

            ((OtherTextViewHolder) viewHolder).tv_name.setText(items.get(position).getUser_Name());
            ((OtherTextViewHolder) viewHolder).tv_text.setText(items.get(position).getUser_Text());
            ((OtherTextViewHolder) viewHolder).img.setImageBitmap(getBitmapFromString(items.get(position).getUser_Photo()));
            GradientDrawable drawable= (GradientDrawable) mContext.getDrawable(R.drawable.rounded);
            ((OtherTextViewHolder) viewHolder).img.setBackground(drawable);
            ((OtherTextViewHolder) viewHolder).img.setClipToOutline(true);
        }
        if(getItemViewType(position) == TYPE_ENTER){
            ((EnterChatText) viewHolder).enter_chat.setText(items.get(position).getUser_Text());
            ((EnterChatText) viewHolder).enter_chat.setPaintFlags( ((EnterChatText) viewHolder).enter_chat.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (user_name.equals(items.get(position).getUser_Name()) && !(items.get(position).getUser_Photo().equals("not_yet") )) {
            return TYPE_MY;
        }
        if(items.get(position).getUser_Photo().equals("not_yet")){
            return TYPE_ENTER;
        }
        return TYPE_OTHER;

    }


//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView tv_name;
//        private TextView tv_text;
//
//        //private ImageView img;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            tv_name = (TextView) itemView.findViewById(R.id.name_contact2);
//            tv_text = (TextView) itemView.findViewById(R.id.phone_contact2);
//            my_chat = (TextView) itemView.findViewById(R.id.my_chat_text);
//            //img = (ImageView) itemView.findViewById(R.id.img_contact);
//        }
//
//    }

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    class OtherTextViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_text;
        private ImageView img;
        OtherTextViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_text = (TextView) itemView.findViewById(R.id.phone_contact2);
            tv_name = (TextView) itemView.findViewById(R.id.name_contact3);
            img = (ImageView) itemView.findViewById(R.id.profile_img);
        }
    }


    class MyChatText extends RecyclerView.ViewHolder {
        private TextView my_chat;
        MyChatText(@NonNull View itemView) {
            super(itemView);
            my_chat = (TextView) itemView.findViewById(R.id.my_chat_text);
        }
    }

    class EnterChatText extends RecyclerView.ViewHolder {
        private TextView enter_chat;
        EnterChatText(@NonNull View itemView) {
            super(itemView);
            enter_chat = (TextView) itemView.findViewById(R.id.enter_chat);
        }
    }





}





