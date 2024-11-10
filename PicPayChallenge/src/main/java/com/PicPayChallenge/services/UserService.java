package com.PicPayChallenge.services;

import com.PicPayChallenge.domain.User.User;
import com.PicPayChallenge.domain.User.UserType;
import com.PicPayChallenge.dtos.UserDTO;
import com.PicPayChallenge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

//contrutor da classe de servico do usuario
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("User seller can't receive transactions");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Not enough balance");
        }
    }

    public User createUser(UserDTO data){
        User newUser = new User(data);
        this.SaveUser(newUser);
        return  newUser;
    }

    public List<User> getAllUsers(){
        return this.repository.findAll();
    }

    public User findUserById(Long id) throws Exception{
            return  this.repository.findUserById(id).orElseThrow(() -> new Exception("User not found"));
    }

    public void SaveUser(User user){
        this.repository.save(user);
    }

    public List<User> getUsers() {
        return null;
    }
}
