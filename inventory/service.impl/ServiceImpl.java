package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.service.DeviceService;
import com.netcracker.edu.inventory.service.RackService;
import com.netcracker.edu.inventory.service.Service;

import java.util.Comparator;

/**
 * Created by Herman Bocharov on 22.03.2017.
 */
public class ServiceImpl implements Service {

    @Deprecated
    public void sortByIN(Device[] devices) {
        DeviceServiceImpl sortIN = new DeviceServiceImpl();
        sortIN.sortByIN(devices);
    }

    @Deprecated
    public void sortByProductionDate(Device[] devices) {
        DeviceServiceImpl sortPD = new DeviceServiceImpl();
        sortPD.sortByProductionDate(devices);
    }

    @Deprecated
    public void filtrateByType(Device[] devices, String type) {
        DeviceServiceImpl filterType = new DeviceServiceImpl();
        filterType.filtrateByType(devices, type);
    }

    @Deprecated
    public void filtrateByManufacturer(Device[] devices, String manufacturer) {
        DeviceServiceImpl filterManufacturer = new DeviceServiceImpl();
        filterManufacturer.filtrateByManufacturer(devices, manufacturer);
    }

    @Deprecated
    public void filtrateByModel(Device[] devices, String model) {
        DeviceServiceImpl filterModel = new DeviceServiceImpl();
        filterModel.filtrateByModel(devices, model);
    }

    @Deprecated
    public boolean isValidDeviceForInsertToRack(Device device) {
        DeviceServiceImpl validDevice = new DeviceServiceImpl();
        if (validDevice.isValidDeviceForInsertToRack(device)) {
            return true;
        } else {
            return false;
        }
    }

    public DeviceService getDeviceService() {
        DeviceServiceImpl deviceImpl = new DeviceServiceImpl();
        return deviceImpl;
    }

    public RackService getRackService() {
        RackServiceImpl rackImpl = new RackServiceImpl();
        return rackImpl;
    }
}
