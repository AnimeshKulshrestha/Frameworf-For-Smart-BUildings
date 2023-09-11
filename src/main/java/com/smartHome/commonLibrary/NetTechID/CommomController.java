package com.smartHome.commonLibrary.NetTechID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("common")
public class CommomController {

    @GetMapping("/discoverAll")
    public HashMap<String, HashMap<String,String>> allDevices() throws IOException, InterruptedException {
        GetAllDevices getAllDevices = new GetAllDevices();
        return getAllDevices.threadsForID();
    }

}
