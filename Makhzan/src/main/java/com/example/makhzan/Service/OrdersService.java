package com.example.makhzan.Service;

import com.example.makhzan.Model.Landlord;
import com.example.makhzan.Model.Orders;
import com.example.makhzan.Model.User;
import com.example.makhzan.Repository.LandLordRepository;
import com.example.makhzan.Repository.OrdersRepository;
import com.example.makhzan.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final LandLordRepository landLordRepository;


}
