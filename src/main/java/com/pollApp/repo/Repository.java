package com.pollApp.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.pollApp.model.Poll;

public interface Repository extends CrudRepository<Poll, Long> {
 Optional<Poll> findById(Long id);
 

}
