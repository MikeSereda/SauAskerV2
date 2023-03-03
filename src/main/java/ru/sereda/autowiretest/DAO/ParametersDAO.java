package ru.sereda.autowiretest.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.sereda.autowiretest.AutowiretestApplication;

import java.util.Map;

@Repository
public class ParametersDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

//    boolean nodbMode = (boolean) AutowiretestApplication.argsMap.get("nodbmode");

    public void saveParams(String string){
        System.out.println(string);
    }
    public void saveParamsCDM570L(Map<String, Object> valuesMap){
//        if (!nodbMode){
            if (valuesMap.containsKey("reachable")&&(boolean)valuesMap.get("reachable")){
                String sql = "INSERT INTO public.cdm570params(" +
                        "timestamp_wotz, device_id, eb_no," +
                        "eb_no_remote, ber, power_level," +
                        "power_level_increase, rsl, faults," +
                        "temperature, freq_offset, reachable, " +
                        "carrier, test, framing, " +
                        "delay) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                jdbcTemplate.update(sql,
                        valuesMap.get("timestamp"), valuesMap.get("deviceId"), valuesMap.get("eb_no"),
                        valuesMap.get("eb_no_remote"), valuesMap.get("ber"), valuesMap.get("tx_power_level"),
                        valuesMap.get("power_level_increase"), valuesMap.get("rsl"), valuesMap.get("faults"),
                        valuesMap.get("temperature"), valuesMap.get("freq_offset"), valuesMap.get("reachable"),
                        valuesMap.get("carrier"), valuesMap.get("test_mode"), valuesMap.get("framing"),
                        valuesMap.get("askDelay"));
            }
            else {
                String sql = "INSERT INTO public.cdm570params(" +
                        "timestamp_wotz, device_id, delay) " +
                        "VALUES (?, ?, ?);";
                jdbcTemplate.update(sql,
                        valuesMap.get("timestamp"), valuesMap.get("deviceId"), valuesMap.get("askDelay"));
            }
//        }
//        else System.out.print("nodbmode: ");
        System.out.println(valuesMap);
    }
}