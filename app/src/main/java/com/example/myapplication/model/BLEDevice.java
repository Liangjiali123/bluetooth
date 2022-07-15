package com.example.myapplication.model;

import java.util.Objects;

public class BLEDevice {
    private String name;
    private String address;
    private int signal;

    public BLEDevice(String name, String address, int signal) {
        this.name = name;
        this.address = address;
        this.signal = signal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BLEDevice bleDevice = (BLEDevice) o;
        return  Objects.equals(name, bleDevice.name) && Objects.equals(address, bleDevice.address);
    }


}
