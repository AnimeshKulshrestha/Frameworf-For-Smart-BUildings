package com.smartHome.commonLibrary.NetTechID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * This class implements the MQTTProtocol on Wi-Fi devices
 */
public class MQTTProtocol extends Thread implements Callable<Void> {
    public MqttClient client;
    boolean on;

    public static void main(String[] args){
        MQTTProtocol mqttProtocol = new MQTTProtocol();
        mqttProtocol.runMQTTProtocol();
    }

    /**
     * This method runs the MQTT Protocol for LED turn on and off
     */
    public void runMQTTProtocol(){

        String broker = "tcp://192.168.45.53:1883"; // MQTT broker address
        String publisherId = UUID.randomUUID().toString();
        try {
            client = new MqttClient("tcp://192.168.45.53:1883", publisherId,new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");
            String topic = "LED"; // Adjust the topic as needed
            while(true) {
                System.out.println("Enter Input");
                System.out.println("Turn LED ON : 1");
                System.out.println("Turn LED OFF : 0");
                System.out.println("Exit : -1");
                Scanner sc = new Scanner(System.in);
                int inp = -1;
                try {
                    inp = sc.nextInt();
                }catch (Exception exception){
                    System.out.println("Invalid Input");
                    continue;
                }
                if(inp==1)
                    on = true;
                else if(inp==0)
                    on = false;
                else if(inp==-1) {
//                    sc.close();
                    return;
                }
                else{
                    System.out.println("Invalid input");
                    continue;
                }
                this.call();
                client.subscribe(topic, (topic1, message) -> {
                    String payload = new String(message.getPayload());
                    System.out.println("Received message on topic " + topic1 + ": " + payload);
//                    messageList.add(message);
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Call function that sends the publishes the message on device
     * @return
     * @throws Exception
     */
    @Override
    public Void call() throws Exception {
        if ( !client.isConnected()) {
            return null;
        }
        MqttMessage msg;
        if(on)
            msg = turnLEDOn();
        else
            msg = turnLEDOff();
        msg.setQos(0);
        msg.setRetained(true);
        client.publish("LED",msg);
        System.out.println("publcished");
        return null;
    }

    /**
     * Method to turn LED off
     * @return MQTT message that is sent to turn the LED off
     */
    private MqttMessage turnLEDOff() {
        String mess = "LED OFF";
        byte[] payload = mess.getBytes();
        return new MqttMessage(payload);
    }

    /**
     * Method to turn LED on
     * @return MQTT message that is sent to turn the LED on
     */
    private MqttMessage turnLEDOn() {
        String mess = "LED ON";
        byte[] payload = mess.getBytes();
        return new MqttMessage(payload);
    }
}
