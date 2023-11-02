package com.smartHome.commonLibrary.NetTechID;


import com.smartHome.commonLibrary.BluetoothDevices.SearchBTDevices;
import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;
import com.smartHome.commonLibrary.WifiDevices.SearchWifiDevices;

import java.io.IOException;
import java.util.*;

import static com.smartHome.commonLibrary.HelperClasses.Constants.*;

/**
 * This class has methods to get a common Map of devices all networking technologies
 */
public class GetAllDevices {


    public static void main(String[] args) throws InterruptedException, IOException {
        new GetAllDevices().threadsForID();
    }

    /**
     * This method finds a common Map of devices all networking technologies by creating different
     * threads for each of then
     * @return a common Map of devices all networking technologies
     * @throws InterruptedException
     * @throws IOException
     */
    public HashMap<String,NetworkTechnology> threadsForID() throws InterruptedException, IOException {
        List<String> netTechList = new ArrayList<>();
        netTechList.add(wifi);
        netTechList.add(bt);

        HashMap<String,NetworkTechnology> allDevList = new HashMap<String,NetworkTechnology>()
                ,wifiList = new HashMap<String,NetworkTechnology>()
                ,btList = new HashMap<String,NetworkTechnology>();


        SearchBTDevices searchBTDevices = new SearchBTDevices();
        SearchWifiDevices searchWifiDevices = new SearchWifiDevices();

        for(String netTech: netTechList) {
            if(netTech.equals(wifi)){
                System.out.println("Running wifi thread");
                wifiList = searchWifiDevices.findAllIPs();
            }

          if(netTech.equals(bt)){
                System.out.println("Running Bluetooth thread");
                if(isWindows)
                    btList = searchBTDevices.findALlBTDevicesWin();
                else
                    btList = searchBTDevices.findAllBTDevicesLin();
            }
        }
        searchWifiDevices.join();
        searchBTDevices.join();

        wifiList.forEach((k,v) -> allDevList.put(k,v));
        btList.forEach((k,v) -> allDevList.put(k,v));


        System.out.println("Wifi "+ wifiList);
        System.out.println(allDevList);


        return allDevList;
    }

}

