package com.driver.services.impl;

import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.driver.model.*;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin=new Admin();
        admin.setPassword(password);
        admin.setUsername(username);
        Admin admin1=adminRepository1.save(admin);
        return admin1;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName){
        Optional<Admin> optionalAdmin=adminRepository1.findById(adminId);
        Admin admin=optionalAdmin.get();

        ServiceProvider serviceProvider=new ServiceProvider();
        serviceProvider.setName(providerName);
        serviceProvider.setUsers(new ArrayList<>());
        serviceProvider.setConnectionList(new ArrayList<>());
        serviceProvider.setCountryList(new ArrayList<>());

        serviceProvider.setAdmin(admin);
        admin.getServiceProviders().add(serviceProvider);
        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
//        ServiceProvider serviceProvider=serviceProviderRepository1.findById(serviceProviderId).get();
//        if(serviceProvider==null)
//            throw new Exception("ServiceProvider not found");
//        char ch=countryName.charAt(0);
//        Country country=new Country();
//        if(ch=='I' || ch=='i'){
//            country.setCountryName(CountryName.IND);
//            country.setCodes("001");
//        }else if(ch=='U' || ch=='u'){
//            country.setCountryName(CountryName.USA);
//            country.setCodes("002");
//        }else if(ch=='A' || ch=='a'){
//            country.setCountryName(CountryName.AUS);
//            country.setCodes("003");
//        }else if(ch=='C' || ch=='c'){
//            country.setCountryName(CountryName.CHI);
//            country.setCodes("004");
//        }else if(ch=='J' || ch=='j'){
//            country.setCountryName(CountryName.JPN);
//            country.setCodes("005");
//        }else
//            throw new Exception("Country not found");
//
//        country.setServiceProvider(serviceProvider);
//        serviceProvider.getCountryList().add(country);
//        ServiceProvider savedServiceProvider=serviceProviderRepository1.save(serviceProvider);
//        return savedServiceProvider;

        String givenCountryName=countryName.toUpperCase();
        Country country=null;
        for(CountryName i:CountryName.values()){
            if(i.toString().equals(givenCountryName)){
                country=new Country();
                country.setCountryName(i);
                country.setCodes(i.toCode());
            }
        }
        if(country==null)
            throw new Exception("Country not found");

        Optional<ServiceProvider>optionalServiceProvider=serviceProviderRepository1.findById(serviceProviderId);
        ServiceProvider serviceProvider=optionalServiceProvider.get();
        country.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(country);

        serviceProviderRepository1.save(serviceProvider);
        return serviceProvider;
    }
}
