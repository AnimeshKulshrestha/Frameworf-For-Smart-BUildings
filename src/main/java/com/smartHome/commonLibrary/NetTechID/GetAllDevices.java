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

    public void threadsForID() throws InterruptedException, IOException {
        List<String> netTechList = new ArrayList<>();
        netTechList.add(wifi);
        netTechList.add(bt);
        netTechList.add(zigbee);
        HashMap<String,String> IPMACMap = new HashMap<String,String>();
        HashMap<String,HashMap<String,String>> listMap = new HashMap<String,HashMap<String,String>>();
        for(String netTech: netTechList) {
            if(netTech.equals(GetAllDevices.wifi)){
                SearchWifiDevices searchWifiDevices = new SearchWifiDevices();
                System.out.println("Running wifi thread");
                IPMACMap = searchWifiDevices.findAllIPs();
            }
//
//          if(netTech.equals(GetAllDevices.bt)){
//                System.out.println("Running Bluetooth thread");
//                SearchBTDevices searchBTDevices = new SearchBTDevices();
//                List<String> macAdrList = searchBTDevices.findDevicesAround();
//                for(String macAddr :macAdrList){
//                    System.out.println("On WiFi: "+macAddr);
//                }
//                System.out.println(macAdrList);
//            }
        }
        listMap.put(wifi,IPMACMap);
        HashSet<String> MACAddress = new HashSet<String>();

        System.out.println(listMap.get(wifi)+" all devices");
    }
}

