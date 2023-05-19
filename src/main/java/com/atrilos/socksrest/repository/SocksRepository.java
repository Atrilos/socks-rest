package com.atrilos.socksrest.repository;

import com.atrilos.socksrest.model.Socks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SocksRepository extends JpaRepository<Socks, Long>, JpaSpecificationExecutor<Socks> {
    Optional<Socks> findByColorAndCottonPart(String color, Integer cottonPart);

}