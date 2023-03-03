package ru.sereda.autowiretest.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.sereda.autowiretest.services.ExceptionService;
import ru.sereda.autowiretest.services.ParametersService;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Scope("prototype")
@Configurable
public class DeviceTimerTask extends TimerTask {
    @Autowired
    private ParametersService parametersService;

    @Autowired
    ApplicationContext context;

    @Autowired
    ExceptionService exceptionService;

    private String name;
    private boolean sleep = false;

    private Long interval;

    private Device device;

    private boolean crashed = false;

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public DeviceTimerTask(){}

    public String getDeviceId() {
        return device.getId();
    }

    public String getName() {
        return name;
    }

//    private int count=1;

    public void goSleep(){
        if (!sleep){
            sleep=true;
        }
    }

    public void goAwake() {
        if (sleep) {
            sleep = false;
        }
    }

    @Override
    public void run() {
        try {
            executingBody();
        }
        catch (Exception exception){
            LocalDateTime exceptionTime = LocalDateTime.now();
            System.out.println("exception caught at "+ exceptionTime);
            exceptionService.addException(exceptionTime,new InformatedException(name,exception, exceptionTime));
            this.crashed = true;
        }
    }

    public void executingBody(){
        if (!sleep){
            long lastQueryUptime = System.currentTimeMillis();
            Map<String,Object> valuesMap;
            if (device.getConnector()!=null){
                valuesMap = device.getValues();

            }
            else {
                valuesMap = new HashMap<>();
                System.out.println(name + " connector is null");
            }
            valuesMap.put("deviceId",device.getId());
            valuesMap.put("askDelay", System.currentTimeMillis()-lastQueryUptime);
            valuesMap.put("timestamp", LocalDateTime.now());
            valuesMap.put("task_id",name);
            parametersService.saveParams(valuesMap);
        }
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public boolean isSleep() {
        return sleep;
    }

    public boolean isCrashed() {
        return crashed;
    }

    //    public void setCrashed(boolean crashed) {
//        this.crashed = crashed;
//    }

    //    @PostConstruct
//    void postConstruct(){
//        System.out.println("postConstruct");
//    }
//    @Autowired
//    public DeviceTimerTask(String name) {
//        this.name = name;
////        this.parametersService = parametersService;
//    }
}
