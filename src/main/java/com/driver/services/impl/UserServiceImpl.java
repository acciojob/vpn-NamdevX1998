package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public User register(String username, String password, String countryName) throws RuntimeException{
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        Country country=countryRepository3.findBycountryName(countryName);
        if(country==null)
            throw new RuntimeException();
        user.setConnected(false);
        user.setCountry(country);
        country.setUser(user);
        User user1=userRepository3.save(user);
        return user1;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();
        if(serviceProvider==null)
            return null;
        Optional<User>optionalUser=userRepository3.findById(userId);
        if(!(optionalUser.isPresent())){
            return null;
        }
        User user=optionalUser.get();
        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);
        User user1=userRepository3.save(user);
        return user;
    }
}
