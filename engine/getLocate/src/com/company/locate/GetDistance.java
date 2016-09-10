package com.company.locate;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by GyungDal on 2016-09-10.
 */
public class GetDistance extends Thread {
    private static final String GET_DISTANCE_REQUEST = "http://map.naver.com/spirra/findCarRoute.nhn?route=route3&output=json&coord_type=naver&search=0&car=0&mileage=12.4&";
    private static final String GET_LOCATE_REQUEST_FORMAT = "http://maps.google.com/maps/api/geocode/json?address=%s&sensor=false";
    private String sp;
    private String ep;

    public GetDistance(String sp, String ep){
        this.sp = sp;
        this.ep = ep;
    }

    @Override
    public void run(){
        try {
            Item spLocate = getLocate(sp);
            Item epLocate = getLocate(ep);
            String requesetURL = GET_DISTANCE_REQUEST + String.format("start=%f,%f,%s&destination=%f,%f,%s&via="
                    , spLocate.lat, spLocate.lng, sp, epLocate.lat, epLocate.lng, ep).replaceAll(",","%2C").replaceAll(" ", "%20");
            System.out.println(requesetURL);
            URL url = new URL(requesetURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine = null, temp = "";

            while((inputLine = in.readLine()) != null){
                temp += inputLine;
            }
            in.close();
            System.out.println(temp.toString());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(temp);
            System.out.println(((JSONObject)jsonObject.get("summary")).get("totalDistance"));
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    private Item getLocate(String name) throws Exception{
        System.out.println(String.format(GET_LOCATE_REQUEST_FORMAT, name).replaceAll(" ", "%20"));
        URL url = new URL(String.format(GET_LOCATE_REQUEST_FORMAT, name).replaceAll(" ", "%20"));
        url.openConnection();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine = null, temp = "";

        while((inputLine = in.readLine()) != null){
            temp += inputLine;
        }
        in.close();
        System.out.println(temp.toString());
        Item item = getPoint(temp);
        System.out.println(temp);
        System.out.println(item.lng);
        System.out.println(item.lat);
        return item;
    }
    private Item getPoint(String temp){
        temp = temp.substring(temp.indexOf("\"lat\" :"), temp.length());
        temp = temp.substring(0, temp.indexOf("}")).trim();
        return new Item(Double.valueOf(temp.substring(temp.indexOf("\"lat\" : ") + "\"lat\" : ".length(), temp.indexOf(',')))
            , Double.valueOf(temp.substring(temp.indexOf("\"lng\" : ") + "\"lat\" : ".length(), temp.length())));
    }
}
