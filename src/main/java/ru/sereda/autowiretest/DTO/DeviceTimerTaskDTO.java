package ru.sereda.autowiretest.DTO;


public class DeviceTimerTaskDTO {
    private String taskId;
    private boolean sleep;
    private int interval;
    private String deviceId;

    public DeviceTimerTaskDTO(String taskId, boolean sleep, int interval, String deviceId) {
        this.taskId = taskId;
        this.sleep = sleep;
        this.interval = interval;
        this.deviceId = deviceId;
    }


    public String getTaskId() {
        return taskId;
    }

    public boolean isSleep() {
        return sleep;
    }

    public int getInterval() {
        return interval;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setSleep(boolean sleep) {
        this.sleep = sleep;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
