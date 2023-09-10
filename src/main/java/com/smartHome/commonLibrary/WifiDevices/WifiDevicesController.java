package com.smartHome.commonLibrary.WifiDevices;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("WiFIDevices")
public class WifiDevicesController {

    @GetMapping("/getDevices")
    public HashMap<String,String> getMQTTDevices() throws InterruptedException, IOException {
        SearchWifiDevices searchWifiDevices = new SearchWifiDevices();
        return searchWifiDevices.findAllIPs();
    }

}
