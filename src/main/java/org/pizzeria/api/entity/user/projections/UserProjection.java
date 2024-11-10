package org.pizzeria.api.entity.user.projections;

public interface UserProjection {

	Long getId();

	String getName();

	String getEmail();

	Integer getContactNumber();
}