package ru.sereda.autowiretest.services;

import org.springframework.stereotype.Service;
import ru.sereda.autowiretest.logic.InformatedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ExceptionService {
    private Map<LocalDateTime, InformatedException> exceptionMap = new HashMap<>();

    public Map<LocalDateTime, InformatedException> getExceptionMap(boolean onlyUnwatched) {
        Map<LocalDateTime, InformatedException> outputMap;
        if (onlyUnwatched){
            outputMap = new HashMap<>();
            for (InformatedException exception : exceptionMap.values()){
                if (!exception.isWatched()){
                    outputMap.put(exception.getLocalDateTime(), exception);
                }
            }
        }
        else outputMap = exceptionMap;
        return outputMap;
    }

    public Set<LocalDateTime> exceptionsEvents(boolean onlyUnwatched){
        Set<LocalDateTime> outputSet;
        if (onlyUnwatched){
            outputSet = new TreeSet<>();
            for (InformatedException exception : exceptionMap.values()){
                if (!exception.isWatched()){
                    outputSet.add(exception.getLocalDateTime());
                }
            }
        }
        else outputSet = exceptionMap.keySet();
        return outputSet;
    }

    public void addException(LocalDateTime time, InformatedException exception){
        exceptionMap.put(time,exception);
    }
}
