package com.example.salma.discountasciiwarehouse;

import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import com.example.salma.discountasciiwarehouse.Adapter.GridAdapter;
import com.example.salma.discountasciiwarehouse.beans.Product;
import com.example.salma.discountasciiwarehouse.listener.GridViewScrollListener;
import com.example.salma.discountasciiwarehouse.network.NetworkManager;


import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Button btn;
    EditText searchText;
    ArrayList<Product> products;
    NetworkManager networkManager;
    Handler handler;
    GridView gridView;
    String searchWord = "";
    GridAdapter gridAdapter = null;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView =(GridView) findViewById(R.id.gridView);

        networkManager= new NetworkManager();
        btn=(Button) findViewById(R.id.button);
        searchText = (EditText) findViewById(R.id.editText);
        spinner = (ProgressBar)findViewById(R.id.progressBar);


        // Get the Default External Cache Directory
        File httpCacheDir = getExternalCacheDir();

        // Cache Size of 5MB
        long httpCacheSize = 5 * 1024 * 1024;

        try {
            // Install the custom Cache Implementation
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }



        // This enables your retreival from the cache when working offline

        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if(cache != null) {
            //   If cache is present, flush it to the filesystem.
            //   Will be used when activity starts again.
            cache.flush();
        }





        // action on Search button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWord = String.valueOf(searchText.getText());
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();
                        Bundle bundle = new Bundle();

                        ArrayList<Product> searchReturn = networkManager.getAllProductBySearch(4,0,searchWord);

                        Log.i("before arrayyyyyyyyyyyy", String.valueOf(searchReturn.size()));

                        if (searchReturn.size()==0){    ///no result

                            bundle.putString("status", String.valueOf(false));
                        }
                       else {
                            // result for first time
                            bundle.putString("status", "first");
                            products.clear();
                            products = searchReturn;
                        }


                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }};
                Thread th = new Thread(r);
                th.start();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                //disable progress bar
                spinner.setVisibility(View.GONE);
                Bundle b = msg.getData();

                String check = b.getString("status");

                if (check.equals("first")) {
                    // set adapter for first time
                    gridAdapter = new GridAdapter(getApplicationContext(), products);
                    gridView.setAdapter(gridAdapter);
                    Log.i("in first handler","ya mosahel");
                }else{
                    if (check.equals("true")) {
                        // having data
                        gridAdapter.notifyDataSetChanged();
                    }
                    else {
                        // no products found
                        Toast.makeText(getApplicationContext(),"No Products Found",Toast.LENGTH_LONG).show();
                        products.clear();

                    }
                }

            }
        };
        Runnable r = new Runnable() {
            @Override
            public void run() {

                products=networkManager.getAllProductByLimit(4,0);

                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();

                bundle.putString("status", "first");

                msg.setData(bundle);
                handler.sendMessage(msg);
            }};
        Thread th = new Thread(r);
        th.start();



        gridView.setOnScrollListener(new GridViewScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                // set progress bar visible
                spinner.setVisibility(View.VISIBLE);
                Log.i("page print", String.valueOf(page));
                Log.i("totalItemsCount print", String.valueOf(totalItemsCount));
                int limit = 4;
                int skip= totalItemsCount-1;
                loadNextData(limit,skip);

                return true;
            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }
        });



    }

    private void loadNextData(final int limit, final int skip) {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                ArrayList<Product> check=networkManager.getAllProductBySearch(limit,skip,searchWord);
                products.addAll(networkManager.getAllProductBySearch(limit,skip,searchWord));
                // check if no more products
                if (check.size()==0){
                    gridAdapter.flag=true;
                }
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();

                bundle.putString("status", String.valueOf(true));
                msg.setData(bundle);
                handler.sendMessage(msg);
            }};
        Thread th = new Thread(r);
        th.start();
    }
}