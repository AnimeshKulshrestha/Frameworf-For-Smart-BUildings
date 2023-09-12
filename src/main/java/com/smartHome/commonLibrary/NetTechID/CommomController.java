package com.smartHome.commonLibrary.NetTechID;

import com.smartHome.commonLibrary.HelperClasses.BTTech;
import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;
import com.smartHome.commonLibrary.HelperClasses.WifiTech;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("common")
public class CommomController {

    public static HashMap<String, HashMap<String,NetworkTechnology>> allDev = null;

    @GetMapping("/discoverAll")
    public HashMap<String, HashMap<String,NetworkTechnology>> allDevices() throws IOException, InterruptedException {
        GetAllDevices getAllDevices = new GetAllDevices();
        allDev = getAllDevices.threadsForID();
        return allDev;
    }

    @GetMapping("/connect")
    public String connect(@RequestParam String MAC) throws IOException, InterruptedException {
        if(allDev==null)
            allDev = new GetAllDevices().threadsForID();
        boolean isWifi = false
                ,isBT = false;

        if(allDev.get(GetAllDevices.wifi).containsKey(MAC)){
            WifiTech wifiDev = (WifiTech) allDev.get(GetAllDevices.wifi).get(MAC);
            String IP = wifiDev.getIP();
            /***
             * Use the IP address to perform Wi-fi Protocol in the following code
             */
            Thread mqtt = new Thread(new Runnable() {
                @Override
                public void run() {
                    /***
                     * Try to run MQTT protocol if it fails its probably Zigbee running in another thread
                      */
                }
            });
            Thread zigbee = new Thread(new Runnable() {
                @Override
                public void run() {
                    /***
                     * Try to run ZigBee protocol if it fails its probably MQTT running in another thread
                     */
                }
            });
            mqtt.start();
            zigbee.start();
            mqtt.join();
            zigbee.join();
            return "Wifi";

        }else{
            BTTech BTDev = (BTTech) allDev.get(GetAllDevices.bt).get(MAC);
            boolean isPaired = BTDev.isPaired();
            String name = BTDev.getName();
            return "Bluetooth";
            /***
             *
             *
             *  Use the Information to perform Bluetooth Protocols
             *
             */
        }
    }

}
