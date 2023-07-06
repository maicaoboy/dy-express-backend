package com.neu.dy.order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BaiduMaptest {
    public static void main(String[] args) throws IOException {
        String ak="Np2TtKZLfOEzrm8k6GQkAQE85svy6U2d";
        String address="辽宁省沈阳市东北大学南湖校区";
        String httpurl="http://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak="+ak;

        URL url = new URL(httpurl);
        // 打开和URL之间的连接，发送请求，返回数据
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        String line = null;
        StringBuilder json = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            json.append(line);
        }
        bufferedReader.close();
        System.out.println(json.toString());
    }
}
