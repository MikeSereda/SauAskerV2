package ru.sereda.autowiretest.DAO.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.sereda.autowiretest.connectors.CDM570LHttpConnector;
import ru.sereda.autowiretest.connectors.CDM570LTelnetConnector;
import ru.sereda.autowiretest.logic.Device;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceRowMapper implements RowMapper<Device> {
    @Override
    public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");//
        String ip = rs.getString("ip");//
        int port = rs.getInt("port");//
        String name = rs.getString("name");//
        String connectorType = rs.getString("device_type");//
        String protocol = rs.getString("protocol");//
        String devAddress = rs.getString("dev_address");//
        String description = rs.getString("description");//
        String location = rs.getString("location");
        String login1 = rs.getString("login");
        String password1 = rs.getString("password");
        String login2 = rs.getString("login_alter");
        String password2 = rs.getString("password_alter");
        Device device = new Device(id,ip,port,description, location);
        device.setName(name);
        device.setTelnetDevAddress(devAddress);
        device.setLogin1(login1);
        device.setPassword1(password1);
        device.setLogin2(login2);
        device.setPassword2(password2);
        if (protocol.equals("telnet")){
            device.setConnector(new CDM570LTelnetConnector(ip,port,devAddress, login1, password1, login2, password2));
        }
        else{
            device.setConnector(new CDM570LHttpConnector(ip,port));
        }
        return device;
    }
}
