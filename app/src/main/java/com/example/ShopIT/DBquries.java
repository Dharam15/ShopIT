package com.example.ShopIT;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ShopIT.Adapter.CategoryAdapter;
import com.example.ShopIT.Adapter.HomePageAdapter;
import com.example.ShopIT.Fragment.HomeFragment;
import com.example.ShopIT.Fragment.MyWishlistFragment;
import com.example.ShopIT.Model.CategoryModel;
import com.example.ShopIT.Model.HomePageModel;
import com.example.ShopIT.Model.HorizontalProductScrollModel;
import com.example.ShopIT.Model.SliderModel;
import com.example.ShopIT.Model.WishlistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBquries {


    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesName = new ArrayList<>();

    public static List<String> wishList = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();

    public static List<String>myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();


    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context){

        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot :task.getResult()){
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(),documentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

     public static void loadFragmentData(final RecyclerView homePageRecyclerView,final Context context, final int index, String categoryName){


         firebaseFirestore.collection("CATEGORIES")
                 .document(categoryName.toUpperCase())
                 .collection("TOP_DEALS").orderBy("index").get()
                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if(task.isSuccessful()){
                             for(QueryDocumentSnapshot documentSnapshot :task.getResult())
                                 if ((long) documentSnapshot.get("view_type") == 0) {

                                     List<SliderModel> sliderModelList = new ArrayList<>();
                                     long no_of_banners = (long) documentSnapshot.get("no_of_banners");

                                     for (long x = 1; x < no_of_banners + 1; x++) {
                                         sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + x).toString()
                                                 , documentSnapshot.get("banner_" + x + "_background").toString()));
                                     }

                                     lists.get(index).add(new HomePageModel(0, sliderModelList));


                                 } else if ((long) documentSnapshot.get("view_type") == 1) {

                                     lists.get(index).add(new HomePageModel(1, documentSnapshot.get("strip_ad_banner").toString(),
                                             documentSnapshot.get("background").toString()));


                                 } else if ((long) documentSnapshot.get("view_type") == 2) {



                                     List<WishlistModel> viewAllProductList = new ArrayList<>();
                                     List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();

                                     long no_of_products = (long) documentSnapshot.get("no_of_products");
                                     for(long x = 1;x < no_of_products + 1; x++) {
                                         horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_"+x).toString()
                                                 ,documentSnapshot.get("product_image_"+x).toString()
                                                 ,documentSnapshot.get("product_title_"+x).toString()
                                                 ,documentSnapshot.get("product_subtitle_"+x).toString()
                                                 ,documentSnapshot.get("product_price_"+x).toString()));

                                               viewAllProductList.add(new WishlistModel(documentSnapshot.get("product_ID_"+x).toString(),documentSnapshot.get("product_image_"+x).toString()
                                                       ,documentSnapshot.get("product_full_title_"+x).toString()
                                                       ,(long)documentSnapshot.get("free_coupens_"+x)
                                                       ,documentSnapshot.get("average_rating_"+x).toString()
                                                       ,(long)documentSnapshot.get("total_ratings_"+x)
                                                       ,documentSnapshot.get("product_price_"+x).toString()
                                                       ,documentSnapshot.get("cutted_price_"+x).toString()
                                                       ,(boolean)documentSnapshot.get("COD_"+x)));
                                     }
                                     lists.get(index).add(new HomePageModel(2,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),horizontalProductScrollModelList,viewAllProductList));




                                 } else if ((long) documentSnapshot.get("view_type") == 3) {

                                     List<HorizontalProductScrollModel> GridLayoutModelList = new ArrayList<>();

                                     long no_of_products = (long) documentSnapshot.get("no_of_product");
                                     for(long x = 1;x < no_of_products + 1; x++) {
                                         GridLayoutModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_"+x).toString()
                                                 ,documentSnapshot.get("product_image_"+x).toString()
                                                 ,documentSnapshot.get("product_title_"+x).toString()
                                                 ,documentSnapshot.get("product_subtitle_"+x).toString()
                                                 ,documentSnapshot.get("product_price_"+x).toString()));
                                     }

                                     lists.get(index).add(new HomePageModel(3,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),GridLayoutModelList));


                                 }
                                 HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                                 homePageRecyclerView.setAdapter(homePageAdapter);
                                 homePageAdapter.notifyDataSetChanged();
                                 HomeFragment.swipeRefreshLayout.setRefreshing(false);

                             }else {
                             String error = task.getException().getMessage();
                             Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                         }
                     }
                 });



     }

     public static void loadWishList(final Context context, final Dialog dialog,final boolean loadProductData){
        wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
         .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                   for(long x = 0;x < (long)task.getResult().get("list_size");x++){
                       wishList.add(task.getResult().get("product_ID_"+x).toString());

                       if(DBquries.wishList.contains(ProductDetailActivity.productID)){
                           ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = true;
                          if(ProductDetailActivity.addToWishListBtn != null) {
                              ProductDetailActivity.addToWishListBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                          }
                       }else {
                           if(ProductDetailActivity.addToWishListBtn != null) {
                               ProductDetailActivity.addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                           }
                          ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = false;
                       }

                       if(loadProductData) {
                           wishlistModelList.clear();
                          final String productId = task.getResult().get("product_ID_" + x).toString();
                           firebaseFirestore.collection("PRODUCTS").document(productId)
                                   .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                   if (task.isSuccessful()) {
                                       wishlistModelList.add(new WishlistModel(productId,task.getResult().get("product_image_1").toString()
                                               , task.getResult().get("product_title").toString()
                                               , (long) task.getResult().get("free_coupens")
                                               , task.getResult().get("average_rating").toString()
                                               , (long) task.getResult().get("total_ratings")
                                               , task.getResult().get("product_price").toString()
                                               , task.getResult().get("cutted_price").toString()
                                               , (boolean) task.getResult().get("COD")));

                                       MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();

                                   } else {
                                       String error = task.getException().getMessage();
                                       Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                                   }
                               }
                           });
                       }

                   }
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

     }

     public static void removeFromWishlist(final int index, final Context context){

        wishList.remove(index);
         Map<String,Object> updateWishlist = new HashMap<>();

         for(int x = 0;x < wishList.size();x++){

             updateWishlist.put("product_ID_"+x,wishList.get(x));
         }
         updateWishlist.put("list_size",(long)wishList.size());

         firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
         .set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful()){

                  if(wishlistModelList.size() != 0){
                      wishlistModelList.remove(index);
                      MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                  }
                  ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = false;
                  Toast.makeText(context,"Removed successfully!",Toast.LENGTH_SHORT).show();
              }else {
                  if(ProductDetailActivity.addToWishListBtn != null) {
                      ProductDetailActivity.addToWishListBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                  }
                 String error = task.getException().getMessage();
                  Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
              }
                 ProductDetailActivity.running_wishlist_query = false;
             }

         });
     }

      public static void loadRatingList(final Context context){

        myRatedIds.clear();
        myRating.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
               .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful()){
                   for(long x = 0;x < (long)task.getResult().get("list_size");x++){
                       myRatedIds.add(task.getResult().get("product_ID_"+x).toString());
                       myRating.add((long)task.getResult().get("rating_"+x));

                       if(task.getResult().get("product_ID_"+x).toString().equals(ProductDetailActivity.productID) && ProductDetailActivity.rateNowContainer != null){
                           ProductDetailActivity.initialRating = Integer.parseInt(String.valueOf((long)task.getResult().get("rating_"+x)))-1;
                           ProductDetailActivity.setRating(ProductDetailActivity.initialRating);
                       }
                   }
               }else {
                   String error = task.getException().getMessage();
                   Toast.makeText(context,error,Toast.LENGTH_SHORT).show();

               }
            }
        });


      }



     public static void clearData(){
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesName.clear();
        wishList.clear();
        wishlistModelList.clear();


     }


}
