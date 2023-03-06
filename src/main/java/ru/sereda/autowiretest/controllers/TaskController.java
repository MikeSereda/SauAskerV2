package ru.sereda.autowiretest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sereda.autowiretest.DTO.DeviceTimerTaskDTO;
import ru.sereda.autowiretest.logic.DeviceTimerTask;
import ru.sereda.autowiretest.services.TaskService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/asker")
public class TaskController{
    @Autowired
    TaskService taskService;

    @GetMapping("/gosleep")
    public void goSleeptASK(@RequestParam(name = "taskId", defaultValue = "") String taskId){
        taskService.goSleep(taskId);
    }

    @GetMapping("/goawake")
    public void goAwakeTask(@RequestParam(name = "taskId", defaultValue = "") String taskId){
        taskService.goAwake(taskId);
    }

    @GetMapping("/tasks")
    public Map<String, DeviceTimerTask> getTasks(){
        return taskService.getTasks();
    }

    @GetMapping("/tasks_reload")
    public Map<String, String> reloadTasks(){
        return taskService.reloadTasks();
    }

    @GetMapping("/task")
    public DeviceTimerTask getTask(@RequestParam(name = "taskId", defaultValue = "") String taskId){
        return taskService.getTaskOptional(taskId).orElse(null);
    }

    @PostMapping("/addTask")
    public DeviceTimerTask addTask(@RequestBody DeviceTimerTaskDTO task){
        return taskService.addTask(task);
    }

    @DeleteMapping("/task")
    public ResponseEntity<DeviceTimerTask> removeTask(@RequestParam(name = "taskId", defaultValue = "") String taskId){
        if (taskService.removeTask(taskId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
