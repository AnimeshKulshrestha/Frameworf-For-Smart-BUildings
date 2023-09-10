package com.smartHome.commonLibrary.BluetoothDevices;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("BTDevices")
@CrossOrigin
public class BTDevicesController {

    @PostMapping("/getDevices")
    public List<String> getBTDevice(){
        return new SearchBTDevices().findDevicesAround();
    }

}
