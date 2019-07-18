package com.example.ShopIT;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ShopIT.Adapter.MyRewardAdapter;
import com.example.ShopIT.Adapter.ProductDetailsAdapter;
import com.example.ShopIT.Adapter.ProductImagesAdapter;
import com.example.ShopIT.Fragment.SignInFragment;
import com.example.ShopIT.Fragment.SignUpFragment;
import com.example.ShopIT.Model.ProductSpecificationnModel;
import com.example.ShopIT.Model.RewardModel;
import com.example.ShopIT.Model.WishlistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ShopIT.MainActivity.showCart;
import static com.example.ShopIT.RegisterActivity.setSignUpFragment;

public class ProductDetailActivity extends AppCompatActivity {

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;

    private ViewPager productImagesViewPager;
    private TabLayout viewpagerIndicator;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
     private  ImageView codIndicator;
     private TextView tvCodIndicator;
    private  TextView rewardTitle;
    private  TextView rewardBody;

    private LinearLayout coupenRedemptionLayout;
    private Button coupenRedeemBtn;

    public static FloatingActionButton addToWishListBtn;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;

    //////// Product description
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;
    private List<ProductSpecificationnModel>productSpecificationnModelList = new ArrayList<>();

    private TextView productOnlyDescriptionBody;
    private String productDescription;
    private String productOtherDetails;

    //////// Product description


    //////rating layout
    public static int initialRating;
    private TextView totalRatings;
    public static LinearLayout rateNowContainer;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    //////rating layout

    private Button buyNowBtn;
    private  LinearLayout addToCartBtn;

    private FirebaseFirestore firebaseFirestore;

    /////////coupendialog

    public static TextView coupenTitle;
    public static TextView coupenExpiryDate;
    public static TextView coupenBody;
    private static RecyclerView coupensRecyclerView;
    private static LinearLayout selectedCoupen;

    /////////coupendialog

    private Dialog signInDialog;
    private Dialog loadingDialog;
    private FirebaseUser currentUser;

    public static String productID;

    private DocumentSnapshot documentSnapshot;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         productImagesViewPager = findViewById(R.id.product_images_viewpager);
         viewpagerIndicator = findViewById(R.id.viewpager_indicator);
         addToWishListBtn = findViewById(R.id.add_to_wishlist_btn);
         productDetailsViewPager = findViewById(R.id.product_detail_viewpager);
         productDetailsTabLayout = findViewById(R.id.product_detail_tablayout);
         buyNowBtn = findViewById(R.id.buy_now_btn);
         coupenRedeemBtn = findViewById(R.id.coupen_redemption_btn);
         productTitle = findViewById(R.id.product_title);
          averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
           totalRatingMiniView = findViewById(R.id.total_ratings_miniview);
          productPrice = findViewById(R.id.product_price);
          cuttedPrice = findViewById(R.id.cutted_price);
          tvCodIndicator = findViewById(R.id.tv_cod_indicator);
          codIndicator = findViewById(R.id.cod_indicator_imageview);
         rewardTitle = findViewById(R.id.reward_title);
         rewardBody = findViewById(R.id.reward_body);
         productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
         productDetailsOnlyContainer = findViewById(R.id.product_details_container);
         productOnlyDescriptionBody = findViewById(R.id.product_details_body);
         totalRatings = findViewById(R.id.total_ratings);
          ratingsNoContainer = findViewById(R.id.ratings_number_container);
          totalRatingsFigure = findViewById(R.id.total_ratings_figure);
          ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
          averageRating = findViewById(R.id.average_rating);
          addToCartBtn = findViewById(R.id.add_to_cart_btn);
        coupenRedemptionLayout = findViewById(R.id.coupen_redemption_layout);

        initialRating = -1;

         ///////loading dialog
        loadingDialog = new Dialog(ProductDetailActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
         loadingDialog.show();
         ////////loading dialog

         firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImages = new ArrayList<>();
         productID = getIntent().getStringExtra("PRODUCT_ID");
         firebaseFirestore.collection("PRODUCTS").document(productID)
                 .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                 if(task.isSuccessful()){
                     documentSnapshot = task.getResult();

                     for(long x = 1;x <(long)documentSnapshot.get("no_of_product_images") + 1;x++){
                       productImages.add(documentSnapshot.get("product_image_"+x).toString());
                     }
                     ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                     productImagesViewPager.setAdapter(productImagesAdapter);

                     productTitle.setText(documentSnapshot.get("product_title").toString());

                     averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                     totalRatingMiniView.setText("("+(long)documentSnapshot.get("total_ratings")+")ratings");
                     productPrice.setText("Rs."+documentSnapshot.get("product_price").toString()+"/-");

                     cuttedPrice.setText("Rs."+documentSnapshot.get("cutted_price").toString()+"/-");
                     if((boolean)documentSnapshot.get("COD")){
                         codIndicator.setVisibility(View.VISIBLE);
                         tvCodIndicator.setVisibility(View.VISIBLE);
                     }else {
                         codIndicator.setVisibility(View.INVISIBLE);
                         tvCodIndicator.setVisibility(View.INVISIBLE);
                     }
                     rewardTitle.setText((long)documentSnapshot.get("free_coupens") + documentSnapshot.get("free_coupen_title").toString());
                      rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());

                      if((boolean)documentSnapshot.get("use_tab_layout")){
                           productDetailsTabsContainer.setVisibility(View.VISIBLE);
                           productDetailsOnlyContainer.setVisibility(View.GONE);
                         productDescription  = documentSnapshot.get("product_description").toString();

                          productOtherDetails = documentSnapshot.get("product_other_details").toString();


                           for(long x = 1;x < (long)documentSnapshot.get("total_spec_titles")+1;x++){
                               productSpecificationnModelList.add(new ProductSpecificationnModel(0,documentSnapshot.get("spec_title_"+x).toString()));

                               for(long y = 1;y < (long)documentSnapshot.get("spec_title_"+x+"_total_fields")+1;y++){
                                   productSpecificationnModelList.add(new ProductSpecificationnModel(1,documentSnapshot.get("spec_title_"+x+"_field_"+y+"_name").toString(),documentSnapshot.get("spec_title_"+x+"_field_"+y+"_value").toString()));
                           }

                           }

                      }else {

                          productDetailsTabsContainer.setVisibility(View.GONE);
                          productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                          productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                      }

                      totalRatings.setText((long)documentSnapshot.get("total_ratings")+"ratings");
                         for(int x = 0;x < 5; x++){
                            TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                            rating.setText(String.valueOf((long)documentSnapshot.get(5-x+"_star")));

                             ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                             int maxProgress = Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_ratings")));
                             progressBar.setMax(maxProgress);
                             progressBar.setProgress(Integer.parseInt(String.valueOf((long)documentSnapshot.get((5-x)+"_star"))));

                         }
                          totalRatingsFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")));
                          averageRating.setText(documentSnapshot.get("average_rating").toString());
                          productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTabLayout.getTabCount(),productDescription,productOtherDetails,productSpecificationnModelList));

                          if(currentUser != null) {

                              if(DBquries.myRating.size() == 0){
                                  DBquries.loadRatingList(ProductDetailActivity.this);
                              }
                              if (DBquries.wishList.size() == 0) {
                                  DBquries.loadWishList(ProductDetailActivity.this, loadingDialog,false);
                              } else {
                                  loadingDialog.dismiss();
                              }
                          }else {
                              loadingDialog.dismiss();
                          }

                     if(DBquries.myRatedIds.contains(productID)){
                         int index = DBquries.myRatedIds.indexOf(productID);
                         initialRating = Integer.parseInt(String.valueOf(DBquries.myRating.get(index))) - 1;
                         setRating(initialRating);
                     }


                     if(DBquries.wishList.contains(productID)){
                         ALREADY_ADDED_TO_WISHLIST = true;
                         addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));

                     }else {
                         addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                         ALREADY_ADDED_TO_WISHLIST = false;
                     }

                 }else{
                     loadingDialog.dismiss();
                     String error = task.getException().getMessage();
                     Toast.makeText(ProductDetailActivity.this,error,Toast.LENGTH_SHORT).show();

                 }

             }
         });

           viewpagerIndicator.setupWithViewPager(productImagesViewPager,true);

           addToWishListBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(currentUser == null){
                       signInDialog.show();
                   }
                   else {
                       //addToWishListBtn.setEnabled(false);
                           if(!running_wishlist_query) {
                               running_wishlist_query = true;
                               if (ALREADY_ADDED_TO_WISHLIST) {
                                   int index = DBquries.wishList.indexOf(productID);
                                   DBquries.removeFromWishlist(index, ProductDetailActivity.this);
                                   addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

                               } else {
                                   addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                   Map<String, Object> addProduct = new HashMap<>();
                                   addProduct.put("product_ID_" + String.valueOf(DBquries.wishList.size()), productID);

                                   firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                           .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()) {

                                               Map<String, Object> updateListSize = new HashMap<>();
                                               updateListSize.put("list_size", (long) (DBquries.wishList.size() + 1));

                                               firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                                       .update(updateListSize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                       if (task.isSuccessful()) {

                                                           if (DBquries.wishlistModelList.size() != 0) {
                                                               DBquries.wishlistModelList.add(new WishlistModel(productID, documentSnapshot.get("product_image_1").toString()
                                                                       , documentSnapshot.get("product_title").toString()
                                                                       , (long) documentSnapshot.get("free_coupens")
                                                                       , documentSnapshot.get("average_rating").toString()
                                                                       , (long) documentSnapshot.get("total_ratings")
                                                                       , documentSnapshot.get("product_price").toString()
                                                                       , documentSnapshot.get("cutted_price").toString()
                                                                       , (boolean) documentSnapshot.get("COD")));

                                                           }


                                                           ALREADY_ADDED_TO_WISHLIST = true;
                                                           addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                                           DBquries.wishList.add(productID);
                                                           Toast.makeText(ProductDetailActivity.this, "Added to Wishlist successfully!", Toast.LENGTH_SHORT).show();
                                                       } else {
                                                           addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                                           String error = task.getException().getMessage();
                                                           Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();

                                                       }
                                                      // addToWishListBtn.setEnabled(true);
                                                        running_wishlist_query = false;
                                                   }

                                               });

                                           } else {
                                              // addToWishListBtn.setEnabled(true);
                                               running_wishlist_query = false;
                                               String error = task.getException().getMessage();
                                               Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();

                                           }

                                       }
                                   });


                               }
                           }
                       }
                   }

           });


productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));

productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        productDetailsViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
});

       /////////rating layout

       rateNowContainer = findViewById(R.id.rate_now_container);

        for(int x = 0;x < rateNowContainer.getChildCount();x++){
            final int starposition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        if(!running_rating_query) {
                            running_rating_query = true;

                            setRating(starposition);
                            if (DBquries.myRatedIds.contains(productID)) {

                            } else {

                                Map<String, Object> productRating = new HashMap<>();
                                productRating.put(starposition+1+"_star", (long)documentSnapshot.get(starposition+1+"_star")+1);
                                productRating.put("average_rating",calculateAverageRating(starposition+1));
                                productRating.put("total_ratings",(long)documentSnapshot.get("total_ratings")+1);

                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(productRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Map<String, Object> rating = new HashMap<>();
                                            rating.put("list_size",(long)DBquries.myRatedIds.size()+1);
                                            rating.put("product_ID_"+DBquries.myRatedIds.size(),productID);
                                            rating.put("rating_"+DBquries.myRatedIds.size(),(long)starposition+1);

                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        DBquries.myRatedIds.add(productID);
                                                        DBquries.myRating.add((long)starposition+1);

                                                        TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starposition - 1);
                                                        rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));


                                                        totalRatingMiniView.setText("(" + ((long)documentSnapshot.get("total_ratings")+1)+ ")ratings");
                                                        totalRatings.setText((long)documentSnapshot.get("total_ratings")+ 1 + " ratings");
                                                        totalRatingsFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")+1));

                                                         averageRating.setText(String.valueOf(calculateAverageRating(starposition + 1)));
                                                        averageRatingMiniView.setText(String.valueOf(calculateAverageRating(starposition + 1)));

                                                        for(int x = 0;x < 5; x++){
                                                            TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);

                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                            int maxProgress = Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_ratings")+1));
                                                            progressBar.setMax(maxProgress);
                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));

                                                        }

                                                        Toast.makeText(ProductDetailActivity.this, "Thank you ! for rating", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;
                                                }
                                            });

                                        } else {
                                            running_rating_query = false;
                                            setRating(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


                            }
                        }
                    }
                }
            });
        }

        //////rating layout

       buyNowBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (currentUser == null) {
                   signInDialog.show();
               } else {
                   Intent deliveryIntent = new Intent(ProductDetailActivity.this, DeliveryActivity.class);
                   startActivity(deliveryIntent);
               }
           }
       });


        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    ///todo: add to cart
                }
            }
        });

        /////////// coupen dialog
        final Dialog checkCoupenPriceDialog = new Dialog(ProductDetailActivity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
        coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
        selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);

        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
        coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_validity);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);



        TextView originalPrice = coupenRedeemBtn.findViewById(R.id.original_price);
        TextView discountedPrice = coupenRedeemBtn.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupensRecyclerView.setLayoutManager(layoutManager);


        List<RewardModel>rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback","till 2nd,jan 2019","GET 10% OFF on any product above Ksh.500/- and below Ksh.3500/-"));
        rewardModelList.add(new RewardModel("Discount","till 2nd,mar 2019","GET 30% OFF on any product above Ksh.500/- and below Ksh.4500/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 free","june 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.3500/-"));
        rewardModelList.add(new RewardModel("Cashback","till 2nd,aug 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.1500/-"));
        rewardModelList.add(new RewardModel("Discount","till 2nd,oct 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.4500/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 free","nov 2nd,jun 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.6500/-"));
        rewardModelList.add(new RewardModel("Cashback","till 2nd,apr 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.9500/-"));
        rewardModelList.add(new RewardModel("Discount","till 2nd,dec 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.4500/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 free","till 2nd,may 2019","GET 20% OFF on any product above Ksh.500/- and below Ksh.7500/-"));


        MyRewardAdapter myRewardAdapter = new MyRewardAdapter(rewardModelList,true);
        coupensRecyclerView.setAdapter(myRewardAdapter);
        myRewardAdapter.notifyDataSetChanged();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });


        ////////////coupen dialog

        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 checkCoupenPriceDialog.show();
            }
        });

          //////// sign dialog


        signInDialog = new Dialog(ProductDetailActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);

        final Intent registerIntent = new Intent(ProductDetailActivity.this,RegisterActivity.class);


        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });

        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);

            }
        });

        //////// sign dialog


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
         currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            coupenRedemptionLayout.setVisibility(View.GONE);
        }else {
            coupenRedemptionLayout.setVisibility(View.VISIBLE);
        }

        if(currentUser != null) {
            if (DBquries.wishList.size() == 0) {
                DBquries.loadWishList(ProductDetailActivity.this, loadingDialog,false);
            } else {
                loadingDialog.dismiss();
            }
            if(DBquries.myRating.size() == 0){
                DBquries.loadRatingList(ProductDetailActivity.this);
            }
        }else {
            loadingDialog.dismiss();
        }

        if(DBquries.myRatedIds.contains(productID)){
            int index = DBquries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBquries.myRating.get(index))) - 1;
            setRating(initialRating);
        }

        if(DBquries.wishList.contains(productID)){
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));

        }else {
            addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
    }

    public static void showDialogRecyclerView(){

        if(coupensRecyclerView.getVisibility() == View.GONE){
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);

        }else {
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setRating(int starposition) {

            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {

                ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);

                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if (x <= starposition) {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));

                }
            }
        }

    private long calculateAverageRating(long currentUserRating){
       long totalStars = 0;
       for(int x = 1;x < 6; x++){
          totalStars = totalStars + ((long)documentSnapshot.get(x + "_star")*x);
       }

       totalStars = totalStars + currentUserRating;
       return totalStars / ((long)documentSnapshot.get("total_ratings") + 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        else if(id == R.id.search){
            return true;

        }

        else if (id == R.id.cart) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent cartIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


}
