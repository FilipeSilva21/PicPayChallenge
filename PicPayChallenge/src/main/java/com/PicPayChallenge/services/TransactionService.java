package com.PicPayChallenge.services;

import com.PicPayChallenge.domain.Transaction.Transaction;
import com.PicPayChallenge.domain.User.User;
import com.PicPayChallenge.dtos.TransactionDTO;
import com.PicPayChallenge.repositories.TransactionRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.sound.midi.Receiver;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

//contrutor da classe de servico de transacoes
@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());


        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if (!isAuthorized){
            throw new Exception("Not authorized");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        this.userService.SaveUser(sender);
        this.userService.SaveUser(receiver);

        this.notificationService.sendNotification(sender, "transaction completed successfully");
        this.notificationService.sendNotification(receiver, "transaction completed successfully");
        return newTransaction;
    }





    public boolean authorizeTransaction(User sender, BigDecimal value) {
       ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

       if(authorizationResponse.getStatusCode()== HttpStatus.OK) {
           String message = (String) authorizationResponse.getBody().get("message");
           return "authorized".equalsIgnoreCase(message);
       } else return false;
    }
}


