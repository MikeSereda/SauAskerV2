package ru.sereda.autowiretest.DAO.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.sereda.autowiretest.DTO.DeviceTimerTaskDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceTimerTaskRowMapper  implements RowMapper<DeviceTimerTaskDTO> {
    @Override
    public DeviceTimerTaskDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        String taskId = rs.getString("task_id");
        String deviceId = rs.getString("device_id");
        long interval = rs.getInt("interval");
        boolean sleep = rs.getBoolean("sleep");
        return new DeviceTimerTaskDTO(taskId,sleep,interval,deviceId);
    }
}