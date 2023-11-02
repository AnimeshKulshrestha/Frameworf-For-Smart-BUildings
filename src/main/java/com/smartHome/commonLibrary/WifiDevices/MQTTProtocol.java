package com.smartHome.commonLibrary.WifiDevices;

import com.smartHome.commonLibrary.HelperClasses.Constants;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * This class implements the MQTTProtocol on Wi-Fi devices
 */
public class MQTTProtocol extends Thread implements Callable<Void> {
    public MqttClient client;
    boolean on;

    /**
     * This method runs the MQTT Protocol for LED turn on and off
     * @param command Sends the command to turn the LED ON or OFF
     *                1 - ON
     *                0 - OFF
     */
    public void controlLEDMQTT(String command){

        String broker = "tcp://"+ Constants.ip +":1883"; // MQTT broker address
        String publisherId = UUID.randomUUID().toString();
        try {
            client = new MqttClient("tcp://"+ Constants.ip +":1883", publisherId,new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");
            String topic = "LED"; // Adjust the topic as needed
            on = command.equalsIgnoreCase("1");
            this.call();
            client.subscribe(topic, (topic1, message) -> {
                String payload = new String(message.getPayload());
                System.out.println("Received message on topic " + topic1 + ": " + payload);
//                    messageList.add(message);
            });
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
