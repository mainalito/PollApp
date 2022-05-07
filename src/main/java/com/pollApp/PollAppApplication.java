package com.pollApp;

import javax.annotation.PostConstruct;

import com.pollApp.controller.Services;
import com.pollApp.model.Poll;
import com.pollApp.repo.ResRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PollAppApplication {
	@Autowired
	Services service;

	public static void main(String[] args) {
		SpringApplication.run(PollAppApplication.class, args);
	}

	@PostConstruct
	public void addQuestions() {
		service.save(new Poll("Best OS", "WINDOWS", "KALI LINUX", "MAC 0S"));
		service.save(new Poll("Best Anime", "Jujutsu Kaisen", "Overlord", "Black clover"));
	}
}
