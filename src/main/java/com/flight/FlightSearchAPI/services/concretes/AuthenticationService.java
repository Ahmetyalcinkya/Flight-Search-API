package com.flight.FlightSearchAPI.services.concretes;

import com.flight.FlightSearchAPI.dto.responses.LoginResponse;
import com.flight.FlightSearchAPI.dto.responses.UserResponse;
import com.flight.FlightSearchAPI.entities.Authority;
import com.flight.FlightSearchAPI.entities.ConfirmationToken;
import com.flight.FlightSearchAPI.entities.Token;
import com.flight.FlightSearchAPI.entities.User;
import com.flight.FlightSearchAPI.exceptions.FlightException;
import com.flight.FlightSearchAPI.repositories.AuthorityRepository;
import com.flight.FlightSearchAPI.repositories.UserRepository;
import com.flight.FlightSearchAPI.services.abstracts.EmailService;
import com.flight.FlightSearchAPI.services.abstracts.ModelMapperService;
import com.flight.FlightSearchAPI.services.abstracts.TokenService;
import com.flight.FlightSearchAPI.services.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private UserService userService;
    private ConfirmationTokenService confirmationTokenService;
    private EmailService emailService;
    private ModelMapperService modelMapperService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, AuthorityRepository authorityRepository,
                                 PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                 TokenService tokenService, UserService userService,
                                 ConfirmationTokenService confirmationTokenService, EmailService emailService, ModelMapperService modelMapperService) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
        this.modelMapperService = modelMapperService;
    }
    @Transactional
    public UserResponse register(String name, String surname, String email, String password){

        userService.getUserByEmail(email);

        String encodedPassword = passwordEncoder.encode(password);
        LocalDateTime date = LocalDateTime.now();
        String emailToken = UUID.randomUUID().toString();

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        Authority authority = authorityRepository.findByAuthority("USER");
        user.setAuthority(authority);
        userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setEmailToken(emailToken);
        confirmationToken.setCreatedAt(date);
        confirmationToken.setExpiresAt(date.plusWeeks(1));
        confirmationToken.setUser(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String text = "Congrats!\n"+
                "Your registration to E-Commerce is successful. The only thing you need to" +
                " do is to click the link below to activate your account and start shopping!\n"+
                "Activate your account: " + "http://localhost:9000/auth/confirm?emailToken="+emailToken;
        emailService.sendEmail(email, "Blog Activation Mail", text);

        return modelMapperService.forResponse().map(user, UserResponse.class);
    }
    @Transactional
    public String confirmationEmailToken(String emailToken){
        ConfirmationToken confirmationToken = confirmationTokenService
                .getConfirmationToken(emailToken)
                .orElseThrow(() ->
                        new FlightException("Token not found", HttpStatus.NOT_FOUND));
        if (confirmationToken.getConfirmedAt() != null){
            throw new FlightException("Email already confirmed!", HttpStatus.BAD_REQUEST);
        }
        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if(LocalDateTime.now().isAfter(expiresAt)){
            throw new FlightException("Token expired!", HttpStatus.BAD_REQUEST);
        }
        confirmationTokenService.setConfirmedAt(emailToken);

        userService.enableUser(confirmationToken.getUser().getEmail());

        return "Email confirmed!";
    }
    @Transactional
    public LoginResponse login(String email, String password){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            String token = tokenService.generateJwtToken(authentication);

            User foundUser = setUserToken(email);
            LocalDateTime expiry = LocalDateTime.now().plusDays(1);
            Token userToken = new Token();
            userToken.setToken(token);
            userToken.setExpiryDate(expiry);
            userService.updateUser(userToken, foundUser);
            tokenService.saveToken(userToken);

            return new LoginResponse(token);
        } catch (Exception ex){
            ex.printStackTrace();
            throw new FlightException("User not found!",HttpStatus.NOT_FOUND);
        }
    }
    public User setUserToken(String email){
        Optional<User> foundUser = userRepository.findUserByEmail(email);

        if(foundUser.isPresent()){
            return foundUser.get();
        }
        throw new FlightException("User not found!",HttpStatus.NOT_FOUND);
    }
}
