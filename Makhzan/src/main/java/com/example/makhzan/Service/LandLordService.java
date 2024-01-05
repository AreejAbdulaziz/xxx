package com.example.makhzan.Service;

import com.example.makhzan.Api.ApiException;
import com.example.makhzan.DTO.LandlordDTO;
import com.example.makhzan.Model.Landlord;
import com.example.makhzan.Model.Orders;
import com.example.makhzan.Model.Storage;
import com.example.makhzan.Model.User;
import com.example.makhzan.Repository.LandLordRepository;
import com.example.makhzan.Repository.OrdersRepository;
import com.example.makhzan.Repository.StorageRepository;
import com.example.makhzan.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LandLordService {
    private final LandLordRepository landLordRepository;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final StorageRepository storageRepository;
    private final StorageService storageService;

    public List<Landlord> getAllLandlords(){
        return landLordRepository.findAll();
    }
    public void register(LandlordDTO landlordDTO){
        User user=new User(null,landlordDTO.getPassword(),landlordDTO.getName(),landlordDTO.getEmail(),landlordDTO.getPhoneNumber(),landlordDTO.getRole(),null,null); //r
        user.setRole("LANDLORD");
        String hash=new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(hash);
        userRepository.save(user);

        Landlord landlord=new Landlord(null, "pending",landlordDTO.getIsCompany(),landlordDTO.getLicense(),user,null);
        landLordRepository.save(landlord);
    }

    public void updateLandlord(LandlordDTO landlordDTO ,Integer id) {
        Landlord landlord = landLordRepository.findLandLordById(id);
        if (landlord == null) {
            throw new ApiException("Landlord Not Found");
        }
        landlord.setName(landlordDTO.getName());
        landlord.setEmail(landlordDTO.getEmail());
        landlord.setPhoneNumber(landlordDTO.getPhoneNumber());
        landlord.setIsCompany(landlordDTO.getIsCompany());
        landlord.setLicense(landlordDTO.getLicense());
        String hash=new BCryptPasswordEncoder().encode(landlordDTO.getPassword());
        landlord.setPassword(hash);

        landLordRepository.save(landlord);
    }

    public void deleteLandlord(Integer id){
        Landlord landlord = landLordRepository.findLandLordById(id);
        if (landlord == null) {
            throw new ApiException("Landlord Not Found");
        }
        User user=userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User Not Found");
        }
        user.setLandlord(null);
        userRepository.save(user);
        for(Storage s:storageRepository.findStorageByLandlord(landlord)){
            s.setLandlord(null);
            s.setOrders(null);
            s.setMedias(null);
            storageRepository.save(s);
            //            s.deleteorder احذفي الستورج استدعي الداله
        }
        landlord.setUser(null);
        user.setLandlord(null);
        userRepository.save(user);
        landLordRepository.save(landlord);
        userRepository.delete(user);
        landLordRepository.delete(landlord);
    }
    ///
    public void acceptOrder(Integer landlordId, Integer orderId) {
        Orders orders = ordersRepository.findOrdersByLandlordIdAndOrderId(landlordId, orderId);
        if (orders == null) {
            throw new ApiException("Order Not Found");
        }
        if(orders.getStatus().equals("ACCEPTED")){
            throw new ApiException("You Have Already Accepted This Order!");
        }
        if(orders.getStorage().equals(false)){
            throw new ApiException("This Storage Already Rented");
        }
        orders.getStorage().setAvailable(false);
        orders.setStatus("ACCEPTED");
        ordersRepository.save(orders);
    }

    //Reject
    public void rejectOrder(Integer landlordId, Integer orderId) {
        Orders orders = ordersRepository.findOrdersByLandlordIdAndOrderId(landlordId, orderId);
        if (orders == null) {
            throw new ApiException("Order Not Found");
        }
        orders.setStatus("REJECT");
        ordersRepository.save(orders);
    }
}
