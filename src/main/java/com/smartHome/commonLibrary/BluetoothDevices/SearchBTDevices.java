package com.smartHome.commonLibrary.BluetoothDevices;

import com.smartHome.commonLibrary.HelperClasses.BTTech;
import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.bluetooth.*;
public class SearchBTDevices extends Thread{
    public HashMap<String,NetworkTechnology> findALlBTDevices() throws IOException, InterruptedException {

        final Object inquiryCompletedEvent = new Object();
        HashMap<String,NetworkTechnology> BTList = new HashMap<String,NetworkTechnology>();
        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                try {
                    String name = btDevice.getFriendlyName(false)
                            ,MAC = btDevice.getBluetoothAddress();
                    BTList.put(MAC,new BTTech(name,btDevice.isTrustedDevice(),MAC));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public void inquiryCompleted(int discType) {
                System.out.println("Device Inquiry completed!");
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };

        synchronized(inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                System.out.println("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
            }
        }
        return BTList;
    }
}