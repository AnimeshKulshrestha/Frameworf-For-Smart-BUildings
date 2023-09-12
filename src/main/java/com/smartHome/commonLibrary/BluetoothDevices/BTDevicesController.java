package com.smartHome.commonLibrary.BluetoothDevices;

import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("BTDevices")
@CrossOrigin
public class BTDevicesController {

    @GetMapping("/getDevices")
    public HashMap<String,NetworkTechnology> getBTDevice() throws IOException, InterruptedException {
        return new SearchBTDevices().findALlBTDevices();
    }

}
