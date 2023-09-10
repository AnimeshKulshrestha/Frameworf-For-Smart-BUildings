package com.smartHome.commonLibrary.WifiDevices;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

@Service
public class SearchWifiDevices {

    
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
    public HashMap<String,String> findAllIPs() throws InterruptedException, IOException {
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
            });     // dont forget to start the thread
            threads[i].start();
        }
        for(int i = 1; i<=254; i++){
            threads[i].join();
        }
        return getIPMACMmap(ipList,defGateway);
    }
    
    public HashMap<String,String> getIPMACMmap(List<String> ipList,String defGate) throws IOException {
        HashMap<String ,String> res = new HashMap<String,String>();
        for(String recievedIP:ipList) {
            if(recievedIP.equalsIgnoreCase(defGate))
                continue;
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "arp -a " + recievedIP);
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
                        MAC = stringTokenizer.nextToken();
                        res.put(MAC,recievedIP);
                    }
                }
            }
        }
        return res;
    }

    public String getDefaultGateway(String ip) throws IOException {

        String defGateway = "";
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ipconfig");
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
                for(int i=0;i<l;i++){
                    words[i] = stringTokenizer.nextToken();
                }
                if(words[0].equalsIgnoreCase("Default")){
                    defGateway = words[l-1];
                    break;
                }
            }
        }
        System.out.println("Default Gateway "+defGateway);
        return defGateway;
    }
    /***
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
        String mess = "LED ON";
        byte[] payload = mess.getBytes();
        return new MqttMessage(payload);
    }
    ***/
}
