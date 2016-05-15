package com.jinguanguke.guwangjinlai.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jin on 16/5/15.
 */
public class SubMemberAdapter extends RecyclerView.Adapter<SubMemberAdapter.viewHolder> {

    
    public static class viewHolder extends RecyclerView.ViewHolder {
        viewHolder(View view) {
            view.setOnClickListener(new View.OnClickListener(){
                @Override public  void  onClick(View v) {

                }
            });
        }

    }
}
