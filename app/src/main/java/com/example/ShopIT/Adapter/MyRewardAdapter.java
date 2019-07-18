package com.example.ShopIT.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ShopIT.Model.RewardModel;
import com.example.ShopIT.ProductDetailActivity;
import com.example.ShopIT.R;

import java.util.List;

public class MyRewardAdapter extends RecyclerView.Adapter<MyRewardAdapter.Viewholder> {

    private List<RewardModel> rewardModelList;
 private  Boolean useMiniLayout = false;


    public MyRewardAdapter(List<RewardModel> rewardModelList,Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;

        if(useMiniLayout){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);

       }else {

            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);

       } return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int poisition) {

        String title = rewardModelList.get(poisition).getTitle();
        String date = rewardModelList.get(poisition).getExpiryDate();
        String body = rewardModelList.get(poisition).getCoupenBody();
        viewholder.setData(title,date,body);

    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private  TextView coupenBody;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            coupenTitle = itemView.findViewById(R.id.coupen_title);
            coupenExpiryDate = itemView.findViewById(R.id.coupen_validity);
            coupenBody = itemView.findViewById(R.id.coupen_body);
        }

        private void setData(final String title, final String date, final String body){
            coupenTitle.setText(title);
            coupenExpiryDate.setText(date);
            coupenBody.setText(body);

            if(useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ProductDetailActivity.coupenTitle.setText(title);
                        ProductDetailActivity.coupenExpiryDate.setText(date);
                        ProductDetailActivity.coupenBody.setText(body);
                       ProductDetailActivity.showDialogRecyclerView();

                    }
                });
            }


        }


    }
}
