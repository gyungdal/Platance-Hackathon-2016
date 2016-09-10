package com.company;

import com.company.locate.GetDistance;
import com.company.locate.Item;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
// 3개 : 2.68초
// 8개 : 100초...

//구조 변경 :
// 3개 : 2.133초
// 8개 : 59.603초

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        ArrayList<String> test = new ArrayList<>();
        test.add("서울 강남구 논현동 122-8");

        test.add("서울 서초구 잠원동 63-2");
        test.add("서울 서초구 양재동 7-26");
        test.add("서울 강남구 신사동 628-36");
        test.add("서울 강남구 도곡동 538");
        test.add("서울 서초구 양재동 344-3");
        test.add("서울 강남구 대치동 511 한보미도맨션2차");
        test.add("서울 강남구 삼성동 158-12");
        test.add("서울특별시 강남구 도산대로 176");
        test.add("서울 강남구 삼성동 161-15");
        test.add("서울 서초구 잠원동 58-16 신반포 10차 316-1204");

        test.add("서울 강남구 논현동 122-8");
        int beforeDistance = 0, afterDistance = 0, temp = 0, temp2 = 0, count = 0;
        for(int i = 0;i<test.size()-1;i++)
            beforeDistance += getValue(test.get(i), test.get(i+1));
        ArrayList<String> clone, result;
        clone = (ArrayList)test.clone();
        result = (ArrayList)test.clone();
        temp = beforeDistance;
        for (int i = 1; i < clone.size() - 1; i++) {
            boolean isBig;
            for (int j = 1; j < clone.size() - 1; j++) {
                isBig = false;
                temp2 = 0;
                String TEMP = clone.get(i);
                clone.set(i, clone.get(j));
                clone.set(j, TEMP);
                for (int k = 0; k < clone.size() - 1; k++) {
                    long t =  getValue(clone.get(k), clone.get(k + 1));
                    count++;
                    if(temp * 0.3 < t || (temp2 > temp)){
                        isBig = true;
                        temp2 = 0;
                        break;
                    }
                    temp2 += t;
                }
                if(isBig)
                    continue;
                //System.out.println("TIME : " + temp2);
                if (temp > temp2) {
                    result = (ArrayList) clone.clone();
                    temp = temp2;
                }
            }
        }

        afterDistance = temp;
        long endTime = System.currentTimeMillis();
        //System.out.println("전체 크기 : " + test.size() + ", 요청 횟수 : " + count);
       // System.out.println("##  소요시간(초.0f) : " + ( endTime - startTime )/1000.0f +"초");
        //System.out.println("ALL DISTANCE : " + beforeDistance);
        //System.out.println("AFTER DISTANCE : " + afterDistance);
        for(int i = 0;i<result.size();i++) {
            System.out.print((i != result.size() - 1 ? test.indexOf(result.get(i)) : test.size() -1)  + " ");
            //System.out.println(results);
        }

       // System.out.println(g.getShortestPath(test.get(0), "서울 송파구 잠실본동 320"));
    }
    private static Item getLocate(String name){
        return GetDistance.hash.get(name);
    }
    private static long getValue(String start, String end){
        GetDistance getDistance = new GetDistance(start, end);
        getDistance.start();
        while(getDistance.getResult() == -1);
        return getDistance.getResult();
    }
}
