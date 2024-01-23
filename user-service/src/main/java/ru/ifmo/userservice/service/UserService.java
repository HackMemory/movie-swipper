package ru.ifmo.userservice.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Streamable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ru.ifmo.userservice.client.FileServiceClient;
import ru.ifmo.userservice.constant.RoleConstants;
import ru.ifmo.userservice.dto.FileDTO;
import ru.ifmo.userservice.dto.FileInfoDto;
import ru.ifmo.userservice.dto.UserDTO;
import ru.ifmo.userservice.exception.NotFoundException;
import ru.ifmo.userservice.exception.ParametersException;
import ru.ifmo.userservice.exception.StorageException;
import ru.ifmo.userservice.mapper.UserMapper;
import ru.ifmo.userservice.model.Role;
import ru.ifmo.userservice.model.User;
import ru.ifmo.userservice.repository.UserRepository;
import ru.ifmo.userservice.websocket.WsFileUploadSender;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final FileServiceClient fileServiceClient;
    private final WsFileUploadSender fileUploadSender;

    @Value("${root.username}")
    private String rootUsername;

    @Value("${root.password}")
    private String rootPassword;

    @Value("${token.expire}")
    private Long expireTime;


    @PostConstruct
    @Transactional
    public void init() {
        if (roleService.getModeratorRole().isEmpty())
            roleService.saveRole(new Role(2L, RoleConstants.MODERATOR));
        if (roleService.getVipRole().isEmpty())
            roleService.saveRole(new Role(3L, RoleConstants.VIP));
        if (roleService.getMemberRole().isEmpty())
            roleService.saveRole(new Role(4L, RoleConstants.MEMBER));


        if (findByUsername(rootUsername).isEmpty()) {
            Role adminRole = roleService.getAdminRole().orElseGet(() -> roleService.saveRole(new Role(1L, RoleConstants.ADMIN)));
            User admin = User.builder()
                    .username(rootUsername)
                    .password(passwordEncoder.encode(rootPassword))
                    .roles(Set.of(adminRole)).build();
            saveUser(admin);
        }
    }

    public void register(String username, String password) {
        Role roleUser = roleService.getMemberRole().orElseThrow();
        if (findByUsername(username).isPresent()){
            throw new ParametersException("User already exist");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(Set.of(roleUser)).build();

        saveUser(user);
    }

    public void changeRole(String username, String role) {
        User user = findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        Role roleEntity = roleService.findByName(role.toUpperCase())
                .orElseThrow(()-> new NotFoundException("Role not found"));

        user.getRoles().clear();
        user.getRoles().add(roleEntity);

        saveUser(user);
    }

    public void uploadAvatar(MultipartFile file, String username) {
        if(file.isEmpty()){
            throw new StorageException("File is empty");
        }

        User user = findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));

        
        ResponseEntity<FileDTO> response = fileServiceClient.uploadFile(file, user.getId());
        if(response.getStatusCode().is2xxSuccessful()){
            user.setAvatarPath(response.getBody().getUuid());
            saveUser(user);

            FileInfoDto fileInfoDto = FileInfoDto.builder()
                            .email(user.getEmail())
                            .uuid(response.getBody().getUuid()).build();


            fileUploadSender.sendMessage(fileInfoDto);
        } else {
            throw new StorageException("Error occured");
        }
    }

    @Transactional
    public void changeEmail(String newEmail, String username) {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public User saveUser(User userEntity) {
        return userRepository.save(userEntity);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // @Override
    // public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //     return userRepository.findByUsername(username)
    //                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    // }

    public List<UserDTO> getAllUsers() {
        return Streamable.of(userRepository.findAll()).stream()
                .map(UserMapper.INSTANCE::toDomain).toList();
    }
}
