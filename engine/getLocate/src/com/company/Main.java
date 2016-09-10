package com.company;

import com.company.locate.GetDistance;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> test = new ArrayList<>();
        test.add("서울 강남구 논현동 122-8");
        test.add("서울 서초구 잠원동 58-16 신반포 10차 316-1204");
        test.add("서울특별시 강남구 도산대로 176");
        test.add("서울 송파구 잠실동 19");
        test.add("서울 송파구 잠실본동 320");
        test.add("서울 강남구 대치동 511 한보미도맨션2차");
        test.add("서울 강남구 도곡동 538");
        test.add("서울 강남구 논현동 122-8");
        int beforeDistance = 0, afterDistance = 0, temp = 0, temp2 = 0;
        for(int i = 0;i<test.size()-1;i++)
            beforeDistance += getValue(test.get(i), test.get(i+1));
        ArrayList<String> clone, result;
        clone = (ArrayList)test.clone();
        result = (ArrayList)test.clone();
        temp = beforeDistance;
        for(int i = 1;i<clone.size() -1 ;i++){
            for(int j = 1;j<clone.size() -1 ;j++){
                temp2 = 0;
                String TEMP = clone.get(i);
                clone.set(i,clone.get(j));
                clone.set(j,TEMP);
                for(int k = 0;k<clone.size() - 1;k++)
                    temp2 += getValue(clone.get(k), clone.get(k + 1));

                System.out.println("TIME : " + temp2);
                if(temp > temp2) {
                    result = (ArrayList) clone.clone();
                    temp = temp2;
                }
            }
        }
        afterDistance = temp;
        System.out.println("ALL DISTANCE : " + beforeDistance);
        System.out.println("AFTER DISTANCE : " + afterDistance);
        for(String results : result)
            System.out.println(results);
       // System.out.println(g.getShortestPath(test.get(0), "서울 송파구 잠실본동 320"));
    }

    private static long getValue(String start, String end){
        GetDistance getDistance = new GetDistance(start, end);
        getDistance.start();
        while(getDistance.getResult() == -1);
        return getDistance.getResult();
    }
}
