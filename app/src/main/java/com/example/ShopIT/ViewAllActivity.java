package com.example.ShopIT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.ShopIT.Adapter.GridProductLayoutAdapter;
import com.example.ShopIT.Adapter.WishlistAdapter;
import com.example.ShopIT.Model.HorizontalProductScrollModel;
import com.example.ShopIT.Model.WishlistModel;

import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;
    public static List<WishlistModel> wishlistModelList;
    public static List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

          recyclerView = findViewById(R.id.recycler_view);
          gridView = findViewById(R.id.grid_view);

         int layout_code = getIntent().getIntExtra("layout_code",-1);

         if(layout_code == 0) {
             recyclerView.setVisibility(View.VISIBLE);
             LinearLayoutManager layoutManager = new LinearLayoutManager(this);
             layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
             recyclerView.setLayoutManager(layoutManager);



             WishlistAdapter adapter = new WishlistAdapter(wishlistModelList, false);
             recyclerView.setAdapter(adapter);
             adapter.notifyDataSetChanged();
         }
         else if(layout_code == 1) {

             gridView.setVisibility(View.VISIBLE);

             GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
             gridView.setAdapter(gridProductLayoutAdapter);
         }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
