package org.pizzeria.api.repos.user.projections;

public interface UserProjection {

	Long getId();

	String getName();

	String getEmail();

	Integer getContactNumber();
}