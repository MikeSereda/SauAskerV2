package ru.sereda.autowiretest.DTO;

public class DeviceDTO {
    private String id;//
    private String ip;//
    private int port;//
    private String protocol;//

    private String description;//
    private String name;//
    private String devAddress;
    private String deviceType;

    private String location;
    private String login1;
    private String password1;
    private String login2;
    private String password2;
    public DeviceDTO(String id, String ip, int port, String protocol,
                     String description, String name, String devAddress,
                     String deviceType, String location, String login1,
                     String password1, String login2, String password2) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.protocol = protocol;
        this.description = description;
        this.name = name;
        this.devAddress = devAddress;
        this.deviceType = deviceType;
        this.location = location;
        this.login1 = login1;
        this.password1 = password1;
        this.login2 = login2;
        this.password2 = password2;
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevAddress() {
        return devAddress;
    }

    public void setDevAddress(String devAddress) {
        this.devAddress = devAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogin1() {
        return login1;
    }

    public void setLogin1(String login1) {
        this.login1 = login1;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getLogin2() {
        return login2;
    }

    public void setLogin2(String login2) {
        this.login2 = login2;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
