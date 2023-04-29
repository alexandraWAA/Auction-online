package com.example.auctionapp.dto;

import com.example.auctionapp.models.Bid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BidDTO {
    @JsonIgnore
    private Long id;
    private String bidderName;
    @JsonIgnore
    private LocalDateTime bidDate;
    private Long lotId;
public static BidDTO fromBid(Bid bid) {
    BidDTO bidDTO = new BidDTO();
    bidDTO.setId(bid.getId());
    bidDTO.setBidderName(bid.getBidderName());
    bidDTO.setBidDate(bid.getBidDate());
    return bidDTO;
}
    public Bid toBid() {
        Bid bid = new Bid();
        bid.setId(this.getId());
        bid.setBidderName(this.getBidderName());
        return bid;
    }
}
