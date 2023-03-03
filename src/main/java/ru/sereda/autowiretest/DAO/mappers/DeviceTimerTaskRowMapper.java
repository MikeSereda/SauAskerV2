package ru.sereda.autowiretest.DAO.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import ru.sereda.autowiretest.DTO.DeviceTimerTaskDTO;
import ru.sereda.autowiretest.logic.DeviceTimerTask;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceTimerTaskRowMapper  implements RowMapper<DeviceTimerTaskDTO> {
    @Override
    public DeviceTimerTaskDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        String taskId = rs.getString("task_id");
        String deviceId = rs.getString("device_id");
        int interval = rs.getInt("interval");
        boolean sleep = rs.getBoolean("sleep");
        DeviceTimerTaskDTO task = new DeviceTimerTaskDTO(taskId,sleep,interval,deviceId);
        return task;
    }
}