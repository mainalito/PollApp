package com.pollApp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pollApp.model.Poll;
import com.pollApp.repo.Repository;


@Service
public class Services {
	@Autowired
	Repository repo;

	public List<Poll> getPOlls(){
		List<Poll> polls = new ArrayList<>();
		repo.findAll().forEach(polls::add);
		return polls;
	}
	public void save(Poll poll) {
		repo.save(poll);
	}
	public void delete(Long id) {
		repo.deleteById(id);
	}
	
	
}
