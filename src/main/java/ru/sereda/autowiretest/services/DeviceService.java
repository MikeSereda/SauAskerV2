package ru.sereda.autowiretest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sereda.autowiretest.DAO.DeviceDAO;
import ru.sereda.autowiretest.DTO.DeviceDTO;
import ru.sereda.autowiretest.connectors.CDM570LHttpConnector;
import ru.sereda.autowiretest.connectors.CDM570LTelnetConnector;
import ru.sereda.autowiretest.connectors.Connector;
import ru.sereda.autowiretest.logic.Device;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    DeviceDAO deviceDAO;

    @Autowired
    TaskService taskService;

    public Map<String, Device> getDevices(){
        return deviceDAO.getDevices();
    }

    public Device getDevice(String id){
        return deviceDAO.getDevice(id);
    }

    public Optional<Device> getDeviceOptional(String deviceId){
        Optional<Device> deviceOptional;
        if (deviceDAO.isContains(deviceId)){
            deviceOptional = Optional.ofNullable(deviceDAO.getDevice(deviceId));
        }
        else deviceOptional = Optional.empty();
        if (deviceOptional.isEmpty()){
            System.out.println("device  "+deviceId+" is not presented");
        }
        return deviceOptional;
    }

    public Device addDevice(DeviceDTO deviceDTO){
        Device device = new Device(deviceDTO.getId(), deviceDTO.getIp(), deviceDTO.getPort(),
                deviceDTO.getDescription(), deviceDTO.getLocation());
        device.setName(deviceDTO.getName());
        Connector connector = null;
        device.setTelnetDevAddress(deviceDTO.getDevAddress());
        if (deviceDTO.getProtocol().equals("telnet")){
            connector = new CDM570LTelnetConnector(deviceDTO.getIp(), deviceDTO.getPort(), deviceDTO.getDevAddress(),
                    deviceDTO.getLogin1(), deviceDTO.getPassword1(), deviceDTO.getLogin2(), deviceDTO.getPassword2());
        }
        if (deviceDTO.getProtocol().equals("http")){
            connector = new CDM570LHttpConnector(deviceDTO.getIp(), deviceDTO.getPort());
        }
        device.setConnector(connector);

        Device result = null;
        if (!deviceDAO.isContains(device.getId())){
            result = deviceDAO.addDevice(device);
        }
        return result;
    }

    public boolean removeDevice(String deviceId){
        taskService.removeTasksWhereDevice(deviceId);
        return deviceDAO.removeDevice(deviceId);
    }
}
