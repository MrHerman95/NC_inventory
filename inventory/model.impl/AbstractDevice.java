package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Herman Bocharov on 22.03.2017.
 */
abstract class AbstractDevice implements Device {
    static protected Logger LOGGER = Logger.getLogger(AbstractDevice.class.getName());

    protected int in;
    protected String type;
    protected String manufacturer;
    protected String model;
    protected Date productionDate;

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        if (in < 0) {
            IllegalArgumentException e = new IllegalArgumentException("IN can not be negative");
            LOGGER.log(Level.SEVERE, "The negative inventory number has specified", e);
            throw e;
        } else if (this.in != 0) {
            LOGGER.log(Level.WARNING, "Inventory number can not be reset");
        } else {
            this.in = in;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }
}
