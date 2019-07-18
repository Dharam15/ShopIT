package com.example.ShopIT.Adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ShopIT.Model.HomePageModel;
import com.example.ShopIT.Model.HorizontalProductScrollModel;
import com.example.ShopIT.Model.SliderModel;
import com.example.ShopIT.Model.WishlistModel;
import com.example.ShopIT.ProductDetailActivity;
import com.example.ShopIT.R;
import com.example.ShopIT.ViewAllActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int lastPosition = -1;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }


    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()){
            case 0:
                return HomePageModel.BANNER_SLIDER;

            case 1:
                return HomePageModel.STRIP_AD_BANNER;

            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;

            case 3:
                return HomePageModel.GRID_PRODUCT_VIEW;

                default:
                    return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType){
            case HomePageModel.BANNER_SLIDER:
            View bannerSliderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sliding_ad_layout,viewGroup,false);
            return new BannerSliderViewholder(bannerSliderView);


            case HomePageModel.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.strip_ad_layout,viewGroup,false);
                return new StripAdBannerViewholer(stripAdView);

            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizontalProductView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout,viewGroup,false);
                return new HorizontalProductViewholder(horizontalProductView);

            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridProductView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_product_layout,viewGroup,false);
                return new GridProductViewholder(gridProductView);


            default:
                return null;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
       switch (homePageModelList.get(position).getType()){
           case HomePageModel.BANNER_SLIDER:
               List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
               ((BannerSliderViewholder)viewHolder).setBannerSliderViewPager(sliderModelList);
                 break;

                 case HomePageModel.STRIP_AD_BANNER:
                     String resource = homePageModelList.get(position).getResource();
                     String color = homePageModelList.get(position).getBackgroundColor();
                     ((StripAdBannerViewholer)viewHolder).setStripAd(resource,color);
                     break;
                   case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                       String layoutColor = homePageModelList.get(position).getBackgroundColor();
                        String horizontalLayoutTitle = homePageModelList.get(position).getTitle();
                        List<WishlistModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                         List<HorizontalProductScrollModel>horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                            ((HorizontalProductViewholder)viewHolder).setHorizontalProductLayout(horizontalProductScrollModelList,horizontalLayoutTitle,layoutColor,viewAllProductList);
                             break;
                     case HomePageModel.GRID_PRODUCT_VIEW:
                         String gridLayoutColor = homePageModelList.get(position).getBackgroundColor();
                         String gridLayoutTitle = homePageModelList.get(position).getTitle();
                         List<HorizontalProductScrollModel>gridProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                         ((GridProductViewholder)viewHolder).setGridProductLayout(gridProductScrollModelList,gridLayoutTitle,gridLayoutColor);
           default:
                   return;
       }

       if(lastPosition < position) {
           Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
           viewHolder.itemView.setAnimation(animation);
           lastPosition = position;
       }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }


    public class BannerSliderViewholder extends RecyclerView.ViewHolder{

        private ViewPager bannerSliderViewPager;
        private int currentPage;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
         private List<SliderModel> arrangeList;


        public BannerSliderViewholder(@NonNull View itemView) {
            super(itemView);

            bannerSliderViewPager = itemView.findViewById(R.id.banner_slider_view_pager);


        }

         private void setBannerSliderViewPager(final List<SliderModel>sliderModelList){

            currentPage = 2;
            if(timer != null){
                timer.cancel();
            }
             arrangeList = new ArrayList<>();
             for(int x = 0;x < sliderModelList.size();x++){
                 arrangeList.add(x,sliderModelList.get(x));
             }
             arrangeList.add(0,sliderModelList.get(sliderModelList.size() -2));
             arrangeList.add(1,sliderModelList.get(sliderModelList.size() -1));
             arrangeList.add(sliderModelList.get(0));
             arrangeList.add(sliderModelList.get(1));

             SliderAdapter sliderAdapter = new SliderAdapter(arrangeList);
             bannerSliderViewPager.setAdapter(sliderAdapter);
             bannerSliderViewPager.setClipToPadding(false);
             bannerSliderViewPager.setPageMargin(20);

             bannerSliderViewPager.setCurrentItem(currentPage);

             ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                 @Override
                 public void onPageScrolled(int i, float v, int i1) {

                 }

                 @Override
                 public void onPageSelected(int i) {
                     currentPage = i;

                 }

                 @Override
                 public void onPageScrollStateChanged(int i) {
                     if(i == ViewPager.SCROLL_STATE_IDLE){
                         pageLooper(arrangeList);

                     }

                 }

             };

             bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

             startBannerSlideShow(arrangeList);

             bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                 @Override
                 public boolean onTouch(View v, MotionEvent event) {
                     pageLooper(arrangeList);
                     stopBannerSlideshow();
                     if(event.getAction() == MotionEvent.ACTION_UP){
                         startBannerSlideShow(arrangeList);

                     }


                     return false;
                 }
             });


         }

        private void pageLooper(List<SliderModel>sliderModelList){
            if(currentPage == sliderModelList.size() - 2){
                currentPage = 2;
                bannerSliderViewPager.setCurrentItem(currentPage,false);
            }
            if(currentPage == 1 ){
                currentPage = sliderModelList.size() - 3 ;
                bannerSliderViewPager.setCurrentItem(currentPage,false);
            }

        }

        private void startBannerSlideShow(final List<SliderModel>sliderModelList){
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size()){
                        currentPage = 1;
                    }

                    bannerSliderViewPager.setCurrentItem(currentPage++,true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            },DELAY_TIME,PERIOD_TIME);


        }

        private void stopBannerSlideshow(){
            timer.cancel();
        }
    }

    public class StripAdBannerViewholer extends RecyclerView.ViewHolder{

        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        public StripAdBannerViewholer(@NonNull View itemView) {
            super(itemView);
            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
        }

         private void setStripAd(String resource,String color){
             Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder)).into(stripAdImage);
             stripAdContainer.setBackgroundColor(Color.parseColor(color));


}

    }

    public class HorizontalProductViewholder extends RecyclerView.ViewHolder{

        private  ConstraintLayout container;
        private TextView horizontalLayoutTitle;
        private Button horizontalLayoutViewAllBtn;
        private RecyclerView horizontalRecyclerView;

        public HorizontalProductViewholder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalLayoutViewAllBtn = itemView.findViewById(R.id.horizontal_scroll_view_all_button);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recyclerview);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);
        }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            private void setHorizontalProductLayout(List<HorizontalProductScrollModel>horizontalProductScrollModelList, final String title, String color, final List<WishlistModel> viewAllProductList){

                container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                 horizontalLayoutTitle.setText(title);

                 if(horizontalProductScrollModelList.size() > 8){
                     horizontalLayoutViewAllBtn.setVisibility(View.VISIBLE);

                     horizontalLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             ViewAllActivity.wishlistModelList = viewAllProductList;
                             Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                             viewAllIntent.putExtra("layout_code",0);
                             viewAllIntent.putExtra("title",title);
                             itemView.getContext().startActivity(viewAllIntent);
                         }
                     });

                 }else{
                     horizontalLayoutViewAllBtn.setVisibility(View.INVISIBLE);
                 }


                HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                horizontalRecyclerView.setLayoutManager(linearLayoutManager);

                horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
                horizontalProductScrollAdapter.notifyDataSetChanged();


            }

    }

    public class  GridProductViewholder extends RecyclerView.ViewHolder{

        private ConstraintLayout container;
        private TextView gridLayoutTitle;
        private Button gridLayoutViewAllBtn;
        private GridLayout gridProductLayout;

        public GridProductViewholder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
             gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutViewAllBtn = itemView.findViewById(R.id.grid_product_layout_viewall_btn);
            gridProductLayout = itemView.findViewById(R.id.grid_layout);

        }
           @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
           private void setGridProductLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color){
           container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
           gridLayoutTitle.setText(title);

           for(int x = 0;x < 4;x++){

               ImageView productImage = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_image);
               TextView productTitle = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_title);
               TextView productDiscription = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_description);
               TextView productPrice = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_price);

              Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder)).into(productImage);

               productTitle.setText(horizontalProductScrollModelList.get(x).getProductTitle());
               productDiscription.setText(horizontalProductScrollModelList.get(x).getProductDescription());
               productPrice.setText("Ksh."+horizontalProductScrollModelList.get(x).getProductPrice()+"/-");

                 gridProductLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#ffffff"));

                 if(!title.equals("")) {

                     final int finalX = x;
                     gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             Intent productDetailIntent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                             productDetailIntent.putExtra("PRODUCT_ID",horizontalProductScrollModelList.get(finalX).getProductID());
                             itemView.getContext().startActivity(productDetailIntent);
                         }
                     });
                 }
           }

               if(!title.equals("")) {
                   gridLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           ViewAllActivity.horizontalProductScrollModelList = horizontalProductScrollModelList;
                           Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                           viewAllIntent.putExtra("layout_code", 1);
                           viewAllIntent.putExtra("title", title);

                           itemView.getContext().startActivity(viewAllIntent);
                       }
                   });
               }
}

    }


}
