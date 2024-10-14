package com.mh.catalogservice.service;

import com.mh.catalogservice.entity.CatalogEntity;
import com.mh.catalogservice.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogService {
    private final CatalogRepository catalogRepository;

    public Iterable<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }

}