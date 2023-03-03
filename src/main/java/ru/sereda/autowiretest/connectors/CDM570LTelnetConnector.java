package ru.sereda.autowiretest.connectors;

import ch.qos.logback.core.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;
import ru.sereda.autowiretest.AutowiretestApplication;
import ru.sereda.autowiretest.communications.telnet.AutoTelnetClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class CDM570LTelnetConnector implements TelnetConnector{
    private String devAddress;
    private final String ip;
    private final int port;
    private String protocol;
    private String login1;
    private String password1;
    private String login2;
    private String password2;

    private boolean offlinemode = AutowiretestApplication.offlinemode;

    private final String connectorType = "CDM570L";


    private AutoTelnetClient telnetClient;

//    public int testCount=0;

    public CDM570LTelnetConnector(String ip, int port, String devAddress, String login1, String password1, String login2, String password2) {
        this.ip = ip;
        this.port = port;
        this.protocol="telnet";
        this.devAddress = devAddress;
        if (!((port>0) && (port<65536))){
            port=23;
        }
        if (!offlinemode){
            telnetClient = new AutoTelnetClient(ip, port);
        }
        this.login1 = login1;
        this.password1 = password1;
        this.login2 = login2;
        this.password2 = password2;
    }



    @Override
    @JsonIgnore
    public Map<String, Object> getValues() {
//        testCount++;
        Map<String,Object> valuesMap;
        boolean reachable = isReachable();
        if (reachable){
            valuesMap = interpreteValues(askDevice());
            valuesMap.put("reachable",true);
        }
        else{
            valuesMap = new HashMap<>();
            valuesMap.put("reachable",false);
        }
//        if (testCount>10 && ip.equals("192.168.100.111")){
//            testCount=-5;
//            System.out.println("Достигнуто");
//            System.out.println(10/0);
//        }
        return valuesMap;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public boolean isReachable(){
        try {
            return InetAddress.getByName(ip).isReachable(3000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> askDevice(){
        List<String> responses = new ArrayList<>();
        if (!offlinemode){
            for (String query:commandList){
                responses.add(askDevice("<"+devAddress+"/"+query));
            }
        }
        else {
            responses.add(">0020ansFLT=001001");
            responses.add(">0020ansTMP=+29");
            responses.add(">0020ansRFO=-007.2");
            responses.add(">0020ansrsl=lt79");
        }

        return responses;
    }

    private static Map<String,Object> interpreteValues(List<String> responses){
        Map<String,Object> values = new HashMap<>();
        for (String response: responses){
            String prefix = response.substring(8,11).toUpperCase();
            String value = response.substring(12).split("\r\n")[0];
            switch (prefix) {
                case "FLT" -> {
                    values.put("faults", value);
                    break;
                }
                case "TMP" -> {
                    values.put("temperature", Integer.valueOf(value));
                    break;
                }
                case "RFO" -> {
                    values.put("freq_offset", Float.valueOf(value));
                    break;
                }
                case "RSL" -> {
                    values.put("rsl", Integer.valueOf(value.substring(2)));
                    break;
                }
                case "EBN" -> {
                    values.put("eb_no", Float.valueOf(value));
                    break;
                }
                case "BER" -> {
                    if (!value.contains("99999")) {
                        values.put("ber", Integer.valueOf(value.substring(4)));
                    } else {
                        values.put("ber", Integer.valueOf(value));
                    }
                    break;
                }
                case "TST" -> {
                    values.put("test_mode", Integer.valueOf(value));
                    break;
                }
                case "FRM" -> {
                    if (value.contains("0")) {
                        values.put("framing", "Unframed");
                    } else if (value.contains("1")) {
                        values.put("framing", "EDMAC");
                    } else if (value.contains("2")) {
                        values.put("framing", "EDMAC-2");
                    } else {
                        values.put("framing", "unknown");
                    }
                    break;
                }
                case "TXO" -> {
                    if (value.contains("0")) {
                        values.put("carrier", "Off");
                    } else if (value.contains("1")) {
                        values.put("carrier", "On");
                    } else if (value.contains("2")) {
                        values.put("carrier", "RTI");
                    } else {
                        values.put("carrier", "unknown");
                    }
                    break;
                }
                case "PLI" -> {
                    if (!value.contains("x")) {
                        values.put("power_level_increase", Float.valueOf(value));
                    } else {
                        values.put("power_level_increase", Float.valueOf("99.9"));
                    }
                    break;
                }
                case "REB" -> {
                    if (!value.contains("x")) {
                        values.put("eb_no_remote", Float.valueOf(value));
                    } else {
                        values.put("eb_no_remote", Float.valueOf("88.8"));
                    }
                    break;
                }
                case "TPL" -> {
                    values.put("tx_power_level", Float.valueOf(value));
                    break;
                }
                default -> {
                    System.out.println("!!!!!!!default!!!!!!!");
                    break;
                }
            }
        }
        return values;
    }

    byte auth(String login, String password) {
        telnetClient.write(login);
        /*String loginResponse = */ telnetClient.readAll(new String[]{login,"->"});
        String loginResponse2 = telnetClient.readAll(":");
        if (!loginResponse2.contains("password:")){
            return 1; //Wrong login
        }
        else {
            telnetClient.write(password);
            String passwordResponse = telnetClient.readAll(new String[]{":","->"});
            if (!passwordResponse.contains("-->")){
                return 2; //Wrong password
            }
            return 0;
        }
    }

    public boolean auth() {
        switch (auth(login1, password1)){
            case 0 : return true;
            case 1 : {
                if (auth(login2, password2) == 0) {
                    return true;
                }
                System.out.println("Disconnect!");
                return false;
            }
            default: {
                System.out.println("Disconnect!");
                return false;
            }
        }
    }

    private String askDevice(String query){
        telnetClient.write(query);
        String response = telnetClient.readAll(query);
        if (response.contains("name:")){
            if (!auth()) {
                return "Auth error!!!";
            }
            telnetClient.write(query);
            /*String loginResponse =*/ telnetClient.readAll(query);
        }
        return telnetClient.readAll("-->");
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    private static final String[] commands = new String[] {
            "FLT?",//flt=abcdef, a - unit, b - tx traffic, c - rx traffic, d - odu
            "TMP?",//temperature tmp=+26 || tmp=-06
            "RFO?",//freq offset, rfo=+999.9 if unlocked, rfo=-123.4, rfo=+123.4
            "RSL?",//rsl=LT99 || rsl===41
            "EBN?",//rx ebno, ebn=12.3 || ebn=+016 || ebn=99.9 is demod unlocked
            "BER?",//ber=4.8e3 || ber=99999
            "TST?",//unit test mode, tst=1 normal, 1 if, 2 dig, 3 io, 4 rf, 5 tx cw, 6 tx alt
            "FRM?", //framing mode, frm=0 unframed, 1-edmac, 2-edmac-2
            "TXO?",//TX CARRIER state, 0 off, 1 on, 2 rti, 5+ rti
            "PLI?",//power level increase, pli=2.3, pli=x.x if aupc disabled
            "REB?",//remote ebn, reb=12.4, reb=99.9 if unlocked, reb=xx.x if disabled
            "TPL?"//tx power level, tpl=13.4
    };

    private final List<String> commandList = new ArrayList<>(Arrays.stream(commands).toList());

    public String getConnectorType() {
        return connectorType;
    }

    @Override
    public String sendActionCommand(String changingParam, Object value, Object confirmingValue) {
        return null;
    }

    @Override
    public String sendActionCommand(String changingParam, Object value) {
        return null;
    }

    public boolean actionCarrier(byte state){
        if (state==0){
            return sendCommand("carrier","off");
        }
        if (state==1){
            return sendCommand("carrier","on");
        }
        if (state==2){
            return sendCommand("carrier","rti");
        }
        return false;
    }

    public boolean actionPowerLevel(float power){
        return sendCommand("power_level",power);
    }

    private boolean sendCommand(String command, Object value){
        if (command.equals("carrier")){
            if (value == "on") {
                return true;
            }
            if (value == "off") {
                return true;
            }
            if (value == "rti") {
                return true;
            }
        }
        if (command.equals("power_level")){
            System.out.println(ip+" Power level changed to "+value);
            return true;
        }
        return false;
    }
}
