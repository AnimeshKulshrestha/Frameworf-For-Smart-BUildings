package com.smartHome.commonLibrary.BluetoothDevices;

import com.smartHome.commonLibrary.HelperClasses.BTTech;
import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;

import java.io.IOException;
import java.util.HashMap;
import javax.bluetooth.*;

/**
 * This class has methods useful for discovering Bluetooth devices available for connection
 */
public class SearchBTDevices extends Thread{

    /**
     * This method discovers Bluetooth devices around the server
     * @return This method return a Map of Bluetooth devices where the Key is their MAC address
     * @throws IOException
     * @throws InterruptedException
     */
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