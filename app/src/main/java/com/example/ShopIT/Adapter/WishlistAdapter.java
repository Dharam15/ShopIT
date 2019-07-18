package com.example.ShopIT.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ShopIT.DBquries;
import com.example.ShopIT.Model.WishlistModel;
import com.example.ShopIT.ProductDetailActivity;
import com.example.ShopIT.R;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private Boolean wishlist;

  private List<WishlistModel> wishlistModelList;

  private int lastPosition = -1;

    public WishlistAdapter(List<WishlistModel> wishlistModelList,Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String productId = wishlistModelList.get(position).getProductId();
        String resource = wishlistModelList.get(position).getProductImage();
        String title = wishlistModelList.get(position).getProductTitle();
        long freeCoupens = wishlistModelList.get(position).getFreeCoupens();
        String rating = wishlistModelList.get(position).getRating();
        long totalRatings = wishlistModelList.get(position).getTotalRatings();
        String productPrice = wishlistModelList.get(position).getProductPrice();
        String cuttedPrice = wishlistModelList.get(position).getCuttedPrice();
        boolean paymentMethod = wishlistModelList.get(position).isCOD();
        viewHolder.setData(productId,resource,title,freeCoupens,rating,totalRatings,productPrice,cuttedPrice,paymentMethod,position);

        if(lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupens;
        private ImageView coupenIcon;
        private TextView rating;
        private TextView totalRatings;
        private View priceCut;
        private  TextView productPrice;
        private  TextView cuttedPrice;
        private TextView paymentMethod;
        private ImageButton deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupens = itemView.findViewById(R.id.free_coupen);
            coupenIcon = itemView.findViewById(R.id.coupen_icon);
            rating = itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRatings = itemView.findViewById(R.id.total_ratings);
            priceCut = itemView.findViewById(R.id.price_cut);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            paymentMethod = itemView.findViewById(R.id.payment_method);
            deleteBtn = itemView.findViewById(R.id.delete_btn);

        }
        private void setData(final String productId, String resource, String title, long freeCoupensNo, String averageRate, long totalRatingsNo, final String price, String cuttedPriceValue, boolean COD, final int index){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder)).into(productImage);
            productTitle.setText(title);
            if(freeCoupensNo != 0){
                coupenIcon.setVisibility(View.VISIBLE);
                if(freeCoupensNo ==1) {
                    freeCoupens.setText("free " + freeCoupensNo + " coupen");
                }else {
                    freeCoupens.setText("free " + freeCoupensNo + " coupens");
                }
            }else {
                coupenIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
            }

            rating.setText(averageRate);
            totalRatings.setText("("+totalRatingsNo+")ratings)");
            productPrice.setText("Ksh."+price+"/-");
            cuttedPrice.setText("Ksh."+cuttedPriceValue+"/-");

            if(COD){
                paymentMethod.setVisibility(View.VISIBLE);

            }else {
                paymentMethod.setVisibility(View.INVISIBLE);
            }


            if(wishlist){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailActivity.running_wishlist_query) {
                        ProductDetailActivity.running_wishlist_query = true;
                        DBquries.removeFromWishlist(index, itemView.getContext());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                    productDetailIntent.putExtra("PRODUCT_ID",productId);
                    itemView.getContext().startActivity(productDetailIntent);
                }
            });

        }
    }
}
