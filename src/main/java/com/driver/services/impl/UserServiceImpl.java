package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
//        Country country=countryRepository3.findCountryHavingcountryName(countryName);
////        if(country==null)
////            throw new Exception("");
//
//        User user=new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setConnected(false);
//        user.setOriginalCountry(country);
//       // user.setMaskedIp("null");
//        country.setUser(user);
//        User user1=userRepository3.save(user);
//        String ans=country.getCodes()+"."+user1.getId();                           //"countryCode.userId"
//        user1.setOriginalIp(ans);
//        User user2=userRepository3.save(user1);
//        return user2;

        String givenCountryName=countryName.toUpperCase();
        Country country=new Country();
        for(CountryName countryNameValue:CountryName.values()){
            if(countryNameValue.toString().equals(givenCountryName)){
                country.setCountryName(countryNameValue);
                country.setCodes(countryNameValue.toCode());

                User user=new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setConnected(false);
                user.setMaskedIp(null);

                user.setConnectionList(new ArrayList<>());
                user.setServiceProviderList(new ArrayList<>());

                country.setUser(user);
                user.setOriginalIp(""+country.getCodes()+"."+userRepository3.save(user).getId());
                userRepository3.save(user);
                return user;
            }
        }
        throw new Exception("Country not found");
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId)  {
        Optional<User>optionalUser=userRepository3.findById(userId);
        User user=optionalUser.get();
         Optional<ServiceProvider>optionalServiceProvider=serviceProviderRepository3.findById(serviceProviderId);
         ServiceProvider serviceProvider=optionalServiceProvider.get();

         user.getServiceProviderList().add(serviceProvider);
         serviceProvider.getUsers().add(user);
         return user;
    }
}
