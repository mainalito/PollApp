package com.pollApp.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity(name = "results")
public class Results {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(columnDefinition = "varchar(255)")
	private String chosen;
	@Column(columnDefinition = "integer ") 
	private int points;

	private Long pid;

	
	@ManyToOne(
        cascade = CascadeType.ALL
    )
    @JoinColumn(
        name= "poll_id"
    )
	private Poll poll;

	

	public Results() {
	}

	public Poll getPoll() {
		return this.poll;
	}

	public Results(Long id, String chosen, int points, Long pid, Poll poll) {
		this.id = id;
		this.chosen = chosen;
		this.points = points;
		this.pid = pid;
		this.poll = poll;
	}


	public Long getPid() {
		return this.pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}
	

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChosen() {
		return chosen;
	}

	public void setChosen(String chosen) {
		this.chosen = chosen;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "Results [id=" + id + ", chosen=" + chosen + ", points=" + points + "]";
	}


}
