package com.example.salma.discountasciiwarehouse.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.salma.discountasciiwarehouse.R;
import com.example.salma.discountasciiwarehouse.beans.Product;

import java.util.ArrayList;

/**
 * Created by salma on 23/05/2017.
 */

public class GridAdapter extends BaseAdapter {

    ArrayList<Product> products ;
    Context context;
    LayoutInflater layoutInflater;
    TextView buy;
    public Boolean flag=false;

    public GridAdapter(Context context,ArrayList<Product> products){
        this.context=context;
        this.products=products;
    }

    @Override
    public int getCount() {
        //products list less than limit
        if (products.size()<4){
            return products.size();
        }else {
            // no more products
            if (flag==true){
                return products.size();
            }else {
                // still have products so add loading cell
                return products.size()+1;
            }
        }


    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View gridView = view;
        if (view==null){
            layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            gridView = layoutInflater.inflate(R.layout.grid_item,null);
        }

        TextView face = (TextView)gridView.findViewById(R.id.textViewFace);
        TextView price = (TextView) gridView.findViewById(R.id.textViewPrice);
        buy = (TextView) gridView.findViewById(R.id.textViewBuy);

        // if loading cell
        if (i==products.size()){
            face.setText("loading..");
            price.setText("");
            buy.setText("");

        }else {
            /////set the cell with data
            face.setText(products.get(i).getFace());
            price.setText("$"+String.valueOf(products.get(i).getPrice()));
            // only one in stock
            if (products.get(i).getStock()==1){
                buy.setText(Html.fromHtml("BUY NOW!<br/><small>(Only 1 more in stock!)</small>"));
            }else {
                buy.setText("BUY NOW!");
            }
        }





        //make hover on the buyTextView
        buy.setOnHoverListener(new View.OnHoverListener() {

            @Override
            public boolean onHover(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_HOVER_ENTER:
                        buy.setBackgroundColor(Color.RED);

                        break;
                    case MotionEvent.ACTION_HOVER_MOVE:
                        buy.setBackgroundColor(Color.RED);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        buy.setBackgroundColor(Color.RED);
                        break;
                }
                    return false;

            }

    });
        return gridView;
}

}
