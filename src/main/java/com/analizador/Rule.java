package com.analizador;

public class Rule {

	private int id;
	private String name;
	private String description;
	private boolean pass;
	private String reason;

	public Rule() {
		pass = true;
		reason="Not Apply";
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name + ", description=" + description + ", pass=" + pass + ", reason="
				+ reason + "]";
	}
	
	
	
}
