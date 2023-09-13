package com.smartHome.commonLibrary.WifiDevices;

import com.smartHome.commonLibrary.HelperClasses.NetworkTechnology;
import com.smartHome.commonLibrary.HelperClasses.WifiTech;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * This class lists all the Wi-Fi devices connected to the network Interface of the server
 */
@Service
public class SearchWifiDevices extends Thread{

    public String cmdPrompt;
    public Boolean isWindows;
    public String middlearg;

    /**
     * This constructor determines the Operating System used by the server's machine
     * as it is needed to run terminal commands
     */
    public SearchWifiDevices(){
           String os = System.getProperty("os.name");
           if(os.trim().toLowerCase().startsWith("windows")) {
               this.isWindows = true;
               this.cmdPrompt = "cmd.exe";
               this.middlearg = "/c";
           }
           else {
               this.isWindows = false;
               this.cmdPrompt = "/bin/sh";
               this.middlearg = "-c";
           }

           System.out.println(cmdPrompt);
    }

    /**
     * Finds the preferred IP address for communication
     * @return IP address of the server in byte array
     */
    public byte[] getIp(){
        byte[] ip = new byte[4];
        try {
            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                System.out.println(socket.getLocalAddress().getHostAddress() + " curr add");
                InetAddress reqHost = InetAddress.getByName(socket.getLocalAddress().getHostAddress());
                ip = reqHost.getAddress();
            }
        } catch (Exception e) {
            System.out.println("didnt get ip");
        }
        return ip;
    }

    /**
     * This method finds all Wi-Fi devices on the Network Interface of the server
     * @return This method return a Map of Wi-Fi devices where the Key is their MAC address
     * @throws InterruptedException
     * @throws IOException
     */
    public HashMap<String,NetworkTechnology> findAllIPs() throws InterruptedException, IOException {
        final byte[] ip = getIp();
        if(ip==null)
            return null;
        List<String> ipList = new ArrayList<>();
        String defGateway = getDefaultGateway(InetAddress.getByAddress(ip).getHostAddress());
        Thread[] threads = new Thread[255];
        for(int i=1;i<=254;i++) {
            final int j = i;  // i as non-final variable cannot be referenced from inner class
            threads[i] = new Thread(new Runnable() {   // new thread for parallel execution
                public void run() {
                    try {
                        ip[3] = (byte)j;
                        InetAddress address = InetAddress.getByAddress(ip);
                        String output = address.toString().substring(1);
                        if (address.isReachable(5000)) {
                            System.out.println(output + " is on the network");
                            ipList.add(output);
                        }
                    } catch (Exception e) {}
                }
            });     // don't forget to start the thread
            threads[i].start();
        }
        for(int i = 1; i<=254; i++){
            threads[i].join();
        }
        return getIPMACmap(ipList,defGateway);
    }

    /**
     * This method maps the Wi-Fi devices to their MAC addresses
     * @param ipList List of IP addresses on the Network Interface of the server
     * @param defGate The Default Gateway IP address so that it can be removed from the final map
     * @return This method return a Map of Bluetooth devices where the Key is their MAC address
     * @throws IOException
     */
    public HashMap<String,NetworkTechnology> getIPMACmap(List<String> ipList, String defGate) throws IOException {
        HashMap<String,NetworkTechnology> wifiList = new HashMap<String,NetworkTechnology>();
        for(String recievedIP:ipList) {
            String recIPcopy = new String(recievedIP);
            if(!this.isWindows){
                recievedIP = "("+recievedIP+")";
            }
            if(recievedIP.equalsIgnoreCase(defGate))
                continue;
            ProcessBuilder builder = new ProcessBuilder(this.cmdPrompt, this.middlearg, "arp -a " + recievedIP);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line, MAC;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                StringTokenizer stringTokenizer = new StringTokenizer(line);
                if (stringTokenizer.hasMoreTokens()) {
                    String word = stringTokenizer.nextToken();
                    if (word.equalsIgnoreCase(recievedIP)) {
                        if(!isWindows)
                            stringTokenizer.nextToken();
                        MAC = stringTokenizer.nextToken();
                        MAC = MAC.substring(0,2)+
                                MAC.substring(3,5)+
                                MAC.substring(6,8)+
                                MAC.substring(9,11)+
                                MAC.substring(12,14)+
                                MAC.substring(15,17);
                        MAC = MAC.toUpperCase();
                        wifiList.put(MAC,new WifiTech(recIPcopy ,MAC));
                    }
                }
            }
        }
        return wifiList;
    }

    /**
     * This method finds out the default gateway IP address by parsing the terminal commands
     * as per requirement
     * @param ip The IP address of the Network Interface of the server
     * @return The default gateway IP address as a String
     * @throws IOException
     */
    public String getDefaultGateway(String ip) throws IOException {
        String defGateway = "";
        if(!isWindows){
            ProcessBuilder builder = new ProcessBuilder(cmdPrompt, middlearg, "ip route | grep default");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true){
                line = r.readLine();
                if (line == null) {
                    break;
                }
                StringTokenizer stringTokenizer = new StringTokenizer(line);
                stringTokenizer.nextToken();
                stringTokenizer.nextToken();
                defGateway = stringTokenizer.nextToken();
            }
            return defGateway;
        }
        ProcessBuilder builder = new ProcessBuilder(cmdPrompt, middlearg, "ipconfig");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        boolean wifiData = false;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            StringTokenizer stringTokenizer = new StringTokenizer(line);
            while(!wifiData && stringTokenizer.hasMoreTokens()) {
                String word = stringTokenizer.nextToken();
                if(word.equals(ip)) {
                    wifiData = true;
                }
            }
            if(wifiData){
                int l = stringTokenizer.countTokens();
                if(l==0)
                    continue;
                String[] words = new String[l];
                HashSet<String> lineSet = new HashSet<String>();
                for(int i=0;i<l;i++){
                    words[i] = stringTokenizer.nextToken();
                    lineSet.add(words[i].toLowerCase());
                }
                if(lineSet.contains("gateway")){//words[0].equalsIgnoreCase("Default")){
                    defGateway = words[l-1];
                    break;
                }
            }
        }
        System.out.println("Default Gateway "+defGateway);
        return defGateway;
    }


    /*
     *
     *
     *
     *      update later based on requirement
     *
     *
     *
     *
     *
    public List<MqttMessage> findDevicesAround(){

        String broker = "tcp://192.168.45.53:1883"; // MQTT broker address
        String publisherId = UUID.randomUUID().toString();
        List<MqttMessage> messageList = new ArrayList<>();
        try {
            client = new MqttClient("tcp://192.168.45.53:1883", publisherId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");
            String topic = "LED"; // Adjust the topic as needed
            this.call();
            client.subscribe(topic, (topic1, message) -> {
                String payload = new String(message.getPayload());
                System.out.println("Received message on topic " + topic1 + ": " + payload);
                messageList.add(message);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return messageList;
    }

    @Override
    public Void call() throws Exception {
        if ( !client.isConnected()) {
            return null;
        }
        MqttMessage msg = readEngineTemp();
        msg.setQos(0);
        msg.setRetained(true);
        client.publish("LED",msg);
        System.out.println("publcished");
        return null;
    }

    private MqttMessage readEngineTemp() {
        String mess = "LED OFF";
        byte[] payload = mess.getBytes();
        return new MqttMessage(payload);
    }
    */
}
