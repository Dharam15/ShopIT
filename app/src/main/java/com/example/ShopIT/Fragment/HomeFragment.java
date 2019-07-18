package com.example.ShopIT.Fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ShopIT.Adapter.CategoryAdapter;
import com.example.ShopIT.Adapter.HomePageAdapter;
import com.example.ShopIT.MainActivity;
import com.example.ShopIT.Model.CategoryModel;
import com.example.ShopIT.Model.HomePageModel;
import com.example.ShopIT.Model.HorizontalProductScrollModel;
import com.example.ShopIT.Model.SliderModel;
import com.example.ShopIT.Model.WishlistModel;
import com.example.ShopIT.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.ShopIT.DBquries.categoryModelList;
import static com.example.ShopIT.DBquries.lists;
import static com.example.ShopIT.DBquries.loadCategories;
import static com.example.ShopIT.DBquries.loadFragmentData;
import static com.example.ShopIT.DBquries.loadedCategoriesName;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private ConnectivityManager connectivityManager;
    private  NetworkInfo networkInfo;

    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private List<CategoryModel>categoryModeFakelList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private List<HomePageModel>homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;
    private ImageView noInternetConnection;
    private Button retryBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        noInternetConnection = view.findViewById(R.id.no_imternet_connection);
        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
        retryBtn = view.findViewById(R.id.retry_btn);


        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);


        ////////categories fake list
        categoryModeFakelList.add(new CategoryModel("null",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));

        ////////categories fake list

         /////home page fake list
        List<SliderModel>sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));

        List<HorizontalProductScrollModel>horizontalProductScrollModelFakeList = new ArrayList<>();
         horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#dfdfdf"));
        homePageModelFakeList.add(new HomePageModel(2,"","#dfdfdf",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#dfdfdf",horizontalProductScrollModelFakeList));

        /////home page fake list

        categoryAdapter = new CategoryAdapter(categoryModeFakelList);
        adapter = new HomePageAdapter(homePageModelFakeList);


        connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
         networkInfo = connectivityManager.getActiveNetworkInfo();


        if(networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(0);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            if(categoryModelList.size() == 0){
                loadCategories(categoryRecyclerView,getContext());
            }else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);

            if(lists.size() == 0){
                loadedCategoriesName.add("HOME");
                lists.add(new ArrayList<HomePageModel>());

                loadFragmentData(homePageRecyclerView,getContext(),0,"Home");
            }else {
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }

            homePageRecyclerView.setAdapter(adapter);

        }else{
            MainActivity.drawer.setDrawerLockMode(1);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.no_internate).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);

        }
        ////////////refresh layout

       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
            public void onRefresh() {
              swipeRefreshLayout.setRefreshing(true);
              reloadPage();
                 }
           });

        ////////////refresh layout


        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               reloadPage();
            }
        });

        return view;
    }


    private void reloadPage(){

        networkInfo = connectivityManager.getActiveNetworkInfo();
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesName.clear();
        if(networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(0);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);
            categoryAdapter = new CategoryAdapter(categoryModeFakelList);
            adapter = new HomePageAdapter(homePageModelFakeList);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homePageRecyclerView.setAdapter(adapter);

            loadCategories(categoryRecyclerView,getContext());

            loadedCategoriesName.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homePageRecyclerView,getContext(),0,"Home");



        }else{
            MainActivity.drawer.setDrawerLockMode(1);
            Toast.makeText(getContext(),"No Internet Connection!",Toast.LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.no_internate).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }

    }


}
