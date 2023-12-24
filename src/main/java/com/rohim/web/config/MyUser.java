package com.rohim.web.config;

import java.util.Objects;

public class MyUser {
	private String username;
	private boolean expired;
	private boolean locked;

	public MyUser(String username, boolean expired, boolean locked) {
		this.username = username;
		this.expired = expired;
		this.locked = locked;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MyUser myUser)) return false;
		return Objects.equals(username, myUser.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	public String getUsername() {
		return username;
	}

	public boolean isExpired() {
		return expired;
	}

	public boolean isLocked() {
		return locked;
	}
}
