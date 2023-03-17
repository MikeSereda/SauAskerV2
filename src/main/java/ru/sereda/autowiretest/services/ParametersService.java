package ru.sereda.autowiretest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sereda.autowiretest.DAO.ParametersDAO;

import java.util.Map;

@Service
public class ParametersService {

    @Autowired
    ParametersDAO parametersDAO;

    public void saveParams(Map<String, Object> valuesMap){
        if (valuesMap.get("connectorType").equals("CDM570L")){
            parametersDAO.saveParamsCDM570L(valuesMap);
        } else if (valuesMap.get("connectorType").equals("Narda")) {
            parametersDAO.saveParametersNarda(valuesMap);
        } else System.out.println("unable to save params "+valuesMap);
    }
}
