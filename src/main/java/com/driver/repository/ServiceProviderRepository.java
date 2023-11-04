package com.driver.repository;

import com.driver.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Integer> {
//    ServiceProvider findByName(String providerName);
//
//    @Query(value = "select * from service_provider order by id;",nativeQuery=true)
//    List<ServiceProvider> findAllServiceProviderWithAscendingOrder();
//
//    @Query(value="select * from service_provider where name = :providerName;",nativeQuery=true)
//    ServiceProvider findServiceProviderHavingName(String providerName);
}
