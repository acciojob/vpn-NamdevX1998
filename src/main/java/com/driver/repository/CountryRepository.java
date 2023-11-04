package com.driver.repository;

import com.driver.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
//    Country findBycountryName(String countryName);
//
//    @Query(value = "select * from country where country_name = :countryName;",nativeQuery = true)
//    Country findCountryHavingcountryName(String countryName);
}
