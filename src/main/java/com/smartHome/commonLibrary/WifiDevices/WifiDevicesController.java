package com.smartHome.commonLibrary.WifiDevices;

import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("WiFIDevices")
public class WifiDevicesController {

    @GetMapping("/getDevices")
    public HashMap<String,NetworkTechnology> getMQTTDevices() throws InterruptedException, IOException {
        SearchWifiDevices searchWifiDevices = new SearchWifiDevices();
        return searchWifiDevices.findAllIPs();
    }

}
