package ru.sereda.autowiretest.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.sereda.autowiretest.DAO.mappers.DeviceRowMapper;
import ru.sereda.autowiretest.logic.Device;

import java.util.*;

@Repository
public class DeviceDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

//    boolean nodbMode = (boolean) AutowiretestApplication.argsMap.get("nodbmode");

    public Map<String, Device> getDevices(){
        Map<String,Device> deviceMap = new HashMap<>();
        String sql = "SELECT * FROM public.devices ORDER BY id ASC";
        for (Device device : jdbcTemplate.query(sql,new DeviceRowMapper())){
            deviceMap.put(device.getId(), device);
        }
        return deviceMap;
    }

    public Device getDevice(String deviceId){
        String sql = "SELECT * FROM public.devices WHERE id=? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, new DeviceRowMapper(), deviceId);
    }

    public boolean isContains(String deviceId){
        String sql = "SELECT count(*) FROM public.devices WHERE id=? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, deviceId)==1;
    }

    public Device addDevice(Device device){
        String sql = "INSERT INTO public.devices" +
                "(id, ip, port, name, device_type, protocol, dev_address, description, location, login, password, login_alter, password_alter) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        int result = jdbcTemplate.update(sql,device.getId(),device.getIp(),device.getPort(),device.getName(),device.getConnector().getConnectorType(),
                device.getConnector().getProtocol(),device.getTelnetDevAddress(), device.getDescription(), device.getLocation(), device.getLogin1(),
                device.getPassword1(), device.getLogin2(), device.getPassword2());
        if (result>0){
            return device;
        }
        else return null;
    }

    public boolean removeDevice(String deviceId){
        String sql = "DELETE FROM public.devices WHERE id=?";
        int result = jdbcTemplate.update(sql,deviceId);
        System.out.println(result);
        return result>0;
    }
}
