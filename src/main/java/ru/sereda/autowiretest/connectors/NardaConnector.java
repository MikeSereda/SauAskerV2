package ru.sereda.autowiretest.connectors;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class NardaConnector implements Connector{

    private TelnetClient telnetClient = new TelnetClient();
    private final String ip;
    private final int port;
    private String protocol;
    private final String connectorType = "Narda";

    private final Map<String, Integer> spectrumConfig = new HashMap<>();

    public NardaConnector(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    private String sendCommand(String command){
        try {
            telnetClient.connect(ip,port);
            InputStream in = telnetClient.getInputStream();
            PrintStream out = new PrintStream(telnetClient.getOutputStream());
            StringBuilder sb = new StringBuilder();
            out.println(command+"\r");
            out.flush();
            char ch = (char) in.read();
            while (ch!=';'){
                if (ch == '\r') sb.append(System.lineSeparator());
                else sb.append(ch);
                ch = (char) in.read();
            }
            sb.append(ch);
            telnetClient.disconnect();
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> getAvgValues(){
        Map<String,Object> valuesMap = new HashMap<>();
        String response = sendCommand("SPECTRUM_TRACE?"+'\r'+"1,AVG;");
        String[] strings = response.replace(System.lineSeparator(),"").split(",");
        valuesMap.put("fStart", Integer.parseInt(strings[4]));
        float[] avg = new float[strings.length-11];
        for (int k=10;k<strings.length-1;k++){
            avg[k-10]=Float.parseFloat(strings[k]);
        }
        valuesMap.put("avgValues", avg);
        valuesMap.putAll(spectrumConfig);
        return valuesMap;
    }

    public Map<String,Object> getSpectrumConfig(){
        Map<String,Object> valuesMap = new HashMap<>();
        String response = sendCommand("SPECTRUM_CONFIG?;");
        String[] strings = response.replace(System.lineSeparator(),"").split(",");
        valuesMap.put("fCent", Integer.parseInt(strings[0]));
        valuesMap.put("fSpan", Integer.parseInt(strings[1]));
        valuesMap.put("rbw", Integer.parseInt(strings[2]));
        valuesMap.put("vbw", Integer.parseInt(strings[4]));
        spectrumConfig.put("fCent", Integer.parseInt(strings[0]));
        spectrumConfig.put("fSpan", Integer.parseInt(strings[1]));
        spectrumConfig.put("rbw", Integer.parseInt(strings[2]));
        spectrumConfig.put("vbw", Integer.parseInt(strings[4]));
        return valuesMap;
    }

    @Override
    public Map<String, Object> getValues() {
        Map<String,Object> valuesMap;
        boolean reachable = isReachable();
        if (reachable){
            valuesMap = getAvgValues();
            valuesMap.put("reachable",true);
        }
        else{
            valuesMap = new HashMap<>();
            valuesMap.put("reachable",false);
        }
        return valuesMap;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public boolean isReachable() {
        try {
            return InetAddress.getByName(ip).isReachable(3000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getConnectorType() {
        return null;
    }

    @Override
    public String sendActionCommand(String changingParam, Object value, Object confirmingValue) {
        return null;
    }

    @Override
    public String sendActionCommand(String changingParam, Object value) {
        return null;
    }
}
