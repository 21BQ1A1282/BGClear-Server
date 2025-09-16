package com.sai.BGClear.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.sai.BGClear.dto.RazorpayOrderDTO;
import com.sai.BGClear.response.ClearBgResponse;
import com.sai.BGClear.service.OrderService;
import com.sai.BGClear.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final RazorpayService razorpayService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestParam String planId, Authentication authentication) throws RazorpayException {
        Map<String, Object> responseMap = new HashMap<>();
        ClearBgResponse response = null;

        // validation for authentication
        if(authentication.getName().isEmpty() || authentication.getName() == null){
            response = ClearBgResponse.builder()
                    .statusCode(HttpStatus.FORBIDDEN)
                    .success(false)
                    .data("You are not authorized to access this resource")
                    .build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try{
            Order order = orderService.createOrder(planId, authentication.getName());
            RazorpayOrderDTO responseDTO = converToDTO(order);
            response = ClearBgResponse.builder()
                    .statusCode(HttpStatus.OK)
                    .success(true)
                    .data(responseDTO)
                    .build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response = ClearBgResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .data("Error while creating order: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private RazorpayOrderDTO converToDTO(Order order){
        return RazorpayOrderDTO.builder()
                .id(order.get("id"))
                .entity(order.get("entity"))
                .amount(order.get("amount"))
                .currency(order.get("currency"))
                .receipt(order.get("receipt"))
                .status(order.get("status"))
                .created_at(order.get("created_at"))
                .build();
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOrder(@RequestBody Map<String, Object> requestBody) throws RazorpayException {
        try{
            String razorpayOrderId = requestBody.get("razorpay_order_id").toString();
            Map<String, Object> verifyResponse = razorpayService.verifyPayment(razorpayOrderId);
            return ResponseEntity.ok(verifyResponse);
        }catch (RazorpayException e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error while verifying payment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
