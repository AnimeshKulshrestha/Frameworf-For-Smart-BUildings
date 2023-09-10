package com.smartHome.commonLibrary.BluetoothDevices;

import javax.bluetooth.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchBTDevices {

    public List<String> findDevicesAround() {
        List<String> macAdrList = new ArrayList<>();
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();

            DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();
            RemoteDevice[] remoteDevices = discoveryAgent.retrieveDevices(DiscoveryAgent.PREKNOWN);

            if (remoteDevices != null) {
                System.out.println("Discovered Bluetooth Devices:");
                for (RemoteDevice remoteDevice : remoteDevices) {
                    System.out.println("Device Name: " + remoteDevice.getFriendlyName(false));
                    System.out.println("Device Address: " + remoteDevice.getBluetoothAddress());
                    macAdrList.add(remoteDevice.getBluetoothAddress());
                }
            } else {
                System.out.println("No Bluetooth devices found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return macAdrList;
    }
}

