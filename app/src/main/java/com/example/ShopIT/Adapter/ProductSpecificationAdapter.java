package com.example.ShopIT.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ShopIT.Model.ProductSpecificationnModel;
import com.example.ShopIT.R;

import java.util.List;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {

    private List<ProductSpecificationnModel> productSpecificationnModelList;

    public ProductSpecificationAdapter(List<ProductSpecificationnModel> productSpecificationnModelList) {
        this.productSpecificationnModelList = productSpecificationnModelList;
    }


    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationnModelList.get(position).getType()) {
            case 0:
                return ProductSpecificationnModel.SPECIFICATION_TITLE;

            case 1:
                return ProductSpecificationnModel.SPECIFICATION_BODY;

            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case ProductSpecificationnModel.SPECIFICATION_TITLE:
                TextView title = new TextView(viewGroup.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16, viewGroup.getContext())
                        , setDp(16, viewGroup.getContext()),
                        setDp(16, viewGroup.getContext()),
                        setDp(8, viewGroup.getContext()));
                title.setLayoutParams(layoutParams);
               return new ViewHolder(title);

            case ProductSpecificationnModel.SPECIFICATION_BODY:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_specification_item_layout, viewGroup, false);
                return new ViewHolder(view);
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder viewHolder, int position) {

        switch (productSpecificationnModelList.get(position).getType()){

            case ProductSpecificationnModel.SPECIFICATION_TITLE:
                viewHolder.setTitle(productSpecificationnModelList.get(position).getTitle());

                break;

                case ProductSpecificationnModel.SPECIFICATION_BODY:
                    String featureTitle = productSpecificationnModelList.get(position).getFeatureName();
                    String featureDetail = productSpecificationnModelList.get(position).getFeatureValue();
                    viewHolder.setFeature(featureTitle, featureDetail);
                    break;

                    default:
                        return;
        }




    }

    @Override
    public int getItemCount() {
        return productSpecificationnModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView featureName;
        private TextView featureValue;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        private void setTitle(String titleText){
            title = (TextView) itemView;
            title.setText(titleText);
        }


        private void setFeature(String featureTitle, String featureDetail) {
            featureName = itemView.findViewById(R.id.feature_name);
            featureValue = itemView.findViewById(R.id.feature_value);
            featureName.setText(featureTitle);
            featureValue.setText(featureDetail);
        }

    }

    private int setDp(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());


    }
}
