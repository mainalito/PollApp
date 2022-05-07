package com.pollApp.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;



@Entity(name="poll")
public class Poll {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "poll_id")
	private long pollId;
	@Column(columnDefinition = "varchar(255)")
	private String title;
	@Column(columnDefinition = "varchar(255)")
	private String option1;
	@Column(columnDefinition = "varchar(255)")
	private String option2;
	@Column(columnDefinition = "varchar(255)")
	private String option3;

	
	public Poll( String title, String option1, String option2, String option3) {
		this.title = title;
		this.option1 = option1;
		this.option2 = option2;
		this.option3 = option3;
	}

	public long getId() {
		return pollId;
	}
	public void setId(long pollId) {
		this.pollId = pollId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOption1() {
		return option1;
	}
	public void setOption1(String option1) {
		this.option1 = option1;
	}
	public String getOption2() {
		return option2;
	}
	public void setOption2(String option2) {
		this.option2 = option2;
	}
	public String getOption3() {
		return option3;
	}
	public void setOption3(String option3) {
		this.option3 = option3;
	}
	public Poll() {
		super();
	}

	
	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", title='" + getTitle() + "'" +
			", option1='" + getOption1() + "'" +
			", option2='" + getOption2() + "'" +
			", option3='" + getOption3() + "'" +
			"}";
	}
	
	
	
}
