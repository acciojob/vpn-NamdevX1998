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
    public User connect(int userId, String countryName) throws Exception{
        User user=userRepository2.findById(userId).get();
        if(user==null)    throw new Exception("Invalid user id");
        Country country=countryRepository.findBycountryName(countryName);
        if(country==null)    throw new Exception("Invalid country name");
        List<ServiceProvider>serviceProviders=user.getServiceProviderList();
        if(serviceProviders.size()!=0)
            throw new Exception("Already connected");
        if(user.getOriginalCountry().getCountryName().toString().equalsIgnoreCase(countryName))
            return user;
        List<ServiceProvider>serviceProviderList=serviceProviderRepository2.findAll();    //findAllServiceProviderWithAscendingOrder();
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
                serviceProvider.getConnectionList().add(connection);
                User user1=userRepository2.save(user);
                return user1;
            }
        }
        throw new Exception("Unable to connect");
    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user=userRepository2.findById(userId).get();
        if(user==null)
            throw new Exception("Invalid user id");
        if(user.getConnected()==false){
            throw new Exception("Already disconnected");
        }
        user.setMaskedIp("null");
        user.setConnected(false);
        User user1=userRepository2.save(user);
        return user1;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User sender=userRepository2.findById(senderId).get();
        User receiver=userRepository2.findById(receiverId).get();
        if(sender==null || receiver==null)
            throw new Exception("invalid id");
        if(sender.getOriginalCountry().getCountryName().toString().
                equalsIgnoreCase(receiver.getOriginalCountry().getCountryName().toString())){
            return sender;
        }
        return receiver;
    }
}
