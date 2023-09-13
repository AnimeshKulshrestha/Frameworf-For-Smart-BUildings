package com.smartHome.commonLibrary.WifiDevices;

import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class is a controller for Wifi devices
 */
@RestController
@CrossOrigin
@RequestMapping("WiFIDevices")
public class WifiDevicesController {

    /**
     * This method returns all the Wi-Fi devices
     * @return This method return a Map of Wifi devices where the Key is their MAC address
     * @throws InterruptedException
     * @throws IOException
     */
    @GetMapping("/getDevices")
    public HashMap<String,NetworkTechnology> getWiFiDevices() throws InterruptedException, IOException {
        SearchWifiDevices searchWifiDevices = new SearchWifiDevices();
        return searchWifiDevices.findAllIPs();
    }

}
