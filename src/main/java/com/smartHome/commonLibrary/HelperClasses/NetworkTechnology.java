package com.smartHome.commonLibrary.HelperClasses;

/**
 * This class is a super class for all networking technologies
 */
public class NetworkTechnology {
    private String MAC;

    private String netTech;

    public String getNetTech() {
        return netTech;
    }

    public void setNetTech(String netTech) {
        this.netTech = netTech;
    }

    private boolean registered = false;

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }
}


