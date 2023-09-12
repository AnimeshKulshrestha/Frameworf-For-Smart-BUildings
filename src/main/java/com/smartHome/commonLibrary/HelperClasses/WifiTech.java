package com.smartHome.commonLibrary.HelperClasses;

public class WifiTech extends NetworkTechnology{

    private String IP;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
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
    }
}
