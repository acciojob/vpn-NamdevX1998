package com.driver.services.impl;

import com.driver.model.Connection;
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
    public User register(String username, String password, String countryName) throws Exception{
        Country country=countryRepository3.findCountryHavingcountryName(countryName);
        if(country==null)
            throw new Exception("");

        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false);
        user.setOriginalCountry(country);
       // user.setMaskedIp("null");
        country.setUser(user);
        User user1=userRepository3.save(user);
        String ans=country.getCodes()+"."+user1.getId();                           //"countryCode.userId"
        user1.setOriginalIp(ans);
        User user2=userRepository3.save(user1);
        return user2;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) throws Exception {
        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();
        if(serviceProvider==null){
            throw new Exception("invalid");
        }
        Optional<User>optionalUser=userRepository3.findById(userId);
        if(!(optionalUser.isPresent())){
            throw new RuntimeException("invalid");
        }
        User user=optionalUser.get();
        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);
//        Connection connection=new Connection();
//        connection.setUser(user);
//        connection.setServiceProvider(serviceProvider);
//        serviceProvider.getConnectionList().add(connection);
//        user.setConnected(true);
//        user.getConnectionList().add(connection);
        User user1=userRepository3.save(user);
        return user1;
    }
}
