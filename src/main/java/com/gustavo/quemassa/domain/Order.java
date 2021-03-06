package com.gustavo.quemassa.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Order_Table")
public class Order implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String clientName;
	
	@OneToMany(mappedBy = "order")
	private List<Meal> meals = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "order")
	private List<DrinkOrder> drinkOrder = new ArrayList<>();
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date startTime;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date endTime;
	
	public Order() {
	}
	
	public Order(Integer id, String clientName) {
		super();
		this.id = id;
		this.clientName = clientName;
		this.startTime = new Date(System.currentTimeMillis());
		this.endTime = generateEndTime() ;
	}
	
	private Date generateEndTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		calendar.add(Calendar.MINUTE, 15);
		return calendar.getTime();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public List<DrinkOrder> getDrinkOrders() {
		return drinkOrder;
	}

	public void setDrinkOrders(List<DrinkOrder> drinkOrder) {
		this.drinkOrder = drinkOrder;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Double getTotalPrice() {		
		
		Double totalPrice = 0.0;
		
		for(Meal meal: meals) {
			totalPrice += meal.getPasta() != null ? meal.getPasta().getPrice() : 0;
			totalPrice += meal.getSauce() != null ? meal.getSauce().getPrice() : 0;
			totalPrice += meal.getTopping() != null ? meal.getTopping().getPrice(): 0;
			
			for(Ingredient ingredient: meal.getIngredients()) {
				totalPrice += ingredient.getPrice();
			}
		}
		
		for(DrinkOrder drink: drinkOrder) {
			totalPrice += drink.getDrink().getPrice()*drink.getQuantity();
			
		}
		
		return totalPrice;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(id, other.id);
	}
}
