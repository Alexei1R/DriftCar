package com.example.utmcontroll.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.utmcontroll.R;

import java.util.ArrayList;
import java.util.List;


public class BTAdapter extends ArrayAdapter<ListItem> {
    public static final String DEF_ITEM_TYPE = "normal";
    public static final String TITLE_ITEM_TYPE = "title";
    public static final String DISCOVERY_ITEM_TYPE = "discovery";
    private List<ListItem> mainList;
    private List<ViewHolder> listViewHolders;
    private SharedPreferences pref;
    private boolean isDiscoveryTape = false;



    public BTAdapter(@NonNull Context context, int resource, List<ListItem> bt_list) {
        super(context, resource,bt_list);
        mainList = bt_list;
        listViewHolders = new ArrayList<>();
        pref = context.getSharedPreferences(BTConstd.MY_PREF,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        switch (mainList.get(position).getitemType()){

            case TITLE_ITEM_TYPE:
                    convertView = titleItem(convertView,parent);
                break;
            default:convertView = defaultItem(convertView,position,parent);
                break;
        }
        return convertView;
    }

    private  void savePref(int pos){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BTConstd.MAC_KEY,mainList.get(pos).getBtDevice().getAddress());
        editor.apply();
    }


    static class
    ViewHolder{

        TextView tvBTName;
        CheckBox chBTSelected;

    }




    private  View titleItem(View convertView,ViewGroup parent){
        boolean hasViewHolder = false;
        if(convertView != null){ hasViewHolder = convertView.getTag() instanceof ViewHolder;}
        if(convertView == null || hasViewHolder){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_list_item_title,null,false);
        }
        return convertView;
}


    private  View defaultItem(View convertView,int position,ViewGroup parent){
        ViewHolder viewHolder;
        boolean hasViewHolder = false;
        if(convertView != null){ hasViewHolder = convertView.getTag() instanceof ViewHolder; }
        if(convertView == null || !hasViewHolder){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_list_item,null,false);
            viewHolder.tvBTName = convertView.findViewById(R.id.tv_bt_name);
            viewHolder.chBTSelected = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
            listViewHolders.add(viewHolder);

        }else {

            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.chBTSelected.setChecked(false);
        }

        if(mainList.get(position).getitemType().equals(BTAdapter.DISCOVERY_ITEM_TYPE)){
            viewHolder.chBTSelected.setVisibility(View.GONE);
            isDiscoveryTape = true;
        }else{
            viewHolder.chBTSelected.setVisibility(View.VISIBLE);
            isDiscoveryTape = false;
        }
        viewHolder.tvBTName.setText(mainList.get(position).getBtDevice().getName());
        viewHolder.chBTSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDiscoveryTape){

                for(ViewHolder holder : listViewHolders){
                    holder.chBTSelected.setChecked(false);
                }
                viewHolder.chBTSelected.setChecked(true);
                savePref(position);
                }
            }
        });

        if(pref.getString(BTConstd.MAC_KEY,"no BT selected").equals(mainList.get(position).getBtDevice().getAddress())){
            viewHolder.chBTSelected.setChecked(true);
        }
        //viewHolder.chBTSelected.setChecked(true);
        isDiscoveryTape = false;
        return convertView;
    }
}
