package ru.sereda.autowiretest.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.sereda.autowiretest.connectors.Connector;

import java.util.Map;

public class Device {
    private String id;
    private String ip;
    private int port;
    private String description;

    private final String location;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelnetDevAddress() {
        return telnetDevAddress;
    }

    private String telnetDevAddress;

    private Connector connector;

    private String login1;
    private String password1;
    private String login2;
    private String password2;
    private String login3;
    private String password3;
    private float exEbNo = 0;
    private float exEbNoRemote = 0;
    public Device(String id, String ip, int port, String description, String location) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.description = description;
        this.location = location;
    }

    public Connector getConnector() {
        return connector;
    }

    @JsonIgnore
    public Map<String,Object> getValues(){
        Map<String,Object> values = connector.getValues();
        if (values.containsKey("eb_no")){
            float exEbNoNew = (float)values.get("eb_no");
            values.put("eb_no_delta",exEbNo-exEbNoNew);
            exEbNo = exEbNoNew;
        }
        if (values.containsKey("eb_no_remote")){
            float exEbNoRemoteNew = (float)values.get("eb_no_remote");
            values.put("eb_no_remote_delta",exEbNoRemote-exEbNoRemoteNew);
            exEbNoRemote=exEbNoRemoteNew;
        }
        values.put("deviceProtocol",this.connector.getProtocol());
        values.put("connectorType",this.connector.getConnectorType());
        return values;
    }

    @JsonIgnore
    public boolean isReachabe(){
        return connector.isReachable();
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTelnetDevAddress(String telnetDevAddress) {
        this.telnetDevAddress = telnetDevAddress;
    }

    public String getLocation() {
        return location;
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

    public String getLogin3() {
        return login3;
    }

    public void setLogin3(String login3) {
        this.login3 = login3;
    }

    public String getPassword3() {
        return password3;
    }

    public void setPassword3(String password3) {
        this.password3 = password3;
    }
}
