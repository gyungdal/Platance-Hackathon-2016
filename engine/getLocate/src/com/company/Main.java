package com.company;

import com.company.locate.GetDistance;

public class Main {
    public static void main(String[] args) {
        GetDistance getDistance = new GetDistance("서울특별시 강남구 도산대로 176", "서울 서초구 잠원동 58-16 신반포 10차 316-1204");
        getDistance.start();
    }
}
