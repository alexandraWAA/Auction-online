package com.example.auctionapp.services;

import com.example.auctionapp.dto.BidDTO;
import com.example.auctionapp.dto.BidDTOForFullLotDTO;
import com.example.auctionapp.dto.LotDTO;
import com.example.auctionapp.models.Bid;
import com.example.auctionapp.pojection.LotProjection;
import com.example.auctionapp.repositories.BidRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Slf4j
@Service
public class BidService {
    private BidRepository bidRepository;

    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public BidDTOForFullLotDTO createNewBidder (BidDTOForFullLotDTO bidDTOForFullLotDTO, LotDTO lotDTO) {
        Bid bid = bidDTOForFullLotDTO.toBid();
        bid.setLot(lotDTO.toLot());
        bid.setBidDate(LocalDateTime.now());
        return BidDTOForFullLotDTO.fromBid(bidRepository.save(bid));
    }
    public LotProjection getFirstBidderByLotId (Long lotId) {
        return bidRepository.findByBidDateMin(lotId);
    }
    public Bid getLastBidderByLotId (Long lotId) {
        return bidRepository.findByBidDateMax(lotId);
    }
    public LotProjection getMaxBiddersOfBidByLotId(Long lotId) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setBidderName(bidRepository.findByBidDateMax(lotId).getBidderName());
        bidDTO.setBidDate(bidRepository.findByBidDateMax(lotId).getBidDate());
        return bidRepository.getMaxBidders(lotId);
    }
    public Long countTotalPrice(Long lotId) {
        return bidRepository.getCountNumberOfBidByLotId(lotId);
    }
    public BidDTOForFullLotDTO findLastBid(Long id) {
        return BidDTOForFullLotDTO.fromBid(bidRepository.findByBidDateMax(id));
    }
}
