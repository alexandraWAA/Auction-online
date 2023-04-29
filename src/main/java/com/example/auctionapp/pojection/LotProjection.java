package com.example.auctionapp.pojection;

import java.time.LocalDateTime;

public interface LotProjection {
    String getBidderName();
    LocalDateTime getBidDate();
}
