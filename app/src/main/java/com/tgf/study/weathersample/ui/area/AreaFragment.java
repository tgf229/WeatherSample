package com.tgf.study.weathersample.ui.area;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tgf.study.weathersample.R;
import com.tgf.study.weathersample.bean.area.AreaBean;
import com.tgf.study.weathersample.bean.area.CityBean;
import com.tgf.study.weathersample.bean.area.CountyBean;
import com.tgf.study.weathersample.bean.area.ProvinceBean;
import com.tgf.study.weathersample.db.CityDAO;
import com.tgf.study.weathersample.db.CountyDAO;
import com.tgf.study.weathersample.db.ProvinceDAO;
import com.tgf.study.weathersample.net.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AreaFragment extends Fragment implements View.OnClickListener{
    public static final int PROVINCE_LEVEL = 0;
    public static final int CITY_LEVEL = 1;
    public static final int COUNTY_LEVEL = 2;
    public int CURRENT_LEVEL = PROVINCE_LEVEL;

    private static final String TAG = "AreaFragment";
    private List<AreaBean> mList = new ArrayList<AreaBean>();
    private RecyclerView recyclerView;
    private AreaAdapter adapter;

    private ProvinceDAO provinceDAO;
    private CityDAO cityDAO;
    private CountyDAO countyDAO;

    public String selectedProvince;
    public String selectedCity;

    private RelativeLayout back_btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_area,container,false);

        back_btn = (RelativeLayout) view.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new AreaAdapter(mList,this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryProvince();
    }

    public void queryProvince(){
        CURRENT_LEVEL = PROVINCE_LEVEL;
        back_btn.setVisibility(View.GONE);
        //从数据库取数据若有
        provinceDAO = new ProvinceDAO(getActivity());
        List<ProvinceBean> list = provinceDAO.query();
        if (list.size() > 0 ){
            mList.clear();
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }else{
            //若无,则网络请求
            queryFromServer(HttpUtil.URL_ADDR);
        }
    }

    public void queryCity(){
        CURRENT_LEVEL = CITY_LEVEL;
        back_btn.setVisibility(View.VISIBLE);
        cityDAO = new CityDAO(getActivity());
        List<CityBean> list = cityDAO.query(selectedProvince);
        if (list.size() > 0){
            mList.clear();
            mList.addAll(list);
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(0);
        }else{
            queryFromServer(HttpUtil.URL_ADDR + "/" + selectedProvince);
        }
    }

    public void queryCounty(){
        CURRENT_LEVEL = COUNTY_LEVEL;
        back_btn.setVisibility(View.VISIBLE);
        countyDAO = new CountyDAO(getActivity());
        List<CountyBean> list = countyDAO.query(selectedCity);
        if (list.size() > 0){
            mList.clear();
            mList.addAll(list);
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(0);
        }else{
            queryFromServer(HttpUtil.URL_ADDR + "/" + selectedProvince + "/" + selectedCity);
        }
    }

    private void queryFromServer(String addr){
        Log.i(TAG, "queryFromServer: ");
        HttpUtil.sendRequest(addr, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                //同步存储数据库
                if (CURRENT_LEVEL == PROVINCE_LEVEL){
                    Gson gson = new Gson();
                    List<ProvinceBean> list = gson.fromJson(result,new TypeToken<List<ProvinceBean>>(){}.getType());
                    for (ProvinceBean b:list){
                        provinceDAO.insert(b);
                    }
                }else if (CURRENT_LEVEL == CITY_LEVEL){
                    Gson gson = new Gson();
                    List<CityBean> list = gson.fromJson(result,new TypeToken<List<CityBean>>(){}.getType());
                    for (CityBean bean : list){
                        bean.setProvinceId(selectedProvince);
                        cityDAO.insert(bean);
                    }
                }else if(CURRENT_LEVEL == COUNTY_LEVEL){
                    Gson gson = new Gson();
                    List<CountyBean> list = gson.fromJson(result,new TypeToken<List<CountyBean>>(){}.getType());
                    for (CountyBean bean : list){
                        bean.setCityId(selectedCity);
                        countyDAO.insert(bean);
                    }
                }

                //给主线程发消息 重新取数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (CURRENT_LEVEL == PROVINCE_LEVEL){
                            queryProvince();
                        }else if(CURRENT_LEVEL == CITY_LEVEL){
                            queryCity();
                        }else if(CURRENT_LEVEL == COUNTY_LEVEL){
                            queryCounty();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"请求失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                if (CURRENT_LEVEL == CITY_LEVEL){
                    queryProvince();
                }else if(CURRENT_LEVEL == COUNTY_LEVEL){
                    queryCity();
                }
                break;
        }
    }
}
