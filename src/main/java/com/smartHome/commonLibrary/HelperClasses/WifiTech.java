package com.smartHome.commonLibrary.HelperClasses;

public class WifiTech extends NetworkTechnology {

    public String getIPAddr() {
        return IPAddr;
    }

    public void setIPAddr(String IPAddr) {
        this.IPAddr = IPAddr;
    }

    public String IPAddr;
    public WifiTech(String MAC,String IP){
        this.IPAddr = IP;
        this.MAC = MAC;
    }
}
