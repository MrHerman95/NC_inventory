package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.exception.DeviceValidationException;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.Rack;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Herman Bocharov on 22.03.2017.
 */
public class RackArrayImpl implements Rack {
    static protected Logger LOGGER = Logger.getLogger(RackArrayImpl.class.getName());

    private int size;
    private Device[] devices;
    private Class clazz;

    public RackArrayImpl(int size) {
        this(size, Device.class);
    }

    public RackArrayImpl(int size, final Class clazz) {
        if (size < 0) {
            IllegalArgumentException e = new IllegalArgumentException("Rack size should not be negative");
            LOGGER.log(Level.SEVERE, "The negative rack size has specified", e);
            throw e;
        }
        if (clazz == null) {
            IllegalArgumentException e = new IllegalArgumentException("Type of device can not be null");
            LOGGER.log(Level.SEVERE, "Type of device has specified as null", e);
            throw e;
        }
        this.size = size;
        this.clazz = clazz;
        devices = new Device[size];
    }

    public int getSize() {
        return size;
    }

    public int getFreeSize() {
        int freeSize = 0;

        for (int index = 0; index < devices.length; index++) {
            if (devices[index] == null) {
                freeSize++;
            }
        }
        return freeSize;
    }

    public Class getTypeOfDevices() {
        return clazz;
    }

    public Device getDevAtSlot(int index) {
        if (isIndexCorrect(index)) {
            return devices[index];
        }
        return null;
    }

    public boolean insertDevToSlot(Device device, int index) {
        if ((device != null) && (!clazz.isInstance(device))) {
            IllegalArgumentException e = new IllegalArgumentException
                    ("Type of transmitted object is not compatible with type that can stored in rack");
            LOGGER.log(Level.SEVERE, "Incompatible type of transmitted object", e);
            throw e;
        } else {
            if (isIndexCorrect(index)) {
                if ((device == null) || (device.getIn() == 0)) {
                    DeviceValidationException e = new DeviceValidationException("Rack.insertDevToSlot", device);
                    LOGGER.log(Level.SEVERE, "The invalid device", e);
                    throw e;
                } else if (devices[index] == null) {
                    devices[index] = device;
                    return true;
                }
            }
            return false;
        }
    }

    public Device removeDevFromSlot(int index) {
        if (isIndexCorrect(index)) {
            if (devices[index] != null) {
                Device removedDevice = devices[index];
                devices[index] = null;
                return removedDevice;
            }
        }
        LOGGER.log(Level.WARNING, "Can not remove from empty slot " + index);
        return null;
    }

    public Device getDevByIN(int in) {
        for (int index = 0; index < devices.length; index++) {
            if((devices[index] != null) && (devices[index].getIn() == in)) {
                return devices[index];
            }
        }
        return null;
    }

    public Device[] getAllDeviceAsArray() {
        Device[] cloneDevices = new Device[size - getFreeSize()];
        int j = 0;

        for (int i = 0; i < devices.length; i++) {
            if (devices[i] != null) {
                cloneDevices[j] = devices[i];
                j++;
            }
        }
        return cloneDevices;
    }

    private boolean isIndexCorrect(int index) {
        if ((index >= 0) && (index < size)) {
            return true;
        } else {
            IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index " + index + " is incorrect. " +
                    "It should be in the range from 0 to " + (this.size == 0 ? 0 : this.size - 1));
            LOGGER.log(Level.SEVERE, "The incorrect index has specified", e);
            throw e;
        }
    }
}
