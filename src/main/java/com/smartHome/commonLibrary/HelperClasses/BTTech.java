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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPaired() {
        return isPaired;
    }

    private String name;
    private boolean isPaired;

    public BTTech(String name, boolean isPaired,String MAC) {
        this.setName(name);
        this.isPaired = isPaired;
        this.setMAC(MAC);
    }
}
