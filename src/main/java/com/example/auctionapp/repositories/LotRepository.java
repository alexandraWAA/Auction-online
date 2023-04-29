package com.example.auctionapp.repositories;

import com.example.auctionapp.enums.LotStatus;
import com.example.auctionapp.models.Lot;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LotRepository extends JpaRepository<Lot, Long> {
    List<Lot> findAllByStatus(LotStatus lotStatus, PageRequest pageRequest);
    }
