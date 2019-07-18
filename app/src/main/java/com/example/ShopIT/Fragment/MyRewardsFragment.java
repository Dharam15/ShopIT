package com.example.ShopIT.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ShopIT.Adapter.MyRewardAdapter;
import com.example.ShopIT.Model.RewardModel;
import com.example.ShopIT.R;

import java.util.ArrayList;
import java.util.List;


public class MyRewardsFragment extends Fragment {


    public MyRewardsFragment() {
        // Required empty public constructor
    }

    private RecyclerView rewardsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);

        rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardsRecyclerView.setLayoutManager(layoutManager);


        List<RewardModel>rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback","till 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.2500/-"));
        rewardModelList.add(new RewardModel("Discount","till 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.2500/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 free","till 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.2500/-"));
        rewardModelList.add(new RewardModel("Cashback","till 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.2500/-"));
        rewardModelList.add(new RewardModel("Discount","till 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.2500/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 free","till 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.2500/-"));
        rewardModelList.add(new RewardModel("Cashback","till 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.2500/-"));
        rewardModelList.add(new RewardModel("Discount","till 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.2500/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 free","till 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.2500/-"));


        MyRewardAdapter myRewardAdapter = new MyRewardAdapter(rewardModelList,false);
        rewardsRecyclerView.setAdapter(myRewardAdapter);
        myRewardAdapter.notifyDataSetChanged();
        return view;
    }

}
