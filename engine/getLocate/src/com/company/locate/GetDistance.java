package com.company.locate;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.rmi.runtime.Log;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by GyungDal on 2016-09-10.
 */
public class GetDistance extends Thread {
    private static final String GET_DISTANCE_REQUEST = "http://map.naver.com/spirra/findCarRoute.nhn?route=route3&output=json&coord_type=naver&search=0&car=0&mileage=12.4&";
    private static final String GET_LOCATE_REQUEST_FORMAT = "https://maps.google.com/maps/api/geocode/json?address=%s&sensor=false&key=%s";
    private static final String API_KEY = "AIzaSyBbE-0753fKTRgWXG4vwWJKomWEnHdCn-w";
    //private static final String GET_LOCATE_REQUEST_FORMAT = "https://apis.daum.net/local/geo/addr2coord?apikey=952d7bd3b757ddb384711627fbd29538&q=%s&output=json&appid=com.codezero.fireprevention";
    private static HashMap<String, Item> hash;
    static {
        hash = new HashMap<>();
    }
    private String sp;
    private String ep;
    private long result = -999;
    public GetDistance(String sp, String ep){
        this.sp = sp;
        this.ep = ep;
    }

    public synchronized long getResult(){
        if(result != -999)
            return this.result;
        return -1;
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
            System.out.println("Done");
            result = Long.valueOf(((JSONObject)jsonObject.get("summary")).get("totalDistance").toString());

        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    private String get(String name) throws IOException {
        System.out.println(String.format(GET_LOCATE_REQUEST_FORMAT, name, API_KEY).replaceAll(" ", "%20"));
        URL url = new URL(String.format(GET_LOCATE_REQUEST_FORMAT, name, API_KEY).replaceAll(" ", "%20"));
        url.openConnection();
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
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
        conn.disconnect();
        return temp;
    }
    private Item getLocate(String name) throws Exception{
        if(hash.get(name) != null){
            return hash.get(name);
        }
        String temp = get(name);
        System.out.println(temp.toString());
        while(temp.contains("OVER_QUERY_LIMIT")){
            this.sleep(100);
            temp = get(name);
        }
        Item item = getPoint(temp);
        hash.put(name, item);
        System.out.println(temp);
        System.out.println(item.lng);
        System.out.println(item.lat);
        return item;
    }
    private Item getPoint(String temp) throws InterruptedException {
        temp = temp.substring(temp.indexOf("\"lat\" :"), temp.length());
        temp = temp.substring(0, temp.indexOf("}")).trim();
        return new Item(Double.valueOf(temp.substring(temp.indexOf("\"lat\" : ") + "\"lat\" : ".length(), temp.indexOf(',')))
                , Double.valueOf(temp.substring(temp.indexOf("\"lng\" : ") + "\"lat\" : ".length(), temp.length())));
    }
}
