package com.smartHome.commonLibrary.HelperClasses;

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

    public void setPaired(boolean paired) {
        isPaired = paired;
    }

    private String name;
    public boolean isPaired;

    public BTTech(String name, boolean isPaired,String MAC) {
        this.setName(name);
        this.isPaired = isPaired;
        this.setMAC(MAC);
    }
}
