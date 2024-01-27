package com.flight.FlightSearchAPI.services.concretes;

import com.flight.FlightSearchAPI.dto.responses.UserResponse;
import com.flight.FlightSearchAPI.entities.Token;
import com.flight.FlightSearchAPI.entities.User;
import com.flight.FlightSearchAPI.exceptions.FlightException;
import com.flight.FlightSearchAPI.repositories.UserRepository;
import com.flight.FlightSearchAPI.services.abstracts.ModelMapperService;
import com.flight.FlightSearchAPI.services.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserManager implements UserService,UserDetailsService {
    private UserRepository userRepository;
    private ModelMapperService modelMapperService;

    @Autowired
    public UserManager(UserRepository userRepository, ModelMapperService modelMapperService) {
        this.userRepository = userRepository;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user ->
                modelMapperService.forResponse().map(user, UserResponse.class)).collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new FlightException("User not found!", HttpStatus.NOT_FOUND));
        return modelMapperService.forResponse().map(user, UserResponse.class);
    }

    @Override
    public void getUserByEmail(String email) {
        boolean userExist = userRepository.findUserByEmail(email).isPresent();
        if(userExist) throw new FlightException("User already exist!", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void updateUser(Token token, User user) {
        user.setToken(token);
    }

    @Override
    public UserResponse deleteUser(long id) {
        String email = getAuthenticatedUser();
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isPresent()){
            if (optionalUser.get().getAuthority().getAuthority().equals("ADMIN")){
                List<User> users = userRepository.findAll();
                for(User user: users){
                    if (user.getId() == id){
                        userRepository.delete(user);
                        return modelMapperService.forResponse().map(user, UserResponse.class);
                    }
                }
            }
            throw new FlightException("You don't have any permission to use this method!", HttpStatus.FORBIDDEN);
        }
        throw new FlightException("User not authenticated!", HttpStatus.FORBIDDEN);
    }

    @Override
    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }
    @Override
    public String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username).orElseThrow();
    }
}
