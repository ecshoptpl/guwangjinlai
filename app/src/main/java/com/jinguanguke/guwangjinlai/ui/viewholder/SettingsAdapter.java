package com.jinguanguke.guwangjinlai.ui.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.ui.activity.AgreementActivity;
import com.jinguanguke.guwangjinlai.ui.activity.JifenActivity;
import com.jinguanguke.guwangjinlai.update.CheckUpdate;
import com.smartydroid.android.starter.kit.account.AccountManager;

import java.util.List;

/**
 * Created by jin on 16/5/11.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {
    List<String> mListData;

    public SettingsAdapter(List<String> mListData) {
        this.mListData = mListData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        myViewHolder.title.setText(mListData.get(i));
        myViewHolder.title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (i)
                {
                    case 0:
                    {
                        Intent intent = new Intent(v.getContext(),AgreementActivity.class);
                        v.getContext().startActivity(intent);
                    }
                    case 1:
                    {
                        Intent intent = new Intent(v.getContext(),JifenActivity.class);
                        v.getContext().startActivity(intent);
                    }
                    case 2:
                    {
                        CheckUpdate.getInstance().startCheck(v.getContext());
                    }
                    case 3:
                    {
                        //AccountManager.registerObserver(new Logout());
                        AccountManager.logout();
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);


            title = (TextView) itemView.findViewById(R.id.listitem_name);
        }
    }


}
