package com.netcracker.edu.inventory.exception;

import com.netcracker.edu.inventory.model.Device;

/**
 * Created by Herman Bocharov on 27.03.2017.
 */
public class DeviceValidationException extends RuntimeException {
    private Device object;
    private String operation;

    public DeviceValidationException() {
        super();
    }

    public DeviceValidationException(String operation) {
        super(operation != null ? "Device is not valid for operation " + operation : "Device is not valid for operation");
    }

    public DeviceValidationException(String operation, Device object) {
        super(operation != null ? "Device is not valid for operation " + operation : "Device is not valid for operation");

        this.object = object;
    }

    public Device getObject() {
        return object;
    }
}
