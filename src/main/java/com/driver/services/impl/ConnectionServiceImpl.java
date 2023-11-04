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
       if(user.getConnected()){
           throw new Exception("AlreadyConnected");
       }
        String inputCountryName=countryName.toUpperCase();
       String userCountryName=user.getOriginalCountry().getCountryName().toString();

       if(inputCountryName.equals(userCountryName))
           return user;

       ServiceProvider serviceProvider=null;
       Country country=null;

       for(ServiceProvider serviceProvider1:user.getServiceProviderList()){
           for(Country country1:serviceProvider1.getCountryList()){
               if(country1.getCountryName().toString().equals(inputCountryName)){
                   serviceProvider=serviceProvider1;
                   country=country1;
                   break;
               }
           }
           if(serviceProvider!=null)break;
       }
       if(serviceProvider==null)
           throw new Exception("Unable to connect");

       Connection connection=new Connection();
       connection.setUser(user);
       connection.setServiceProvider(serviceProvider);
        serviceProvider.getConnectionList().add(connection);
        user.getConnectionList().add(connection);
        user.setConnected(true);
        user.setMaskedIp(""+country.getCountryName().toCode()+"."+serviceProvider.getId()+"."+userId);

        userRepository2.save(user);
        serviceProviderRepository2.save(serviceProvider);

        return user;
    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user=userRepository2.findById(userId).get();
        if(!user.getConnected()){
            throw new Exception("Already disconnected");
        }
        user.setMaskedIp(null);
        user.setConnected(false);
        User user1=userRepository2.save(user);
        return user1;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User sender=userRepository2.findById(senderId).get();
        User receiver=userRepository2.findById(receiverId).get();
        String senderCountry=sender.getOriginalCountry().getCountryName().toString();
        String receiverCountry=null;
       if(receiver.getConnected()){
           String code=receiver.getMaskedIp().substring(0,3);
           switch(code){
               case "001":receiverCountry="IND";break;
               case "002":receiverCountry="USA";break;
               case "003":receiverCountry="AUS";break;
               case "004":receiverCountry="CHI";break;
               case "005":receiverCountry="JPN";break;
           }
       }else{
           receiverCountry=receiver.getOriginalCountry().getCountryName().toString();
       }
       if(senderCountry.equals(receiverCountry))
           return sender;

       User user=connect(senderId,receiverCountry);
       if(!user.getConnected()){
           throw new Exception("cannot establish communication");
       }
       return user;
    }
}
