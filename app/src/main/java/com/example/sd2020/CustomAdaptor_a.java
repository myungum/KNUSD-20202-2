package com.example.sd2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdaptor_a extends RecyclerView.Adapter<CustomAdaptor_a.CustomViewHolder_a> {
    private ArrayList<Parent> arrayList;
    private Context context;
    public interface  MyRecyclerViewClickListener{
        void onItemClicked(int position);
        void onGoDownClicked(int position);
        void onDeleteClicked(int position);
    }
    private MyRecyclerViewClickListener mListener=null;
    public void setOnClickListener(MyRecyclerViewClickListener listener)
    {
        mListener=listener;
    }
    public CustomAdaptor_a(ArrayList<Parent> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public CustomAdaptor_a.CustomViewHolder_a onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_applied,parent,false);
        CustomAdaptor_a.CustomViewHolder_a holder=new CustomAdaptor_a.CustomViewHolder_a(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdaptor_a.CustomViewHolder_a holder, int position) {
        holder.tv_id.setText(arrayList.get(position).getId());
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_pn.setText(arrayList.get(position).getPn());
        if(mListener!=null)
        {
            final int pos=position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(pos);
                }
            });
            holder.bt_go_down.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mListener.onGoDownClicked(pos);
                }
            });
            holder.bt_delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mListener.onDeleteClicked(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList!=null?arrayList.size():0);
    }

    public static class CustomViewHolder_a extends RecyclerView.ViewHolder {
        TextView tv_name,tv_id,tv_pn;
        Button bt_go_down,bt_delete;
        public CustomViewHolder_a(@NonNull View itemView) {
            super(itemView);
            this.tv_name=itemView.findViewById(R.id.applied_name);
            this.tv_id=itemView.findViewById(R.id.applied_id);
            this.tv_pn=itemView.findViewById(R.id.applied_pn);
            this.bt_go_down=itemView.findViewById(R.id.bt_applied_go_down);
            this.bt_delete=itemView.findViewById(R.id.bt_applied_delete);
        }
    }
}