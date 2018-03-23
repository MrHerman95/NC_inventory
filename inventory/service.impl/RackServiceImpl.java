package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.model.impl.*;
import com.netcracker.edu.inventory.service.RackService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Herman Bocharov on 17.04.2017.
 */
class RackServiceImpl implements RackService {

    static protected Logger LOGGER = Logger.getLogger(RackServiceImpl.class.getName());
    private final String LINE_SEPARATOR = System.getProperty("line.separator");
    private DeviceServiceImpl devServiceImpl = new DeviceServiceImpl();

    public void outputRack(Rack rack, OutputStream outputStream) throws IOException {

        if (rack != null) {

            if (outputStream == null) {
                IllegalArgumentException e = new IllegalArgumentException("The output stream can not be null");
                LOGGER.log(Level.SEVERE, "The output stream has specified as null", e);
                throw e;
            }

            DataOutput dataOutput = new DataOutputStream(outputStream);

            dataOutput.writeInt(rack.getSize());
            dataOutput.writeUTF(rack.getTypeOfDevices().getName());

            for (int i = 0; i < rack.getSize(); i++) {
                if (rack.getDevAtSlot(i) == null) {
                    dataOutput.writeUTF(LINE_SEPARATOR);
                } else {
                    devServiceImpl.outputDevice(rack.getDevAtSlot(i), outputStream);
                }
            }
        }
    }

    public Rack inputRack(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("The input stream can not be null");
            LOGGER.log(Level.SEVERE, "The input stream has specified as null", e);
            throw e;
        }

        DataInput dataInput = new DataInputStream(inputStream);
        int size = dataInput.readInt();
        String className = dataInput.readUTF();

        RackArrayImpl rack = null;

        try {
            Class clazz = Class.forName(className);
            if (!Device.class.isAssignableFrom(clazz)) {
                ClassNotFoundException e = new ClassNotFoundException("Class should limit the type of devices");
                LOGGER.log(Level.SEVERE, "Class-delimiter not found", e);
                throw e;
            }

            rack = new RackArrayImpl(size, clazz);
            if (size == 0) {
                return rack;
            }

            for (int i = 0; i < rack.getSize(); i++) {
                Device device = devServiceImpl.inputDevice(inputStream);
                if (device != null) {
                    rack.insertDevToSlot(device, i);
                }
            }

        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Class not found", e);
            throw e;
        }

        return rack;
    }

    public void writeRack(Rack rack, Writer writer) throws IOException {
        NotImplementedException e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "The method is not implemented", e);
        throw e;
    }

    public Rack readRack(Reader reader) throws IOException, ClassNotFoundException {
        NotImplementedException e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "The method is not implemented", e);
        throw e;
    }
}
