package com.homework.lq.mynote;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.sql.SQLClientInfoException;
import java.util.List;
/**
 * Created by ASUS on 2018/5/28.
 */

public class InforAdapter extends RecyclerView.Adapter<InforAdapter.ViewHolder>  {
    private List<Information> mInforList;
    private OnItemOnClickListener mOnItemOnClickListener;
    private int key;
    private int key1;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView inforColor;
        TextView inforContent;
        TextView inforDate;

        public ViewHolder(View view) {
            super(view);
            inforColor = (ImageView) view.findViewById(R.id.infor_color);
            inforContent = (TextView) view.findViewById(R.id.infor_content);
            inforDate = (TextView) view.findViewById(R.id.infor_date);
        }
    }
    public InforAdapter(List<Information> inforList){
        mInforList = inforList;
    }

    public interface OnItemOnClickListener{
        void onItemOnClick(View view, int pos);
        void onItemLongOnClick(View view, int pos);
    }

    public void setOnItemClickListener(OnItemOnClickListener listener){
        this.mOnItemOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.infor_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        Information infor = mInforList.get(position);
        int degree = infor.getDegree();
        if(degree == 1){
            holder.inforColor.setBackgroundColor(Color.parseColor("#87CEFF"));
        }else if (degree == 2){
            holder.inforColor.setBackgroundColor(Color.parseColor("#FF00FF"));
        }else if (degree == 3){
            holder.inforColor.setBackgroundColor(Color.parseColor("#90EE90"));
        } else if (degree == 4){
            holder.inforColor.setBackgroundColor(Color.parseColor("#FFFF00"));
        }else if (degree == 5){
            holder.inforColor.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        holder.inforContent.setText(infor.getContent());
        holder.inforDate.setText(infor.getDateDifferent());

        if(mOnItemOnClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Information information1 = mInforList.get(position);
                    int  id1=information1.getId();
                    key1=id1;
                    mOnItemOnClickListener.onItemOnClick(holder.itemView,position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Information information = mInforList.get(position);
                    int  id=information.getId();
                    key=id;
                    mOnItemOnClickListener.onItemLongOnClick(holder.itemView, position);
                    mInforList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    return true;
                }
            });
        }
    }
    @Override
    public int getItemCount(){
        return mInforList.size();
    }
    public int getKey()
    {
        return key;
    }
    public int getKey1(){return key1;}
}
