package com.PicPayChallenge.dtos;

import java.math.BigDecimal;

//define o dto da transactionServic
public record TransactionDTO(BigDecimal value, Long senderId, Long receiverId) {
}
