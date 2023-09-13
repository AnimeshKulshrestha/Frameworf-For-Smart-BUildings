package com.smartHome.commonLibrary.BluetoothDevices;

import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;

/**
 * This class is a controller for Bluetooth Devices
 */
@RestController
@RequestMapping("BTDevices")
@CrossOrigin
public class BTDevicesController {

    /**
     * @return This method return a Map of Bluetooth devices where the Key is their MAC address
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping("/getDevices")
    public HashMap<String,NetworkTechnology> getBTDevice() throws IOException, InterruptedException {
        return new SearchBTDevices().findALlBTDevices();
    }

}
