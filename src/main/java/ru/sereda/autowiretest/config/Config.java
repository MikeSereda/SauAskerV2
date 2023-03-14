package ru.sereda.autowiretest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"ru.sereda.autowiretest"})
@PropertySource(value = {"classpath:application.yml"})
public class Config {

}
