package com.example.dima.player;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dima on 5/15/2018.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>  {
    private List<Composition> list_compositions;
    private Context context;
    private Context mainContext;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTitle;
        public TextView txtAuthor;
        public ImageView imgIcon;
        public ImageView imgGroup;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtTitle = (TextView) v.findViewById(R.id.firstLine);
            txtAuthor = (TextView) v.findViewById(R.id.secondLine);
            imgIcon = v.findViewById(R.id.playInRecycleView);
            imgGroup = v.findViewById(R.id.imageOfGroup);
            //this.listener = listener;

        }
    }


    public RecycleViewAdapter(Context context, List<Composition> myDataset){
        list_compositions = myDataset;
        mainContext = context;
    }

    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycleview_row, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(final RecycleViewAdapter.ViewHolder holder, final int position) {
        final Composition composition = list_compositions.get(position);
        holder.txtTitle.setText(composition.getTitle());
        holder.imgIcon.setImageResource(composition.getIcon());
        holder.txtAuthor.setText("Author: " + composition.getAuthor());
        holder.imgGroup.setImageResource(composition.getImageGroup());

        holder.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mainContext).createMediaPlayer(composition.getSong(), true, position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return list_compositions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
