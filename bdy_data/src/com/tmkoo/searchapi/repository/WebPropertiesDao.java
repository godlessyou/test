package com.tmkoo.searchapi.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tmkoo.searchapi.entity.WebProperties;

public interface WebPropertiesDao extends PagingAndSortingRepository<WebProperties, Long>, JpaSpecificationExecutor<WebProperties> {
}
