package com.smartHome.commonLibrary.NetTechID;

import com.smartHome.commonLibrary.HelperClasses.BTTech;
import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;
import com.smartHome.commonLibrary.HelperClasses.WifiTech;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;


/**
 * This class is a generalised controller for all networking technologies
 */
@RestController
@CrossOrigin
@RequestMapping("common")
public class CommomController{

    public static HashMap<String, HashMap<String,NetworkTechnology>> allDev = null;

    /**
     * This method discovers all devices around for all network technologies
     * @return A Map of all the devices where they key is the network Technologies
     *          The devices are stored in a Map where the key is their MAC Address
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping("/discoverAll")
    public HashMap<String, HashMap<String,NetworkTechnology>> allDevices() throws IOException, InterruptedException {
        GetAllDevices getAllDevices = new GetAllDevices();
        allDev = getAllDevices.threadsForID();
        return allDev;
    }

    /**
     *
     * This method checks if the MAC address is present or available for
     * connection and if it is then it runs whichever protocol the device runs on
     * by checking for all of them.
     *
     * @param MAC MAC address of the device that has to be connected
     * @return The network technology used or if the MAC address is not found
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping("/connect")
    public String connect(@RequestParam String MAC) throws IOException, InterruptedException {
        if(allDev==null)
            allDev = new GetAllDevices().threadsForID();
        if(allDev.get(GetAllDevices.wifi).containsKey(MAC)){
            WifiTech wifiDev = (WifiTech) allDev.get(GetAllDevices.wifi).get(MAC);
            String IP = wifiDev.getIP();
            /*
             * Use the IP address to perform Wi-fi Protocol in the following code
             */
            Thread mqtt = new Thread(new Runnable() {

                @Override
                public void run() {

                    new MQTTProtocol().runMQTTProtocol();
                    /*
                     * Try to run MQTT protocol if it fails its probably Zigbee running in another thread
                     */
                }
            });
            Thread zigbee = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Zigbee check");
                    /*
                     * Try to run ZigBee protocol if it fails its probably MQTT running in another thread
                     */
                }
            });
            mqtt.start();
            zigbee.start();
            mqtt.join();
            zigbee.join();
            return "Wifi";

        }else if(allDev.get(GetAllDevices.bt).containsKey(MAC)){
            BTTech BTDev = (BTTech) allDev.get(GetAllDevices.bt).get(MAC);
            boolean isPaired = BTDev.isPaired();
            String name = BTDev.getName();
            return "Bluetooth";
            /*
             *  Use the Information to perform Bluetooth Protocols
             */
        }
        return "MAC address not found discover again";
    }


}
