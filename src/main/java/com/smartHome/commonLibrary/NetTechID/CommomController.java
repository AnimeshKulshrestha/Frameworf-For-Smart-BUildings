package com.smartHome.commonLibrary.NetTechID;

import com.smartHome.commonLibrary.BluetoothDevices.BluetoothProtocol;
import com.smartHome.commonLibrary.HelperClasses.BTTech;
import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;
import com.smartHome.commonLibrary.HelperClasses.Constants;
import com.smartHome.commonLibrary.HelperClasses.WifiTech;
import com.smartHome.commonLibrary.WifiDevices.MQTTProtocol;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

import static com.smartHome.commonLibrary.HelperClasses.Constants.*;


/**
 * This class is a generalised controller for all networking technologies
 */
@RestController
@CrossOrigin
@RequestMapping("common")
public class CommomController{

//    public static HashMap<String, HashMap<String,NetworkTechnology>> allDev = null;

    /**
     * This method discovers all devices around for all network technologies
     * @return A Map of all the device the devices are stored in a Map where the key is their MAC Address
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping("/discoverAll")
    public HashMap<String,NetworkTechnology> allDevices() throws IOException, InterruptedException {
        GetAllDevices getAllDevices = new GetAllDevices();
        Constants.allDev = getAllDevices.threadsForID();
        return Constants.allDev;
    }

    /**
     *
     * This method checks if the MAC address is present or available for
     * connection and if it is then it runs whichever protocol the device runs on
     * by checking for all of them.
     *
     * @param MAC MAC address of the device that has to be connected
     * @param cmd Command to turn on or off
     *            1 - on
     *            0 off
     *            any value works for devices not requiring to be turned off
     * @return The network technology used or if the MAC address is not found
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping("/connect")
    public String connect(@RequestParam String MAC, @RequestParam(required = false) String cmd) throws IOException, InterruptedException {
        if(Constants.allDev==null)
            Constants.allDev = new GetAllDevices().threadsForID();
        String devType = new String();
        if(allDev.containsKey(MAC) && allDev.get(MAC).getNetTech().equals(wifi)) {
            WifiTech wifiDev = (WifiTech) Constants.allDev.get(MAC);
            if(Constants.allDev.get(MAC).isRegistered()){
                devType = wifiDev.getDevType();
            }else{
                return "First register Device";
            }
            
            String IP = wifiDev.getIP();
            /*
             * Use the IP address to perform Wi-fi Protocol in the following code
             */
            String finalDevType = devType;
            final String[] ret = { "Wifi" };
            Thread mqtt = new Thread(new Runnable() {

                @Override
                public void run() {

                    if(finalDevType.equals(toggle)) {
                        if(cmd==null)
                            ret[0] = "Empty command";
                        new MQTTProtocol().controlLEDMQTT(cmd);
                    }
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
            return ret[0];

        } else if (allDev.containsKey(MAC) && allDev.get(MAC).getNetTech().equals(bt)){
            BTTech BTDev = (BTTech) Constants.allDev.get(MAC);
            if(Constants.allDev.get(MAC).isRegistered()){
                devType = BTDev.getDevType();
            }else{
                return "First register Device";
            }
            boolean isPaired = BTDev.isPaired();
            String name = BTDev.getName();
            if(devType.equals(toggle))
                new BluetoothProtocol().controlLED(MAC,cmd);
            else if(devType.equals(recData))
                return new BluetoothProtocol().getTemp(MAC);
            return "Bluetooth";
            /*
             *  Use the Information to perform Bluetooth Protocols
             */
        }
        return "MAC address not found discover again";
    }

    @GetMapping("/register")
    public String register(@RequestParam String MAC, @RequestParam String dt) throws IOException, InterruptedException {
        if(!dt.equalsIgnoreCase(toggle) && !dt.equalsIgnoreCase(recData))
            return "Invalid device type";
        if(Constants.allDev==null)
            Constants.allDev = new GetAllDevices().threadsForID();
        if(allDev.containsKey(MAC) && allDev.get(MAC).getNetTech().equals(wifi)) {
            WifiTech wifiDev = (WifiTech) Constants.allDev.get(MAC);
            if (Constants.allDev.get(MAC).isRegistered()) {
                return "already registered";
            }
            wifiDev.setDevType(dt);
            wifiDev.setRegistered(true);
        } else if (allDev.containsKey(MAC) && allDev.get(MAC).getNetTech().equals(bt)){
            BTTech btDev = (BTTech) allDev.get(MAC);
            if (Constants.allDev.get(MAC).isRegistered()) {
                return "already registered";
            }
            btDev.setDevType(dt);
            btDev.setRegistered(true);

        }
        registeredDev.put(MAC,dt);
        return "Registererd "+MAC;
    }

    @GetMapping("/unregister")
    public String unregister(@RequestParam String MAC) throws IOException, InterruptedException {
        if(Constants.allDev==null)
            Constants.allDev = new GetAllDevices().threadsForID();
        if(allDev.containsKey(MAC) && allDev.get(MAC).getNetTech().equals(wifi)) {
            WifiTech wifiDev = (WifiTech) Constants.allDev.get(MAC);
            if (!Constants.allDev.get(MAC).isRegistered()) {
                return "not registered yet";
            }
            wifiDev.setDevType(null);
            wifiDev.setRegistered(false);
        } else if (allDev.containsKey(MAC) && allDev.get(MAC).getNetTech().equals(bt)){
            BTTech btDev = (BTTech) allDev.get(MAC);
            if (!Constants.allDev.get(MAC).isRegistered()) {
                return "not registered yet";
            }
            btDev.setDevType(null);
            btDev.setRegistered(false);

        }
        registeredDev.remove(MAC);
        return "Unegistererd "+MAC;
    }
    
}
