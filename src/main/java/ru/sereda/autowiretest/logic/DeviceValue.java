package ru.sereda.autowiretest.logic;

public class DeviceValue {
    ValueType type;
    String stringValue;
    int intValue;
    float floatValue;
    boolean booleanValue;

    public DeviceValue(String stringValue){
        this.stringValue = stringValue;
        this.type = ValueType.STRING;
    }

    public DeviceValue(int intValue) {
        this.intValue = intValue;
        this.type = ValueType.INTEGER;
    }

    public DeviceValue(float floatValue) {
        this.floatValue = floatValue;
        this.type = ValueType.FLOAT;
    }

    public DeviceValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
        this.type = ValueType.BOOLEAN;
    }

    @Override
    public String toString() {
        switch (type) {
            case STRING -> {return stringValue;}
            case INTEGER -> {return String.valueOf(intValue);}
            case FLOAT -> {return String.valueOf(floatValue);}
            case BOOLEAN -> {return String.valueOf(booleanValue);}
            default -> {return null;}
        }
    }
}
