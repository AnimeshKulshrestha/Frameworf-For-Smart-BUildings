package com.smartHome.commonLibrary.HelperClasses;

/**
 * This class is to store all the information
 * about the Wi-Fi devices
 */
public class WifiTech extends NetworkTechnology{

    private String IP;
    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    private String devType;


    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }
    @Override
    public String toString() {
        return "WifiTech{" +
                "IP='" + getIP() + '\'' +
                ", MAC='" + getMAC() + '\'' +
                '}';
    }

    public WifiTech(String IP, String MAC) {
        this.setIP(IP);
        this.setMAC(MAC);
        this.setRegistered(false);
    }
}
