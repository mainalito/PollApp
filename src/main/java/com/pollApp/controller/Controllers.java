package com.pollApp.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.MapUtils;

import com.fasterxml.classmate.types.ResolvedRecursiveType;
import com.pollApp.model.Poll;
import com.pollApp.model.Results;
import com.pollApp.repo.Repository;
import com.pollApp.repo.ResRepo;

@Controller
public class Controllers {
	@Autowired
	Repository repo;
	@Autowired
	Services serv;
	@Autowired
	ResRepo ro;

	@GetMapping("/addPoll") // retrieve a form
	public String showForm(Model model) {
		Poll poll = new Poll();

		model.addAttribute("polls", poll);
		return "add-poll";
	}

	@PostMapping("/addPoll")
	public String addPoll(@ModelAttribute("polls") Poll poll) {
		serv.save(poll);
		return "redirect:/";
	}

	@PostMapping("/poll/{id}/vote")
	public ModelAndView poll(@PathVariable("id") long id, @ModelAttribute("results") Results rst) throws Exception {

		Optional<Poll> poll = repo.findById(id);

		if (poll.isPresent()) {
			rst.setPoll(poll.get());

			rst.setPid(poll.get().getId());
			List<Results> results = ro.findByChosenAndPid(rst.getChosen(), rst.getPoll().getId());
			if (CollectionUtils.isEmpty(results)) {
				rst.setPoints(1);
				ro.save(rst);
			} else {
				ro.incrementVotes(rst.getChosen(), rst.getPoll().getId());
			}
		}
	
		return new ModelAndView("redirect:/addPoll/" + id);
	}

	@GetMapping("/results")
	public String results(Model model) {

		return "result";
	}

	@GetMapping("/")
	public String showPoll(Model model) {
		model.addAttribute("pollsFetched", serv.getPOlls());
		model.addAttribute("result", new Poll());
		return "index";
	}

	@GetMapping("/addPoll/{id}")
	public String showVoters(@PathVariable("id") long id, Model model) {
		Poll poll = repo.findById(id).orElseThrow();
		model.addAttribute("pollsFetched", poll);
		model.addAttribute("polls", poll);
		model.addAttribute("results", new Results());
		return "poll-options";
	}

	@PostMapping("/addResult")
	public ModelAndView sendPoll(@ModelAttribute("polls") Poll poll) {
		serv.save(poll);
		

		return new ModelAndView("redirect:/");
	}

	@GetMapping("/fetchPoll/{id}")
	public String showVotersResults(@PathVariable("id") long id, Model model) {

		
		Poll polls = repo.findById(id).get();
		List<Results> result = ro.fetchByPid(id);
		
		//calculate total points
		int total_points = ro.findByPid(id).stream().map(Results::getPoints).reduce(0,(a,b) -> a +b);

		//find the winner with the highest points
		int highest_points = ro.findByPid(id).stream().map(Results::getPoints).mapToInt(v -> v).max().orElseThrow(NoSuchElementException::new);
		Results highest = ro.fetchHighestPoints(highest_points, id);

		//check if there is a draw
		
		// int points = ro.fetchPointsforChosen(result.getChosen(), result.getPoll().getId());
		// System.out.println(points + " " + result.getChosen() +" " + result.getPoll().getId());
		System.out.println("Total points for "+ polls.getTitle() + " ====== "+ total_points);
		model.addAttribute("result", result);
		model.addAttribute("polls", polls);
		model.addAttribute("totalPoints", total_points);
		model.addAttribute("highestPoints", highest);
		return "result";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id) {
		serv.delete(id);
		return "redirect:/";
	}

}
