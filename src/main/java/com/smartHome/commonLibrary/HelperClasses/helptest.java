package com.smartHome.commonLibrary.HelperClasses;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class helptest {

    public static void main(String[] args) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/hello.py");
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        byte[] out = inputStream.readAllBytes();
        System.out.println(new String(out));
    }
}
