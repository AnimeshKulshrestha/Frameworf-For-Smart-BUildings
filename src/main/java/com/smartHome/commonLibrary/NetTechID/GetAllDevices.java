package com.smartHome.commonLibrary.NetTechID;


import com.smartHome.commonLibrary.BluetoothDevices.SearchBTDevices;
import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;
import com.smartHome.commonLibrary.HelperClasses.WifiTech;
import com.smartHome.commonLibrary.WifiDevices.SearchWifiDevices;

import java.io.IOException;
import java.util.*;


public class GetAllDevices {

    public static String wifi = "WiFi";
    public static String bt = "Bluetooth";

    public static void main(String[] args) throws InterruptedException, IOException {
        new GetAllDevices().threadsForID();
    }

    public HashMap<String,HashMap<String,NetworkTechnology>> threadsForID() throws InterruptedException, IOException {
        List<String> netTechList = new ArrayList<>();
        netTechList.add(wifi);
        netTechList.add(bt);

        HashMap<String,HashMap<String,NetworkTechnology>> devList = new HashMap<String,HashMap<String,NetworkTechnology>>();

        HashMap<String,NetworkTechnology> WifiList = new HashMap<String,NetworkTechnology>()
                    ,BTList = new HashMap<String,NetworkTechnology>();
        HashMap<String,String> NameMACMap = new HashMap<String,String>();


        SearchBTDevices searchBTDevices = new SearchBTDevices();
        SearchWifiDevices searchWifiDevices = new SearchWifiDevices();

        for(String netTech: netTechList) {
            if(netTech.equals(GetAllDevices.wifi)){
                System.out.println("Running wifi thread");
                WifiList = searchWifiDevices.findAllIPs();
            }

          if(netTech.equals(GetAllDevices.bt)){
                System.out.println("Running Bluetooth thread");
                BTList = searchBTDevices.findALlBTDevices();
            }
        }
        searchWifiDevices.join();
        searchBTDevices.join();
        devList.put(wifi,WifiList);
        devList.put(bt,BTList);

        System.out.println(devList.get(wifi).toString()+" all wifi devices");
        System.out.println(devList.get(bt).toString()+" all BT devices");

        return devList;
    }

}

