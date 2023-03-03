package ru.sereda.autowiretest.logic;

import java.time.LocalDateTime;

public class InformatedException extends Exception{
    private final Exception exception;
    private boolean watched = false;
    private final String name;
    private final LocalDateTime localDateTime;

    public InformatedException(String name, Exception exception, LocalDateTime localDateTime){
        this.exception = exception;
        this.name = name;
        this.localDateTime = localDateTime;
    }

    public Exception getException() {
        watched = true;
        return exception;
    }

    public String getName() {
        watched = true;
        return name;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}
