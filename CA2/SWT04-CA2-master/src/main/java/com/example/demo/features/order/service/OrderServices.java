package com.example.demo.features.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.features.order.model.Orders;
import com.example.demo.features.order.repository.OrderRepository;
import com.example.demo.features.user.model.User;
@Component
public class OrderServices
{
	@Autowired
	private OrderRepository orderRepository;
	public List<Orders> getOrders()
	{
		List<Orders> list=this.orderRepository.findAll();
		return list;
	}
	public void saveOrder(Orders order)
	{
		this.orderRepository.save(order);
	}

	public void updateOrder(int id,Orders order)
	{
		order.setoId(id);
		this.orderRepository.save(order);
		 
	}

	public void deleteOrder(int id)
	{
		this.orderRepository.deleteById(id);
	}

	public List<Orders> getOrdersForUser(User user)
	{
	 return  this.orderRepository.findOrdersByUser(user);
	}

	public double calculateTotalForUser(User user)
	{
		List<Orders> orders = getOrdersForUser(user);
		if (orders == null)
		{
			return 0.0;
		}
		double total = 0.0;
		for (Orders order : orders)
		{
			if (order == null)
			{
				continue;
			}
			double lineTotal = order.getTotalAmmout();
			if (lineTotal <= 0)
			{
				lineTotal = order.getoPrice() * order.getoQuantity();
			}
			total += lineTotal;
		}
		return total;
	}
	
}
