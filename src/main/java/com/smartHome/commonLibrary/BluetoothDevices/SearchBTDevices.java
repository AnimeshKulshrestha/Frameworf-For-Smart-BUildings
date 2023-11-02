package com.smartHome.commonLibrary.BluetoothDevices;

import com.smartHome.commonLibrary.HelperClasses.BTTech;
import com.smartHome.commonLibrary.HelperClasses.Constants;
import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.bluetooth.*;

import static com.smartHome.commonLibrary.HelperClasses.Constants.*;

/**
 * This class has methods useful for discovering Bluetooth devices available for connection
 */
public class SearchBTDevices extends Thread{

    public HashMap<String,NetworkTechnology> findAllBTDevicesLin() throws IOException {
        HashMap<String,NetworkTechnology> BTList = new HashMap<>();
        ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/hello.py");
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        byte[] out = inputStream.readAllBytes();
        String btdevs = new String(out);
        StringTokenizer stringTokenizer = new StringTokenizer(btdevs);
        while (stringTokenizer.hasMoreTokens()){
            String name = stringTokenizer.nextToken().toString()
                    ,MAC = stringTokenizer.nextToken().toString();
            BTList.put(MAC,new BTTech(name,MAC));
        }
        return BTList;
    }

    /**
     * This method discovers Bluetooth devices around the server
     * @return This method return a Map of Bluetooth devices where the Key is their MAC address
     * @throws IOException
     * @throws InterruptedException
     */
    public HashMap<String,NetworkTechnology> findALlBTDevicesWin() throws IOException, InterruptedException {

        final Object inquiryCompletedEvent = new Object();
        HashMap<String,NetworkTechnology> BTList = new HashMap<String,NetworkTechnology>();
        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                try {
                    String name = btDevice.getFriendlyName(false)
                            ,MAC = btDevice.getBluetoothAddress();
                    BTTech btTech = new BTTech(name,btDevice.isTrustedDevice(),MAC);
                    btTech.setNetTech(bt);
                    if(Constants.registeredDev.containsKey(MAC)){
                        btTech.setRegistered(true);
                        btTech.setDevType(registeredDev.get(MAC));
                    }
                    BTList.put(MAC,btTech);
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
            DiscoveryAgent discoveryAgent = LocalDevice.getLocalDevice().getDiscoveryAgent();
            boolean started = discoveryAgent.startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                System.out.println("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
            }
        }
        return BTList;
    }
}