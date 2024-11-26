package oBeta.PiggyWebBank.services;

import oBeta.PiggyWebBank.entities.Role;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.BadRequestException;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.payloads.signin.SigninDTO;
import oBeta.PiggyWebBank.payloads.UserDTO;
import oBeta.PiggyWebBank.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Page<User> getAllUsers(int page, int size, String sortBy){
        if(size > 50) size = 50;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.usersRepo.findAll(pageable);
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

        return this.usersRepo.save(found);
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
                found.getRole().getId() == dto.role_id();
    }

}
