package com.driver.services.impl;

import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.driver.model.*;

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
    public Admin addServiceProvider(int adminId, String providerName) {
        Admin admin=adminRepository1.findById(adminId).get();
        if(admin==null)
            return null;
        ServiceProvider serviceProvider=serviceProviderRepository1.findByName(providerName);
        if(serviceProvider==null)
            return null;
        admin.getServiceProviders().add(serviceProvider);
        serviceProvider.setAdmin(admin);
        Admin admin1=adminRepository1.save(admin);
        return admin1;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws RuntimeException{
        ServiceProvider serviceProvider=serviceProviderRepository1.findById(serviceProviderId).get();
        if(serviceProvider==null)
            throw new RuntimeException("ServiceProvider not found");
        char ch=countryName.charAt(0);
        Country country=new Country();
        if(ch=='I' || ch=='i'){
            country.setCountryName(CountryName.IND);
            country.setCodes("001");
        }else if(ch=='U' || ch=='u'){
            country.setCountryName(CountryName.USA);
            country.setCodes("002");
        }else if(ch=='A' || ch=='a'){
            country.setCountryName(CountryName.AUS);
            country.setCodes("003");
        }else if(ch=='C' || ch=='c'){
            country.setCountryName(CountryName.CHI);
            country.setCodes("004");
        }else if(ch=='J' || ch=='j'){
            country.setCountryName(CountryName.JPN);
            country.setCodes("005");
        }else
            throw new RuntimeException("Country not found");

        country.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(country);
        ServiceProvider savedServiceProvider=serviceProviderRepository1.save(serviceProvider);
        return savedServiceProvider;
    }
}
