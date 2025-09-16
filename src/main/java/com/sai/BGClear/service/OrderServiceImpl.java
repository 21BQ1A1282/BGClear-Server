package com.sai.BGClear.service;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.sai.BGClear.entity.OrderEntity;
import com.sai.BGClear.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final RazorpayService razorpayService;
    private final OrderRepository orderRepository;

    private static final Map<String, PlanDetails> plans = Map.of(
            "Basic", new PlanDetails("Starter Membership", 100, 499),
            "Premium", new PlanDetails("Premium Membership", 250, 899),
            "Ultimate", new PlanDetails("Ultimate Membership", 1000, 1499)
    );

    private record PlanDetails(String name, int credits, double amount){

    }

    @Override
    public Order createOrder(String planId, String clerkId) throws RazorpayException {
        PlanDetails details = plans.get(planId);
        if (details == null) {
            throw new IllegalArgumentException("Invalid plan ID: " + planId);
        }

        try{
            Order razorpayOrder = razorpayService.createOrder(details.amount(), "INR");

            OrderEntity newOrder = OrderEntity.builder()
                    .orderId(razorpayOrder.get("id"))
                    .clerkId(clerkId)
                    .plan(details.name())
                    .amount(details.amount())
                    .credits(details.credits())
                    .build();
            orderRepository.save(newOrder);
            return razorpayOrder;
        }catch (RazorpayException e){
            throw new RazorpayException("Error while creating order: " + e.getMessage());
        }
    }
}
