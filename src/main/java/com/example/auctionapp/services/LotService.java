package com.example.auctionapp.services;

import com.example.auctionapp.dto.BidDTOForFullLotDTO;
import com.example.auctionapp.dto.CreateLotDTO;
import com.example.auctionapp.dto.FullLotDTO;
import com.example.auctionapp.dto.LotDTO;
import com.example.auctionapp.enums.LotStatus;
import com.example.auctionapp.models.Lot;
import com.example.auctionapp.pojection.LotProjection;
import com.example.auctionapp.repositories.LotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@Transactional
public class LotService {
    private LotRepository lotRepository;
    private BidService bidService;

    public LotService(LotRepository lotRepository, BidService bidService) {
        this.lotRepository = lotRepository;
        this.bidService = bidService;
    }

    public LotDTO createNewLot (CreateLotDTO createLotDTO) {
        Lot lot = createLotDTO.toLot();
        lot.setStatus(LotStatus.CREATED);
        return LotDTO.fromLot(lotRepository.save(lot));
    }
    public BidDTOForFullLotDTO createBidder(Long id, BidDTOForFullLotDTO bidDTOForFullLotDTO){
        LotDTO lotDTO = LotDTO.fromLot(lotRepository.findById(id).orElse(null));
        if (lotDTO == null){
            return null;
        }
        return bidService.createNewBidder(bidDTOForFullLotDTO, lotDTO);
    }
    public List<LotDTO> getAllLotsByStatusOnPage (LotStatus lotStatus, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
        return lotRepository.findAllByStatus(lotStatus, pageRequest)
                .stream()
                .map(LotDTO::fromLot)
                .collect(Collectors.toList());
    }
    public Collection<FullLotDTO> getAllFullLots() {
        return lotRepository.findAll()
                .stream()
                .map(FullLotDTO::fromLot)
                .collect(Collectors.toList());
    }
    private Long totalPrice(Long lotId) {
        Lot lot = getLotById(lotId).toLot();
        Long countPrice = bidService.countTotalPrice(lotId);
        return countPrice * lot.getBidPrice() + lot.getStartPrice();
    }
    public FullLotDTO getFullLotById (Long id) {
        Lot lot = lotRepository.findById(id).orElse(null);
        if (lot == null) {
            return null;
        }
        FullLotDTO fullLotDTO = FullLotDTO.fromLot(lot);
        fullLotDTO.setCurrentPrice(totalPrice(id));
        fullLotDTO.setLastBid(getLastBidderByLotId(id));
        return fullLotDTO;
    }
    public LotProjection getLastBidForLot (Long id) {
        return bidService.getMaxBiddersOfBidByLotId(id);
    }
    public BidDTOForFullLotDTO getLastBidderByLotId (Long lotId) {
        return BidDTOForFullLotDTO.fromBid(bidService.getLastBidderByLotId(lotId));
    }
    public LotDTO getLotById (Long id) {
        Lot lot = lotRepository.findById(id).orElse(null);
        return LotDTO.fromLot(lot);
    }
    public void updateStatus(Long id, LotStatus lotStatus) {
        Lot lot = lotRepository.findById(id).orElse(null);
        lot.setStatus(lotStatus);
        lotRepository.save(lot);
    }

    public boolean checkMistakeInCreatingLot(CreateLotDTO createLotDTO) {
        if(createLotDTO.getTitle() == null || createLotDTO.getTitle().isBlank() ||
                createLotDTO.getDescription() == null || createLotDTO.getDescription().isBlank() ||
        createLotDTO.getStartPrice() == null || createLotDTO.getBidPrice() == null){
            return false;
        } else {
            return true;
        }
    }
    public FullLotDTO getFullLot(Long id) {
        Lot lot = lotRepository.findById(id).orElse(null);

        if (lot != null) {
            FullLotDTO fullLotDTO = FullLotDTO.fromLot(lot);
            fullLotDTO.setCurrentPrice(totalPrice(id));
            fullLotDTO.setLastBid(findLastBid(id));
            return fullLotDTO;
        }
        return null;
    }
    private BidDTOForFullLotDTO findLastBid(Long id) {
        if (bidService.countTotalPrice(id) != 0) {
            return bidService.findLastBid(id);
        }
        return null;
    }
}
