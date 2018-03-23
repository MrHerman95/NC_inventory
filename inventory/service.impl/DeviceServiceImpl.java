package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.exception.DeviceValidationException;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.impl.Battery;
import com.netcracker.edu.inventory.model.impl.Router;
import com.netcracker.edu.inventory.model.impl.Switch;
import com.netcracker.edu.inventory.model.impl.WifiRouter;
import com.netcracker.edu.inventory.service.DeviceService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Herman Bocharov on 17.04.2017.
 */
class DeviceServiceImpl implements DeviceService {

    static protected Logger LOGGER = Logger.getLogger(DeviceServiceImpl.class.getName());
    private final String LINE_SEPARATOR = System.getProperty("line.separator");

    public void sortByIN(Device[] devices) {
        java.util.Arrays.sort(devices, new Comparator<Device>() {
            @Override
            public int compare(Device o1, Device o2) {
                if (o1 == null) {
                    return 1;
                }
                if (o1.getIn() == 0) {
                    if (o2 != null) {
                        return 1;
                    }
                }
                if (o2 == null) {
                    return -1;
                }
                if (o2.getIn() == 0) {
                    return -1;
                }
                if (o1.getIn() > o2.getIn()) {
                    return 1;
                } else if (o1.getIn() < o2.getIn()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    public void sortByProductionDate(Device[] devices) {
        java.util.Arrays.sort(devices, new Comparator<Device>() {
            @Override
            public int compare(Device o1, Device o2) {
                if (o1 == null) {
                    return 1;
                }
                if (o1.getProductionDate() == null) {
                    if (o2 != null) {
                        return 1;
                    }
                }
                if (o2 == null) {
                    return -1;
                }
                if (o2.getProductionDate() == null) {
                    return -1;
                }

                return o1.getProductionDate().compareTo(o2.getProductionDate());
            }
        });
    }

    public void filtrateByType(Device[] devices, String type) {
        for (int i = 0; i < devices.length; i++) {
            if (devices[i] != null) {
                filtrateBy(devices, i, devices[i].getType(), type);
            }
        }
    }

    public void filtrateByManufacturer(Device[] devices, String manufacturer) {
        for (int i = 0; i < devices.length; i++) {
            if (devices[i] != null) {
                filtrateBy(devices, i, devices[i].getManufacturer(), manufacturer);
            }
        }
    }

    public void filtrateByModel(Device[] devices, String model) {
        for (int i = 0; i < devices.length; i++) {
            if (devices[i] != null) {
                filtrateBy(devices, i, devices[i].getModel(), model);
            }
        }
    }

    private void filtrateBy(Device[] device, int i, String method, String property) {
        if (device[i] != null) {
            if (!equals(method, property)) {
                device[i] = null;
            }
        }
    }

    private static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public boolean isValidDeviceForInsertToRack(Device device) {
        if ((device != null) && (device.getIn() > 0)) {
            return true;
        } else {
            return false;
        }
    }

    public void outputDevice(Device device, OutputStream outputStream) throws IOException {
        if (device != null) {
            if (outputStream == null) {
                IllegalArgumentException e = new IllegalArgumentException("The output stream can not be null");
                LOGGER.log(Level.SEVERE, "The output stream has specified as null", e);
                throw e;
            } else if (!isValidDeviceForOutputToStream(device)) {
                DeviceValidationException e = new DeviceValidationException("DeviceService.outputDevice");
                LOGGER.log(Level.SEVERE, "The device is not valid for output to stream", e);
                throw e;
            } else {
                DataOutput dataOutput = new DataOutputStream(outputStream);

                dataOutput.writeUTF(device.getClass().getName());

                dataOutput.writeInt(device.getIn());

                if (device.getType() != null) {
                    dataOutput.writeUTF(device.getType());
                } else {
                    dataOutput.writeUTF(LINE_SEPARATOR);
                }

                if (device.getModel() != null) {
                    dataOutput.writeUTF(device.getModel());
                } else {
                    dataOutput.writeUTF(LINE_SEPARATOR);
                }

                if (device.getManufacturer() != null) {
                    dataOutput.writeUTF(device.getManufacturer());
                } else {
                    dataOutput.writeUTF(LINE_SEPARATOR);
                }

                if (device.getProductionDate() != null) {
                    dataOutput.writeLong(device.getProductionDate().getTime());
                } else {
                    dataOutput.writeLong(-1);
                }

                if (device.getClass().equals(Battery.class)) {
                    Battery battery = (Battery)device;
                    dataOutput.writeInt(battery.getChargeVolume());
                } else if (device.getClass().equals(Router.class)) {
                    Router router = (Router) device;
                    dataOutput.writeInt(router.getDataRate());
                } else if (device.getClass().equals(Switch.class)) {
                    Switch aSwitch = (Switch) device;
                    dataOutput.writeInt(aSwitch.getDataRate());
                    dataOutput.writeInt(aSwitch.getNumberOfPorts());
                } else if (device.getClass().equals(WifiRouter.class)) {
                    WifiRouter wifiRouter = (WifiRouter) device;
                    dataOutput.writeInt(wifiRouter.getDataRate());
                    if (wifiRouter.getSecurityProtocol() != null) {
                        dataOutput.writeUTF(wifiRouter.getSecurityProtocol());
                    } else {
                        dataOutput.writeUTF(LINE_SEPARATOR);
                    }
                }
            }
        }
    }

    public Device inputDevice(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("The input stream can not be null");
            LOGGER.log(Level.SEVERE, "The input stream has specified as null", e);
            throw e;
        }

        DataInput dataInput = new DataInputStream(inputStream);

        try {
            String className = dataInput.readUTF();
            if (className.equals(LINE_SEPARATOR)) {
                return null;
            }

            Class clazz = Class.forName(className);

            if (!Device.class.isAssignableFrom(clazz)) {
                ClassCastException e = new ClassCastException("Class should implement interface Device");
                LOGGER.log(Level.SEVERE, "Class that is not a device was got", e);
                throw e;
            }

            if (Battery.class.equals(clazz)) {

                Battery battery = new Battery();
                inputCommonProperties(battery, inputStream);
                battery.setChargeVolume(dataInput.readInt());
                return battery;

            } else if (Router.class.equals(clazz)) {

                Router router = new Router();
                inputCommonProperties(router, inputStream);
                router.setDataRate(dataInput.readInt());
                return router;

            } else if (Switch.class.equals(clazz)) {

                Switch aSwitch = new Switch();
                inputCommonProperties(aSwitch, inputStream);
                aSwitch.setDataRate(dataInput.readInt());
                aSwitch.setNumberOfPorts(dataInput.readInt());
                return aSwitch;

            } else if (WifiRouter.class.equals(clazz)) {

                WifiRouter wifiRouter = new WifiRouter();
                inputCommonProperties(wifiRouter, inputStream);
                wifiRouter.setDataRate(dataInput.readInt());
                String securityProtocol = dataInput.readUTF();
                if (!securityProtocol.equals(LINE_SEPARATOR)) {
                    wifiRouter.setSecurityProtocol(securityProtocol);
                }
                return wifiRouter;

            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Class not found", e);
            throw e;
        }

        return null;
    }

    public void writeDevice(Device device, Writer writer) throws IOException {
        if (device != null) {
            if (writer == null) {
                IllegalArgumentException e = new IllegalArgumentException("The writer can not be null");
                LOGGER.log(Level.SEVERE, "The writer has specified as null", e);
                throw e;
            } else if (!isValidDeviceForOutputToStream(device) ||
                       !isValidDeviceForWriteToStream(device)) {
                DeviceValidationException e = new DeviceValidationException("DeviceService.writeDevice");
                LOGGER.log(Level.SEVERE, "The device is not valid for write to stream", e);
                throw e;
            } else {
                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                bufferedWriter.write(device.getClass().getName() + "\n");
                bufferedWriter.write(device.getIn() + " |");

                if (device.getType() != null) {
                    bufferedWriter.write(" " + device.getType() + " |");
                } else {
                    bufferedWriter.write(" |");
                }

                if (device.getModel() != null) {
                    bufferedWriter.write(" " + device.getModel() + " |");
                } else {
                    bufferedWriter.write(" |");
                }

                if (device.getManufacturer() != null) {
                    bufferedWriter.write(" " + device.getManufacturer() + " |");
                } else {
                    bufferedWriter.write(" |");
                }

                if (device.getProductionDate() != null) {
                    bufferedWriter.write(" " + device.getProductionDate().getTime() + " |");
                } else {
                    bufferedWriter.write(" " + -1 + " |");
                }

                if (device.getClass().equals(Battery.class)) {
                    Battery battery = (Battery) device;
                    bufferedWriter.write(" " + battery.getChargeVolume() + " |");
                } else if (device.getClass().equals(Router.class)) {
                    Router router = (Router) device;
                    bufferedWriter.write(" " + router.getDataRate() + " |");
                } else if (device.getClass().equals(Switch.class)) {
                    Switch aSwitch = (Switch) device;
                    bufferedWriter.write(" " + aSwitch.getDataRate() + " |");
                    bufferedWriter.write(" " + aSwitch.getNumberOfPorts() + " |");
                } else if (device.getClass().equals(WifiRouter.class)) {
                    WifiRouter wifiRouter = (WifiRouter) device;
                    bufferedWriter.write(" " + wifiRouter.getDataRate() + " |");
                    if (wifiRouter.getSecurityProtocol() != null) {
                        bufferedWriter.write(" " + wifiRouter.getSecurityProtocol() + " |");
                    } else {
                        bufferedWriter.write(" |");
                    }
                }

                bufferedWriter.write("\n");
            }
        }
    }

    public Device readDevice(Reader reader) throws IOException, ClassNotFoundException {
        if (reader == null) {
            IllegalArgumentException e = new IllegalArgumentException("The reader can not be null");
            LOGGER.log(Level.SEVERE, "The reader has specified as null", e);
            throw e;
        }

        StreamTokenizer stringTokenizer = new StreamTokenizer(reader);

        try {
            String className = stringTokenizer.
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Class not found", e);
            throw e;
        }
    }

    public boolean isValidDeviceForOutputToStream(Device device) {
        return isValidDeviceForStream(device, "\n");
    }

    public boolean isValidDeviceForWriteToStream(Device device) {
        return isValidDeviceForStream(device, "|");
    }

    private boolean isValidDeviceForStream(Device device, String symbol) {
        if (device == null) {
            return false;
        }

        if (device.getType() != null && device.getType().contains(symbol) ||
                device.getManufacturer() != null && device.getManufacturer().contains(symbol) ||
                device.getModel() != null && device.getModel().contains(symbol)) {
            return false;
        }

        if (device.getClass().equals(WifiRouter.class)) {
            WifiRouter wifiRouter = (WifiRouter) device;
            if (wifiRouter.getSecurityProtocol() != null &&
                    wifiRouter.getSecurityProtocol().contains(symbol)) {
                return false;
            }
        }
        return true;
    }

    private void inputCommonProperties (Device device, InputStream inputStream) throws IOException {
        DataInput dataInput = new DataInputStream(inputStream);

        device.setIn(dataInput.readInt());
        String type = dataInput.readUTF();
        if (!type.equals(LINE_SEPARATOR)) {
            device.setType(type);
        }
        String model = dataInput.readUTF();
        if (!model.equals(LINE_SEPARATOR)) {
            device.setModel(model);
        }
        String manufacture = dataInput.readUTF();
        if (!manufacture.equals(LINE_SEPARATOR)) {
            device.setManufacturer(manufacture);
        }
        long prodDate = dataInput.readLong();
        if (prodDate != -1) {
            device.setProductionDate(new Date(prodDate));
        }
    }
}
