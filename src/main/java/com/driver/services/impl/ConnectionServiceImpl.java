package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Autowired
    CountryRepository countryRepository;

    @Override
    public User connect(int userId, String countryName) throws RuntimeException{
        User user=userRepository2.findById(userId).get();
        if(user==null)    throw new RuntimeException("Invalid user id");
        Country country=countryRepository.findBycountryName(countryName);
        if(country==null)    throw new RuntimeException("Invalid country name");
        List<ServiceProvider>serviceProviders=user.getServiceProviderList();
        if(serviceProviders.size()!=0)
            throw new RuntimeException("Already connected");
        if(user.getCountry().getCountryName().toString().equalsIgnoreCase(countryName))
            return user;
        List<ServiceProvider>serviceProviderList=serviceProviderRepository2.findAllServiceProviderWithAscendingOrder();
        for(ServiceProvider serviceProvider:serviceProviderList){
            List<Country>countryList=serviceProvider.getCountryList();
            if(countryList.contains(country)){
                user.getServiceProviderList().add(serviceProvider);
                String ans=country.getCodes()+"."+serviceProvider.getId()+"."+user.getId();
                user.setMaskedIp(ans);
                user.setConnected(true);
                Connection connection=new Connection();
                connection.setUser(user);
                connection.setServiceProvider(serviceProvider);
                user.getConnectionList().add(connection);
                serviceProvider.getUsers().add(user);
                User user1=userRepository2.save(user);
                return user1;
            }
        }
        throw new RuntimeException("Unable to connect");
    }
    @Override
    public User disconnect(int userId) throws RuntimeException {
        User user=userRepository2.findById(userId).get();
        if(user==null)
            throw new RuntimeException("Invalid user id");
        if(user.getConnected()==false){
            throw new RuntimeException("Already disconnected");
        }
        user.setMaskedIp("null");
        user.setConnected(false);
        User user1=userRepository2.save(user);
        return user1;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws RuntimeException {
        User sender=userRepository2.findById(senderId).get();
        User receiver=userRepository2.findById(receiverId).get();
        if(sender==null || receiver==null)
            throw new RuntimeException("invalid id");
        if(sender.getCountry().getCountryName().toString().
                equalsIgnoreCase(receiver.getCountry().getCountryName().toString())){
            return sender;
        }
        return receiver;
    }
}
