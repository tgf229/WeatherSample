package com.tgf.study.weathersample.ui.area;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tgf.study.weathersample.R;
import com.tgf.study.weathersample.bean.area.AreaBean;
import com.tgf.study.weathersample.bean.area.CountyBean;

import java.util.List;

/**
 * Created by tugaofeng on 17/3/30.
 */
public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {
    private static final String TAG = "AreaAdapter";
    private List<AreaBean> mList;
    private AreaFragment fragment;

    public AreaAdapter(List<AreaBean> mList,AreaFragment fragment) {
        this.mList = mList;
        this.fragment = fragment;
    }

    @Override
    public AreaViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_area_list_item,parent,false);
        final AreaViewHolder vh = new AreaViewHolder(view);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vh.getAdapterPosition();
                if (fragment.CURRENT_LEVEL == fragment.PROVINCE_LEVEL){
                    fragment.selectedProvince = mList.get(position).getId();
                    fragment.queryCity();
                }else if (fragment.CURRENT_LEVEL == fragment.CITY_LEVEL){
                    fragment.selectedCity = mList.get(position).getId();
                    fragment.queryCounty();
                }else if (fragment.CURRENT_LEVEL == fragment.COUNTY_LEVEL){
                    Toast.makeText(parent.getContext(),((CountyBean)mList.get(position)).getWeatherId(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position) {
        holder.name_txt.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class AreaViewHolder extends RecyclerView.ViewHolder{
        private TextView name_txt;
        private String clickId;
        public AreaViewHolder(View itemView) {
            super(itemView);
            name_txt = (TextView) itemView.findViewById(R.id.name_txt);
        }
    }
}
