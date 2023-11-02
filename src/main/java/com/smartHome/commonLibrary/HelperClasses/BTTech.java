package com.smartHome.commonLibrary.HelperClasses;

/**
 * This is a class to store all information
 * about the Bluetooth devices
 */
public class BTTech extends NetworkTechnology{
    @Override
    public String toString() {
        return "BTTech{" +
                "name='" + getName() + '\'' +
                ", isPaired=" + isPaired +
                ", MAC='" + getMAC() + '\'' +
                '}';
    }

    private String name;
    private boolean isPaired;
    private String devType;

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPaired() {
        return isPaired;
    }

    public BTTech(String name, String MAC) {
        this.setMAC(MAC);
        this.name = name;
    }

    public BTTech(String name, boolean isPaired, String MAC) {
        this.setName(name);
        this.isPaired = isPaired;
        this.setMAC(MAC);
        this.setRegistered(false);
    }
}
