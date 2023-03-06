package ru.sereda.autowiretest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sereda.autowiretest.DTO.DeviceDTO;
import ru.sereda.autowiretest.logic.Device;
import ru.sereda.autowiretest.services.DeviceService;
import ru.sereda.autowiretest.services.TaskService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/asker")
public class DevicesController {
    @Autowired
    DeviceService deviceService;

    @Autowired
    TaskService taskService;

    @GetMapping("/devices")
    public Map<String, Device> getDevices(){
        return deviceService.getDevices();
    }

    @GetMapping("/device")
    public Device getDevice(@RequestParam(name = "deviceId", defaultValue = "") String deviceId){
        return deviceService.getDeviceOptional(deviceId).orElse(null);
    }

    @PostMapping("/addDevice")
    public Device addDevice(@RequestBody DeviceDTO deviceDTO){
        return deviceService.addDevice(deviceDTO);
    }

    @DeleteMapping("/device")
    public ResponseEntity<Device> removeDevice(@RequestParam(name = "deviceId", defaultValue = "") String deviceId){
        if (deviceService.removeDevice(deviceId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/device_tasks/{deviceId}")
    public ResponseEntity<String> removeTasksOfDevice(@RequestParam(name = "deviceId", defaultValue = "") String deviceId){
        int removedCount = taskService.removeTasksWhereDevice(deviceId);
        if (removedCount>0){
            return new ResponseEntity<>("removed "+removedCount+" tasks",HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
