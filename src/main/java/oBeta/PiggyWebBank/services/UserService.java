package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.Role;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.entities.UserCharacteristic;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.payloads.me.MeUserDTO;
import oBeta.PiggyWebBank.payloads.signin.SigninDTO;
import oBeta.PiggyWebBank.payloads.admin.UserDTO;
import oBeta.PiggyWebBank.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepo;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private PasswordEncoder bcrypt;


    public Page<User> getAllUsersPage(int page, int size, String sortBy){
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.usersRepo.findAll(pageable);
    }

    public List<User> getAllUsers(){
        return this.usersRepo.findAll();
    }

    public User getUserById (UUID idToFind){
        return this.usersRepo.findById(idToFind)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + idToFind + " not found!")
                );
    }

    public User getUserByUsername(String username){
        return this.usersRepo.findByUsername(username)
                .orElseThrow( () ->
                        new NotFoundException("User with username " + username + " not found!")
                );
    }

    public User saveNewUser(UserDTO userDTO){

        this.usersRepo.findByUsername(userDTO.username())
                .ifPresent(user -> {
                    throw new BadRequestException("User with username " + user.getUsername() + " already exist!");
                });

        this.usersRepo.findByEmail(userDTO.email())
                .ifPresent(user -> {
                    throw new BadRequestException("User with email " + user.getEmail() + " already exist!");
                });

        Role userRole = this.rolesService.getRoleById(userDTO.role_id());



        return this.usersRepo.save(new User(userDTO, bcrypt.encode(userDTO.password()), userRole));
    }

    public User signin(SigninDTO userDTO){

        this.usersRepo.findByUsername(userDTO.username())
                .ifPresent(user -> {
                    throw new BadRequestException("User with username " + user.getUsername() + " already exist!");
                });

        this.usersRepo.findByEmail(userDTO.email())
                .ifPresent(user -> {
                    throw new BadRequestException("User with email " + user.getEmail() + " already exist!");
                });

        Role userRole = this.rolesService.getUserRole();

        return this.usersRepo.save(new User(userDTO, bcrypt.encode(userDTO.password()), userRole));
    }

    public User updateUser (UUID idToUpdate, UserDTO dto){

        User found = this.getUserById(idToUpdate);

        if (!found.getUsername().equals(dto.username())) {
            Optional<User> searchUserByUsername = usersRepo.findByUsername(dto.username());
            if (searchUserByUsername.isPresent() && searchUserByUsername.get().getId() != idToUpdate){
                throw new BadRequestException("User with username " + dto.username() + " already exist!");
            }
        }

        if (!found.getEmail().equals(dto.email())) {
            Optional<User> searchUserByEmail = usersRepo.findByEmail(dto.email());
            if (searchUserByEmail.isPresent() && searchUserByEmail.get().getId() != idToUpdate){
                throw new BadRequestException("User with email " + dto.email() + " already exist!");
            }
        }

        if(this.isFoundEqualsToDTO(found, dto))
            return found;

        found.setName(dto.name());
        found.setSurname(dto.surname());
        found.setUsername(dto.username());
        found.setEmail(dto.email());
        found.setRole(
                this.rolesService.getRoleById(dto.role_id())
        );
        found.setNewPassword(bcrypt.encode(dto.password()));

        return this.usersRepo.save(found);
    }

    public User updateMeUser (MeUserDTO dto, User loggedUser){

        if (!loggedUser.getUsername().equals(dto.username())) {
            Optional<User> searchUserByUsername = usersRepo.findByUsername(dto.username());
            if (searchUserByUsername.isPresent() && searchUserByUsername.get().getId() != loggedUser.getId()){
                throw new BadRequestException("User with username " + dto.username() + " already exist!");
            }
        }

        if (!loggedUser.getEmail().equals(dto.email())) {
            Optional<User> searchUserByEmail = usersRepo.findByEmail(dto.email());
            if (searchUserByEmail.isPresent() && searchUserByEmail.get().getId() != loggedUser.getId()){
                throw new BadRequestException("User with email " + dto.email() + " already exist!");
            }
        }

        if(this.isFoundEqualsToDTO(loggedUser, dto))
            return loggedUser;

        loggedUser.setName(dto.name());
        loggedUser.setSurname(dto.surname());
        loggedUser.setUsername(dto.username());
        loggedUser.setEmail(dto.email());

        return this.usersRepo.save(loggedUser);
    }


    public void updatePassword(String newPassword, User user){

        if(bcrypt.matches(newPassword, user.getPassword()))
            throw new BadRequestException("The password must be different from the existing one");

        user.setNewPassword(bcrypt.encode(newPassword));
        this.usersRepo.save(user);
    }

    public void deleteUser (UUID idToDelete){
        this.usersRepo.delete(
                this.getUserById(idToDelete)
        );
    }

    private boolean isFoundEqualsToDTO (User found, UserDTO dto) {
        return found.getName().equals(dto.name()) &&
                found.getSurname().equals(dto.surname()) &&
                found.getUsername().equals(dto.username()) &&
                found.getEmail().equals(dto.email()) &&
                found.getRole().getId() == dto.role_id() &&
                bcrypt.matches(dto.password(), found.getPassword());
    }

    private boolean isFoundEqualsToDTO (User found, MeUserDTO dto) {
        return found.getName().equals(dto.name()) &&
                found.getSurname().equals(dto.surname()) &&
                found.getUsername().equals(dto.username()) &&
                found.getEmail().equals(dto.email());
    }

}
