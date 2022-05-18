package com.pollApp.controller;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.pollApp.model.Poll;
import com.pollApp.model.Results;
import com.pollApp.repo.Repository;
import com.pollApp.repo.ResRepo;

import javax.servlet.http.HttpServletRequest;

@Controller

public class Controllers {

	private static final String[] IP_HEADER_CANDIDATES = {
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP",
			"HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED",
			"HTTP_VIA",
			"REMOTE_ADDR"
	};
	public static String getClientIpAddressIfServletRequestExist() {

		if (RequestContextHolder.getRequestAttributes() == null) {
			return "0.0.0.0";
		}

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		for (String header: IP_HEADER_CANDIDATES) {
			String ipList = request.getHeader(header);
			if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
				String ip = ipList.split(",")[0];
				return ip;
			}
		}

		return request.getRemoteAddr();
	}


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
	public ModelAndView poll(@PathVariable("id") long id, @ModelAttribute("results") Results rst, HttpServletRequest request) throws Exception {

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
		String clientIP= "";
		String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
		if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
		Assert.isTrue(ip.chars().filter($ -> $ == '.').count() == 3, "Illegal IP: " + ip);
		System.out.println("IP ADDRESS IS " + ip);

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

	@PostMapping("/createPoll")
	public ModelAndView sendPoll(@ModelAttribute("polls") Poll poll) {
		serv.save(poll);

		return new ModelAndView("redirect:/");
	}

	@GetMapping("/fetchPoll/{id}")
	public String showVotersResults(@PathVariable("id") long id, Model model) {

		Poll polls = repo.findById(id).get();
		List<Results> result = ro.fetchByPid(id);

		// calculate total points
		int total_points = ro.findByPid(id).stream().map(Results::getPoints).reduce(0, Integer::sum);

		// find the winner with the highest points
		int highest_points = ro.findByPid(id).stream().map(Results::getPoints).mapToInt(v -> v).max()
				.orElseThrow(NoSuchElementException::new);
		List<Results> highest = ro.fetchHighestPoints(highest_points, id);

		model.addAttribute("highestPoints", highest);


		model.addAttribute("result", result);
		model.addAttribute("polls", polls);
		model.addAttribute("totalPoints", total_points);

		return "result";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id) {
		serv.delete(id);
		return "redirect:/";
	}

}
