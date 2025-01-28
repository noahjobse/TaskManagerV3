package com.example.TaskManagerV3;

import com.example.TaskManagerV3.menu.Menu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
//Main file
@SpringBootApplication
public class TaskManagementApp {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TaskManagementApp.class, args);
		Menu menu = context.getBean(Menu.class);
		menu.start();
	}
}