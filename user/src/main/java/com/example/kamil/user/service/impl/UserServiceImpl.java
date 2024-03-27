package com.example.kamil.user.service.impl;

import com.example.kamil.user.exception.customExceptions.*;
import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.dto.VendorRequestDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.entity.VendorRequest;
import com.example.kamil.user.model.enums.Role;
import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.example.kamil.user.repository.UserRepository;
import com.example.kamil.user.service.LoggedInUserDetailsService;
import com.example.kamil.user.service.UserService;
import com.example.kamil.user.service.VendorRequestService;
import com.example.kamil.user.service.sse.SseService;
import com.example.kamil.user.utils.UserUtil;
import com.example.kamil.user.utils.converter.UserDTOConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.example.kamil.user.model.enums.VendorRoleStatus.REQUESTED;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VendorRequestService vendorRequestService;
    private final SseService sseService;

    private Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private static final String SseVendorRequestUrl = "VENDOR_REQUEST_NOTIFICATIONS";

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = findUserByEmail(email);
        UserUtil.checkIsActive(user);

        return UserDTOConverter.convert(user);
     }

    @Override
//    @Transactional
    public User getUserByEmailForUserDetails(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found with this email: " + email));
    }

    @Override
    public UserDTO insertUser(RegisterPayload userRequest) {
        validateUniquenessOfEmailAndUsername(userRequest);

        User user = populateUserAndItsDetails(userRequest);

        User savedUser = userRepository.save(user);

        return UserDTOConverter.convert(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(String email, RegisterPayload userRequest) {
        User userFromDB = findUserByEmail(email);
        checkUserIsSameWithAuthenticatedUser(email,"You cannot update other user information!");

        UserUtil.checkIsActive(userFromDB);

        validateForUpdate(userFromDB,userRequest);

        User updatedUserData = updateUserData(userFromDB, userRequest);// not save only set

        User updatedUser = userRepository.save(updatedUserData); // save

        return UserDTOConverter.convert(updatedUser);
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserDTOConverter.convert(user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getActiveUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getIsActive())
                .map(user -> UserDTOConverter.convert(user))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String email) {
        User user = findUserByEmail(email);

        userRepository.deleteById(user.getId());
    }

    @Override
    @Transactional(/*value = Transactional.TxType.REQUIRES_NEW*/)
    public User findUserByEmail(String email) {
           return userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found with this email: " + email));
    }

    @Override
    @Transactional
    public void deactivateUser(String email) {
        User user = findUserByEmail(email);
        validateDeactivationPermissions(email, user);
        changeStatusOfUser(email, false);
    }

    @Override
    @Transactional
    public void activateUser(String email) {
        User user = findUserByEmail(email);
        validateActivationPermissions(user);
        changeStatusOfUser(email, true);
    }

    @Override
    @Transactional
    public void sendRequestForVendorRole(String email) { //todo:Secure this
        User user = getUserByEmailForUserDetails(email);
        LoggedInUserDetails userDetails = user.getUserDetails();

        VendorRequest vendorRequest = VendorRequest.builder()
                .userDetails(userDetails)
                .createdAt(LocalDate.now())
                .vendorRoleStatus(REQUESTED)
                .build();

        //refactorThis

        // For real time notifications add sse
        SseEmitter sseEmitter = sseEmitters.get(SseVendorRequestUrl);

        vendorRequestService.insertVendorRequest(vendorRequest);// save request in db
        //refactorThis



        //todo: implement expiration logic for rejected or not read messages
        if (sseEmitter != null) {
            sseEmitters.forEach( (str,emitter) ->
                            System.out.println(str + " : " + emitter)
                    );
            log.info("Emitter is not null; there is at least one subscribed user (admin)");
            System.out.println(sseEmitter);
            try {
                VendorRequestDTO vendorRequestDto = VendorRequestDTO.builder()
                        .email(vendorRequest.getUserDetails().getUser().getEmail())
                        .createdAt(vendorRequest.getCreatedAt())
                        .respondedAt(vendorRequest.getRespondedAt())
                        .vendorRoleStatus(vendorRequest.getVendorRoleStatus())
                        .build();
                sseEmitter.send(SseEmitter.event().name(SseVendorRequestUrl).data(vendorRequestDto));
                log.info("VendorRequest Message sent to proper destination which is: {}", SseVendorRequestUrl);
            } catch (IOException e) {
                log.error("Failed to send event via SseEmitter", e);//todo:onCompletion not working it works only after error (it can be for version incompatibility)
                //throw new SseEmitterSendingException("Failed to send event via SseEmitter", e);
            }
        } else {
            // Handle the case where there are no subscribed users (admins) online
            log.info("No subscribed users (admins) online to receive the event");
        }


    }

    @Override
    @Transactional
    public SseEmitter listenVendorRequestEmitter() {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE); // In this case, Long.MAX_VALUE is passed as the timeout value, which effectively means that
        // the emitter will never timeout.
        // This indicates that the SSE connection will be kept open indefinitely until either the server or client explicitly closes it


        sseEmitters.put(SseVendorRequestUrl , sseEmitter);
        try {
            sseEmitter.send(SseEmitter.event().name("Connection opened")); // it is just for ui only event name without data
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // when user connected check if there are not read messages (when user is not online) show
        vendorRequestService.findUnreadMessages().forEach( unreadMsg -> {
             VendorRequestDTO unreadVendorRequestDto = VendorRequestDTO.builder()
                       .email(unreadMsg.getUserDetails().getUser().getEmail())
                       .createdAt(unreadMsg.getCreatedAt())
                       .respondedAt(unreadMsg.getRespondedAt())
                       .vendorRoleStatus(unreadMsg.getVendorRoleStatus())
                       .build();
            try {
                sseEmitter.send(SseEmitter.event().name(SseVendorRequestUrl).data(unreadVendorRequestDto));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        sseEmitter.onCompletion(() -> {
                sseEmitters.remove(SseVendorRequestUrl);
                System.out.println("SSE connection was manually closed");
        });
        sseEmitter.onTimeout(() -> {
            sseEmitters.remove(SseVendorRequestUrl);
            System.out.println("SSE connection was time outed");
        });

        sseEmitter.onError((ex) -> {
            sseEmitters.remove(SseVendorRequestUrl);
            System.out.println("SSE connection encountered an error: " + ex.getMessage());
        });

        return sseEmitter;
    }

    //////////

    // It must be used with @transactional annotation , because we call lazy obj authenticatedUser.getUser()
    public void checkUserIsSameWithAuthenticatedUser(String email,String exMessage) {
        // Check that is updated user same with current user?
        LoggedInUserDetails authenticatedUser = (LoggedInUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!authenticatedUser.getUser().getEmail().equals(email)) {
            throw new PermissionDeniedException(exMessage);
        }
    }

    public LoggedInUserDetails getAuthenticatedUser() {
        return (LoggedInUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Util methods
    private void validateDeactivationPermissions(String email, User user) {
        LoggedInUserDetails authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.getAuthorities().contains(Role.ROLE_ADMIN)) {
            checkUserIsSameWithAuthenticatedUser(email,"You cannot deactivate/delete other user information!");
        }

        if (user.getUserDetails().getAuthorities().contains(Role.ROLE_ADMIN)) {
            throw new PermissionDeniedException("You cannot deactivate/delete user with admin role.\nYou can achieve through db manually!");
        }
    }

    private void validateActivationPermissions(User user) {
        if (user.getUserDetails().getAuthorities().contains(Role.ROLE_ADMIN)) {
            throw new PermissionDeniedException("You cannot activate user with admin role.\nYou can achieve through db manually!");
        }
    }

    private void validateForUpdate(User currentUser, RegisterPayload userRequest) {
        String emailFromRequest = userRequest.getEmail();
        String usernameFromRequest = userRequest.getUsername();

        if(!currentUser.getEmail().equals(emailFromRequest)){
            ifExistsByEmailThrowException(emailFromRequest);
        }
        else if (!currentUser.getUsername().equals(usernameFromRequest)) {
            ifExistsByUsernameThrowException(usernameFromRequest);
        }

    }

    private void validateUniquenessOfEmailAndUsername(RegisterPayload userRequest) {
       ifExistsByEmailThrowException(userRequest.getEmail());
       ifExistsByUsernameThrowException(userRequest.getUsername());
    }
    private void ifExistsByUsernameThrowException(String username) {
        if(userRepository.existsByUsername(username))
            throw new UserIsAlreadyExistsWithThisUsernameException(username);
    }

    private void ifExistsByEmailThrowException(String email) {
        if(userRepository.existsByEmail(email))
            throw new UserIsAlreadyExistsWithThisEmailException(email);
    }
    private User populateUserAndItsDetails(RegisterPayload userRequest){
        return User.builder()
                .lastName(userRequest.getLastName())
                .firstName(userRequest.getFirstName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .isActive(true) // it can be false ,depends on business logic
                .userDetails(
                       LoggedInUserDetails.builder()
                        .authorities(Set.of(Role.ROLE_USER))
                        .build())
                .build();
    }
    private User updateUserData(User user, RegisterPayload userRequest) {
        // Email and username must be unique
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());

        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());

        return user;
    }
    private void changeStatusOfUser(String email,boolean status){
        User user = findUserByEmail(email);
        user.setIsActive(status);
        userRepository.save(user);
    }



}
