package oBeta.PiggyWebBank.services;


import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.UnauthorizedException;
import oBeta.PiggyWebBank.jwt.JWT;
import oBeta.PiggyWebBank.payloads.login.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JWT jwt;

    @Autowired
    private PasswordEncoder bcrypt;

    public String checkLoginCredentials(LoginDTO dto){
        User loggedUser = this.userService.getUserByUsername(dto.username());

        if(!bcrypt.matches(dto.password(), loggedUser.getPassword()))
            throw new UnauthorizedException("Wrong Credentials!");

        return jwt.createToken(loggedUser);
    }

}
