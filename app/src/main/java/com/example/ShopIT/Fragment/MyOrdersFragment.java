package com.example.ShopIT.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ShopIT.Adapter.MyOrderAdapter;
import com.example.ShopIT.Model.MyOrderItemModel;
import com.example.ShopIT.R;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersFragment extends Fragment {


    public MyOrdersFragment() {
        // Required empty public constructor
    }

  private RecyclerView myOrderRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_orders, container, false);

        myOrderRecyclerView = view.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrderRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
         myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mob5,2,"Pixel 2XL (BLACK)","Delivered on Mon,15th JUN 2019"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mob1,1,"Pixel 2XL (BLACK)","Delivered on Mon,15th JUN 2019"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mob2,0,"Pixel 2XL (BLACK)","Cancelled"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mob7,4,"Pixel 2XL (BLACK)","Delivered on Mon,15th JUN 2019"));

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrderRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();

        return view;
    }

}
