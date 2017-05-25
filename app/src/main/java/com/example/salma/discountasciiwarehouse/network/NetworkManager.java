package com.example.salma.discountasciiwarehouse.network;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.salma.discountasciiwarehouse.beans.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by salma on 23/05/2017.
 */

public class NetworkManager {

    URL urlObj;
    HttpURLConnection httpcon;
    InputStream is=null;
    BufferedReader reader;
    String content;
    ArrayList<Product> products;

    public ArrayList<Product> getAllProductBySearch(int limit,int skip,String q){


                try {
                    Boolean response = false;

                    String myurl = "http://74.50.59.155:5000/api/search?limit="+limit+"&skip="+skip+"&q="+q+"&onlyInStock=true";

                    Log.i("tag", myurl);
                    urlObj = new URL(myurl);
                    httpcon = (HttpURLConnection) urlObj.openConnection();
                  //  httpcon.connect();



                    //   Value of stale indicates how old the cache content can be
                    //   before using it. Here the value is 1 hour(in secs)
                    httpcon.addRequestProperty("Cache-Control", "max-stale=" + 60 * 60);

                    //   Indicating the connection to use caches
                    httpcon.setUseCaches(true);





                    is = httpcon.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {

                        sb.append(line);
                        sb.append("\n");
                    }
                    content = sb.toString();
                    Log.i("abljsonelmfrod",content);
                    String myResponse = "[" + content.replaceAll("\n", ",") + "]";
                    myResponse=myResponse.replace(",]","]");
                    Log.i("jsonelmfrod",myResponse);

                    JSONArray array = new JSONArray(myResponse);
                     products =new ArrayList<Product>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        Product product =new Product();
                        product.setType(row.getString("type"));
                        product.setId(row.getString("id"));
                        product.setSize(Integer.parseInt(row.getString("size")));
                        product.setPrice(Integer.parseInt(row.getString("price")));
                        product.setFace(row.getString("face"));
                        product.setStock(Integer.parseInt(row.getString("stock")));

                        ArrayList<String> productTags = new ArrayList<>();
                        JSONArray myTags = row.optJSONArray("tags");
                        for (int j = 0; j < myTags.length(); j++) {

                            String oneTag = (String) myTags.get(j);
                            productTags.add(oneTag);
                        }
                        product.setTags(productTags);
                        products.add(product);
                        Log.i("json array",row.getString("price"));

                    }






                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



    return products;
    }


    public ArrayList<Product> getAllProductByLimit(int limit,int skip){


        try {
            Boolean response = false;
            String myurl = "http://74.50.59.155:5000/api/search?limit="+limit+"&skip="+skip;

            Log.i("tag", myurl);
            urlObj = new URL(myurl);
            httpcon = (HttpURLConnection) urlObj.openConnection();
           // httpcon.connect();




            //   Value of stale indicates how old the cache content can be
            //   before using it. Here the value is 1 hour(in secs)
            httpcon.addRequestProperty("Cache-Control", "max-stale=" + 60 * 60);

            //   Indicating the connection to use caches
            httpcon.setUseCaches(true);





            is = httpcon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {

                sb.append(line);
                sb.append("\n");
            }
            content = sb.toString();
            Log.i("abljsonelmfrod",content);
            String myResponse = "[" + content.replaceAll("\n", ",") + "]";
            myResponse=myResponse.replace(",]","]");
            Log.i("jsonelmfrod",myResponse);

            JSONArray array = new JSONArray(myResponse);
            products =new ArrayList<Product>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                Product product =new Product();
                product.setType(row.getString("type"));
                product.setId(row.getString("id"));
                product.setSize(Integer.parseInt(row.getString("size")));
                product.setPrice(Integer.parseInt(row.getString("price")));
                product.setFace(row.getString("face"));
                product.setStock(Integer.parseInt(row.getString("stock")));

                ArrayList<String> productTags = new ArrayList<>();
                JSONArray myTags = row.optJSONArray("tags");
                for (int j = 0; j < myTags.length(); j++) {

                    String oneTag = (String) myTags.get(j);
                    productTags.add(oneTag);
                }
                product.setTags(productTags);
                products.add(product);
                Log.i("json array",row.getString("price"));

            }





        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return products;
    }
    }



