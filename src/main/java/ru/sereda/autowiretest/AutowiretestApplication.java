package ru.sereda.autowiretest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.sereda.autowiretest.services.DeviceService;
import ru.sereda.autowiretest.services.TaskService;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"ru.sereda.autowiretest"})
@EnableConfigurationProperties
public class AutowiretestApplication implements CommandLineRunner {
	@Autowired
	DeviceService deviceService;
	@Autowired
	TaskService taskService;

	public static Map<String, Object> argsMap = new HashMap<>();

	public static boolean offlinemode=false;

	@Value("${management.endpoint.integrationgraph.enabled}")
	private String integrationgraph;

	@Value("${jwt.token.expired}")
	private String expired;

	public static void main(String[] args) {
		argsPare(args);

		SpringApplication.run(AutowiretestApplication.class, args);
	}

	@Override
	public void run(String... args){
		System.out.println(integrationgraph);
		System.out.println(expired);
		taskService.fillTasks();
	}

	private static void argsPare(String... args){
		argsMap.put("offlinemode", false);
		argsMap.put("nodbmode", false);
		for (String arg: args){
			System.out.println(arg);
			if (arg.startsWith("offlinemode=")){
				offlinemode=Boolean.parseBoolean(arg.split("=")[1]);
				argsMap.put("offlinemode", offlinemode);
			}
			if (arg.startsWith("nodbmode=")){
				argsMap.put("nodbmode", Boolean.parseBoolean(arg.split("=")[1]));
			}
		}
	}
}
