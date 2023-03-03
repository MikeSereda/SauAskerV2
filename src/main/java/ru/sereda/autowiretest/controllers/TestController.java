package ru.sereda.autowiretest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import ru.sereda.autowiretest.logic.InformatedException;
import ru.sereda.autowiretest.services.DeviceService;
import ru.sereda.autowiretest.services.ExceptionService;
import ru.sereda.autowiretest.services.TaskService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@RestController
public class TestController {
    @Autowired
    ApplicationContext context;
    @Autowired
    TaskService taskService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    ExceptionService exceptionService;

    @GetMapping("/exceptions")
    public Map<LocalDateTime, InformatedException> getExceptions(@RequestParam(name = "onlyUnwatched",required = false,defaultValue = "true") boolean onlyUnwatched){
        return exceptionService.getExceptionMap(onlyUnwatched);
    }

    @GetMapping("/exceptions_list")
    public Set<LocalDateTime> getExceptionsEvents(@RequestParam(name = "onlyUnwatched",required = false,defaultValue = "false") boolean onlyUnwatched){
        return exceptionService.exceptionsEvents(onlyUnwatched);
    }
}
