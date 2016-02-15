package com.sourceit.task1.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourceit.task1.R;
import com.sourceit.task1.model.BroadcasterModel;

import java.util.ArrayList;

/**
 * Created by User on 15.02.2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<BroadcasterModel> broadcasters;

    public MyAdapter(ArrayList<BroadcasterModel> broadcasters) {
        this.broadcasters = broadcasters;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.broadcaster, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.title.setText(String.valueOf(broadcasters.get(i).getTitle()));
        viewHolder.state.setText(String.valueOf(broadcasters.get(i).getState()));
    }

    @Override
    public int getItemCount() {
        return broadcasters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView state;

        public ViewHolder(View item) {
            super(item);
            title = (TextView) item.findViewById(R.id.broadcaster_title);
            state = (TextView) item.findViewById(R.id.broadcaster_state);
        }
    }
}
