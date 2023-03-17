package ru.sereda.autowiretest.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.sereda.autowiretest.DAO.mappers.DeviceTimerTaskRowMapper;
import ru.sereda.autowiretest.DTO.DeviceTimerTaskDTO;
import ru.sereda.autowiretest.logic.DeviceTimerTask;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TaskDAO {

    @Autowired
    DeviceDAO deviceDAO;
    @Autowired
    ApplicationContext context;
    @Autowired
    JdbcTemplate jdbcTemplate;
//    boolean nodbMode = (boolean) AutowiretestApplication.argsMap.get("nodbmode");

    private DeviceTimerTask taskCreator(DeviceTimerTaskDTO taskDTO){
        DeviceTimerTask task = context.getBean(DeviceTimerTask.class);
        task.setName(taskDTO.getTaskId());
        task.setInterval(taskDTO.getInterval());
        task.setDevice(deviceDAO.getDevice(taskDTO.getDeviceId()));
        if (taskDTO.isSleep()){
            task.goSleep();
        }
        return task;
    }

    public Map<String, DeviceTimerTask> getTasks(){
        Map<String, DeviceTimerTask> taskMap = new HashMap<>();
        String sql="SELECT * FROM tasks";
        for (DeviceTimerTaskDTO task : jdbcTemplate.query(sql, new DeviceTimerTaskRowMapper())){
            taskMap.put(task.getTaskId(), taskCreator(task));
        }
        return taskMap;
    }

    public boolean isContains(String taskId){
        String sql = "SELECT count(*) FROM public.tasks WHERE task_id=? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, taskId)==1;
    }

    public DeviceTimerTask getTask(String taskId){
        String sql = "SELECT * FROM public.tasks WHERE task_id=? LIMIT 1";
        DeviceTimerTaskDTO taskDTO = jdbcTemplate.queryForObject(sql, new DeviceTimerTaskRowMapper(),taskId);
        return taskCreator(taskDTO);
    }

    public DeviceTimerTask addTask(DeviceTimerTaskDTO taskDTO){
        DeviceTimerTask task = taskCreator(taskDTO);
        String sql = "INSERT INTO public.tasks (task_id, device_id, interval, sleep) VALUES (?, ?, ?, ?);";
        int result = jdbcTemplate.update(sql, taskDTO.getTaskId(), taskDTO.getDeviceId(), taskDTO.getInterval(), taskDTO.isSleep());
        if (result==1){
            return task;
        }
        return null;
    }

    public boolean updateTask(DeviceTimerTask task){
        String sql = "UPDATE public.tasks SET \"interval\"=?, sleep=? WHERE task_id=?;";
        int result = jdbcTemplate.update(sql,task.getInterval(), task.isSleep(), task.getName());
        return result == 1;
    }

    public boolean removeTask(String taskId){
        String sql = "DELETE FROM public.tasks WHERE task_id=?";
        int result = jdbcTemplate.update(sql,taskId);
        return result>0;
    }
}
