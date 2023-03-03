package ru.sereda.autowiretest.connectors;

import java.util.Map;

public interface Connector {
    Map<String, Object> getValues();

    String getProtocol();

    boolean isReachable();

    String getConnectorType();

    String sendActionCommand(String changingParam, Object value, Object confirmingValue);

    String sendActionCommand(String changingParam, Object value);
}
