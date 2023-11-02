package com.smartHome.commonLibrary.HelperClasses;

import java.util.HashMap;

public class Constants {

    public Constants(){
        os = System.getProperty("os.name");
        isWindows = os.trim().toLowerCase().startsWith("windows");
    }
    public static String wifi = "WiFi";
    public static String bt = "Bluetooth";
    public static String toggle = "TOG";
    public static String recData = "RDT";
    public static String ip =  new String();

    public static String os;

    public static boolean isWindows;
    public static byte[] byteip = new byte[4];

    public static HashMap<String,NetworkTechnology> allDev = new HashMap<>();
    public static HashMap<String,String> registeredDev = new HashMap<>();

}
