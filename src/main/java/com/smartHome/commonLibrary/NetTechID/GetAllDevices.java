package com.smartHome.commonLibrary.NetTechID;


import com.smartHome.commonLibrary.BluetoothDevices.SearchBTDevices;
import com.smartHome.commonLibrary.WifiDevices.SearchWifiDevices;

import java.io.IOException;
import java.util.*;


public class GetAllDevices {

    public static String wifi = "WiFi";
    public static String bt = "Bluetooth";
    public static String zigbee = "Zigbee";

    public static void main(String[] args) throws InterruptedException, IOException {
        new GetAllDevices().threadsForID();
    }

    public HashMap<String,HashMap<String,String>> threadsForID() throws InterruptedException, IOException {
        List<String> netTechList = new ArrayList<>();
        netTechList.add(wifi);
        netTechList.add(bt);
        netTechList.add(zigbee);
        HashMap<String,String> IPMACMap = new HashMap<String,String>();
        HashMap<String,String> NameMACMap = new HashMap<String,String>();
        HashMap<String,HashMap<String,String>> listMap = new HashMap<String,HashMap<String,String>>();
        SearchBTDevices searchBTDevices = new SearchBTDevices();
        SearchWifiDevices searchWifiDevices = new SearchWifiDevices();
        for(String netTech: netTechList) {
            if(netTech.equals(GetAllDevices.wifi)){
                System.out.println("Running wifi thread");
                IPMACMap = searchWifiDevices.findAllIPs();
            }

          if(netTech.equals(GetAllDevices.bt)){
                System.out.println("Running Bluetooth thread");
                NameMACMap = searchBTDevices.findALlBTDevices();
            }
        }
        searchWifiDevices.join();
        searchBTDevices.join();
        listMap.put(wifi,IPMACMap);
        listMap.put(bt,NameMACMap);

        System.out.println(listMap.get(wifi)+" all wifi devices");
        System.out.println(listMap.get(bt)+" all BT devices");
        return listMap;
    }
}

