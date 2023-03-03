package ru.sereda.autowiretest.connectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.sereda.autowiretest.AutowiretestApplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class CDM570LHttpConnector implements Connector{
    private final String ip;
    private final int port;
    private String protocol;

    private boolean offlinemode = AutowiretestApplication.offlinemode;

    private final String connectorType = "CDM570L";

    public CDM570LHttpConnector(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.protocol="http";
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getValues() {
        System.out.println("trying to ask device "+ip+":"+port);
        return askDevice();
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public boolean isReachable() {
        try {
            return InetAddress.getByName(ip).isReachable(3000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getConnectorType() {
        return connectorType;
    }

    @Override
    public String sendActionCommand(String changingParam, Object value, Object confirmingValue) {
        return null;
    }

    @Override
    public String sendActionCommand(String changingParam, Object value) {
        return null;
    }

    private Map<String,Object> askDevice(){
        // TODO: 24.12.2022 remake valuesMap to Map<String,DeviceValue>

//        Map<String, DeviceValue> map = new HashMap<>();
//        map.put("eb_no",new DeviceValue(7.5f));
//        map.put("temperature",new DeviceValue(23));
//        map.put("Alarms",new DeviceValue("None"));
//        map.put("active",new DeviceValue(true));
//        System.out.println(map);
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put("eb_no",7.5f);
        valuesMap.put("temperature",24);
        valuesMap.put("Alarms","None");
        valuesMap.put("reachable",isReachable());
        return valuesMap;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
