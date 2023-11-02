package com.smartHome.commonLibrary.BluetoothDevices;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BluetoothProtocol {

    public static void main(String[] args) throws IOException {
        System.out.println(new BluetoothProtocol().getTemp("FCA89A004A93"));
//        new BluetoothProtocol().controlLED("FCA89A004A93","0");
    }

    /**
     * This method communicates with the bluetooth device
     * @param BTMAC MAC Address of Bluetooth device
     * @param command Sends the command to turn the LED ON or OFF
     *                1 - ON
     *                0 - OFF
     * @throws IOException
     */
    public void controlLED(String BTMAC, String command) throws IOException {
        StreamConnection streamConnection = (StreamConnection)
                Connector.open("btspp://"+BTMAC+":1;authenticate=false;encrypt=false;master=false");

        OutputStream outputStream = streamConnection.openOutputStream();
        outputStream.write(command.charAt(0));
        outputStream.close();

        streamConnection.close();
//        while (true);
    }

    public String getTemp(String BTMAC) throws IOException {
        System.out.println("Getting temp");
        StreamConnection streamConnection = (StreamConnection)
                Connector.open("btspp://"+BTMAC+":1;authenticate=false;encrypt=false;master=false");
        InputStream inputStream = streamConnection.openInputStream();
        byte[] temps = new byte[100];
        String receivedData = new String();

        int len;
        while (!receivedData.endsWith("%")) {
            len = inputStream.read(temps);
            receivedData += new String(temps, 0, len, "UTF-8");
        }
        inputStream.close();
        streamConnection.close();
        int length = receivedData.length();
        int startIndex = Math.max(0, length - 31);
        return receivedData.substring(startIndex);
    }
}

