package ru.sereda.autowiretest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.sereda.autowiretest.DAO.DeviceDAO;
import ru.sereda.autowiretest.DAO.TaskDAO;
import ru.sereda.autowiretest.DTO.DeviceTimerTaskDTO;
import ru.sereda.autowiretest.logic.DeviceTimerTask;

import java.util.*;

@Service
public class TaskService {
    @Autowired
    TaskDAO taskDAO;

    @Autowired
    DeviceDAO deviceDAO;

    @Autowired
    ApplicationContext context;

    public static Timer mainTaskTimer = new Timer(true);

    private Map<String, DeviceTimerTask> taskMap = new HashMap<>();


    @Value("${management.endpoint.integrationgraph.enabled}")
    private String integrationgraph;

    @Value("${jwt.token.expired}")
    private String expired;

    public void fillTasks(){
        for (DeviceTimerTask task : taskDAO.getTasks().values()){
            taskMap.put(task.getName(),task);
            mainTaskTimer.schedule(task, 0, task.getInterval());
        }


//        System.out.println(integrationgraph);
//        System.out.println(expired);
    }

    public Map<String, String> reloadTasks(){
        System.out.println("reloading tasks...");
        Map<String,String> resultMap = new HashMap<>();
        for (DeviceTimerTask task : taskMap.values()){
            task.cancel();
        }
        taskMap.clear();
        mainTaskTimer.purge();
        fillTasks();
        return resultMap;
    }

    public Map<String, DeviceTimerTask> getTasks(){
        return taskDAO.getTasks();
    }

    public DeviceTimerTask addTask(DeviceTimerTaskDTO taskDTO){
        if (!getTasks().containsKey(taskDTO.getTaskId())){
            DeviceTimerTask task = taskDAO.addTask(taskDTO);
            if (task!=null){
                mainTaskTimer.schedule(task, 0, task.getInterval());
                taskMap.put(task.getName(),task);
            }
            return task;
        }
        else return null;
    }

    public Optional<DeviceTimerTask> getTaskOptional(String taskId){
        Optional<DeviceTimerTask> taskOptional = null;
        if (taskDAO.isContains(taskId)){
            taskOptional = Optional.ofNullable(taskDAO.getTask(taskId));
        }
        return taskOptional;
    }

    public boolean removeTask(String taskId){
        boolean isRemoved = taskDAO.removeTask(taskId);
        if (isRemoved){
            taskMap.get(taskId).cancel();
        }
        mainTaskTimer.purge();
        return isRemoved;
    }

    public int removeTasksWhereDevice(String deviceId){
        Set<String> taskIds = tasksWhereDevice(deviceId).keySet();
        for (String taskId : taskIds){
            removeTask(taskId);
        }
        return taskIds.size();
    }

    public Map<String, DeviceTimerTask> tasksWhereDevice(String deviceId){
        Map<String, DeviceTimerTask> selectedTasks = new HashMap<>();
        for (String taskId : getTasks().keySet()){
            if (taskDAO.getTask(taskId).getDevice().getId().equals(deviceId)){
                selectedTasks.put(taskId,taskDAO.getTask(taskId));
            }
        }
        return selectedTasks;
    }

    public void goAwake(String taskId){
        Optional<DeviceTimerTask> taskOptional = getTaskOptional(taskId);
        if (taskOptional.isPresent()){
            taskOptional.ifPresent(DeviceTimerTask::goAwake);
        }
        else System.out.println("task "+taskId+" is not presented");
    }

    public void goSleep(String taskId){
        Optional<DeviceTimerTask> taskOptional = getTaskOptional(taskId);
        if (taskOptional.isPresent()){
            taskOptional.ifPresent(DeviceTimerTask::goSleep);
        }
        else System.out.println("task "+taskId+" is not presented");
    }

    private List<String> taskIdsWhereDevice(String deviceId){
        List<String> taskIds = new ArrayList<>();
        for (String taskId : taskDAO.getTasks().keySet()){
            if (taskDAO.getTask(taskId).getDevice().getId().equals(deviceId)){
                taskIds.add(taskId);
            }
        }
        return taskIds;
    }
}