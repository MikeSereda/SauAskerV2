package ru.sereda.autowiretest.communications.telnet;

import org.apache.commons.net.telnet.*;
import ru.sereda.autowiretest.AutowiretestApplication;

import java.io.InputStream;
import java.io.PrintStream;

public class AutoTelnetClient {

    public static boolean offlinemode = AutowiretestApplication.offlinemode;
    private TelnetClient telnet = new TelnetClient();
    private InputStream in;
    private PrintStream out;
    private String prompt = "-->";
//    boolean offlineMode = V2Application.offlineMode;

    public AutoTelnetClient(String server, int port) {
        if (!offlinemode){
            try {
                // Connect to the specified server
                telnet.connect(server, port);
                TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);
                EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);
                SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);
                try {
                    telnet.addOptionHandler(ttopt);
                    telnet.addOptionHandler(echoopt);
                    telnet.addOptionHandler(gaopt);
                } catch (InvalidTelnetOptionException e) {
                    System.err.println("Error registering option handlers: " + e.getMessage());
                }
                in = telnet.getInputStream();
                out = new PrintStream(telnet.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            boolean found = false;
            char ch = (char) in.read();
            while (true) {
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(String value) {
        if (!offlinemode){
            try {
                out.println(value);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String sendCommand(String command) {
        try {
            write(command);
            return readUntil(prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String readAll(){
        try {
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (!(sb.toString().endsWith("-->")||sb.toString().endsWith("name:")||sb.toString().endsWith("password:"))){
                sb.append(ch);
                ch = (char) in.read();
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readAll(String lastCommand){
        if (!offlinemode){
            try {
                StringBuffer sb = new StringBuffer();
                while (!(sb.toString().endsWith(lastCommand))){
                    char ch = (char) in.read();
                    sb.append(ch);
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String readAll(String[] breakers){
        try {
            StringBuffer sb = new StringBuffer();
            do {
                char ch = (char) in.read();
                sb.append(ch);
                for (String breaker : breakers){
                    if (sb.toString().endsWith(breaker)){
                        return sb.toString();
                    }
                }
            }
            while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
