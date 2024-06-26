package com.example.kamil.user.service.impl;

import com.example.kamil.common.response.BaseResponse;
import com.example.kamil.user.exception.customExceptions.PermissionDeniedException;
import com.example.kamil.user.exception.customExceptions.UserIsAlreadyExistsWithThisEmailException;
import com.example.kamil.user.exception.customExceptions.UserIsAlreadyExistsWithThisUsernameException;
import com.example.kamil.user.exception.customExceptions.UserNotFoundException;
import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.dto.UserNotificationDTO;
import com.example.kamil.user.model.dto.VendorRequestDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.entity.UserNotification;
import com.example.kamil.user.model.entity.VendorRequest;
import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.example.kamil.user.model.enums.Role;
import com.example.kamil.user.model.enums.UserNotificationStatus;
import com.example.kamil.user.model.enums.VendorRoleStatus;
import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.repository.UserRepository;
import com.example.kamil.user.service.UserNotificationService;
import com.example.kamil.user.service.UserService;
import com.example.kamil.user.service.VendorRequestService;
import com.example.kamil.user.service.sse.SseService;
import com.example.kamil.user.utils.UserUtil;
import com.example.kamil.user.utils.converter.UserDTOConverter;
import com.example.kamil.user.utils.converter.VendorRequestDTOConverter;
import com.example.kamil.user.utils.mapper.UserNotificationMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.example.kamil.user.model.enums.Role.ROLE_VENDOR;
import static com.example.kamil.user.model.enums.UserNotificationStatus.UNREAD;
import static com.example.kamil.user.model.enums.VendorRoleStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VendorRequestService vendorRequestService;
    private final UserNotificationService userNotificationService;
    private final SseService sseService;
    private static final int EXPIRATION_TIME_FOR_REJECTED_REQUEST = 30;
    private static final int EXPIRATION_TIME_FOR_READ_REQUEST = 7;
    private Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private static final String SseVendorRequestUrl = "VENDOR_REQUEST_NOTIFICATIONS";
    private static final String APPROVED_MESSAGE_TO_USER = "Congratulations! Your request to become Vendor is accepted.";
    private static final String REJECTED_MESSAGE_TO_USER = "Unfortunately, your request was rejected. You can send request again after " + EXPIRATION_TIME_FOR_REJECTED_REQUEST +" days";

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
    public void sendRequestForVendorRole(String email) { //todo:Secure these (if you can't secure you can with if statement and throw AccessDeniedException)
        User user = getUserByEmailForUserDetails(email);
        LoggedInUserDetails userDetails = user.getUserDetails();

        if (vendorRequestService.existsByEmail(email)) {
            VendorRequest vendorRequestFromDb = vendorRequestService.findByEmailForLastRequest(email);
            System.out.println(vendorRequestFromDb);

            VendorRoleStatus vendorRoleStatus = vendorRequestFromDb.getVendorRoleStatus();

            //refactorThis:
            // Get the current date

            // Calculate the difference in days between the response time and now
            long daysSinceResponse = ChronoUnit.DAYS.between(vendorRequestFromDb.getRespondedAt(), LocalDateTime.now());

            if(vendorRoleStatus.equals(REQUESTED)){
                throw new IllegalStateException("Your request hasn't read yet! You cannot send request before responding or reading your request! ");
            } else if (vendorRoleStatus.equals(REJECTED) && daysSinceResponse < EXPIRATION_TIME_FOR_REJECTED_REQUEST) {
                throw new IllegalStateException("You cannot send request for " + EXPIRATION_TIME_FOR_REJECTED_REQUEST + " days after rejected");
            }
            else if (vendorRoleStatus.equals(READ) && daysSinceResponse < EXPIRATION_TIME_FOR_READ_REQUEST) {
                throw new IllegalStateException("You cannot send request for "+ EXPIRATION_TIME_FOR_READ_REQUEST +" days after read");
            }

            //todo: for APPROVED we give vendor role and only users can send request to this path not vendors
        }

        VendorRequest vendorRequest = VendorRequest.builder()
                .userDetails(userDetails)
                .createdAt(LocalDateTime.now())
                .respondedAt(LocalDateTime.now()) // if it equals to createdAt , it means there is no response yet
                .vendorRoleStatus(REQUESTED)
                .build();

        //refactorThis

        // For real time notifications add sse
        SseEmitter sseEmitter = sseEmitters.get(SseVendorRequestUrl);

        VendorRequestDTO vendorRequestDTO = vendorRequestService.save(vendorRequest);// save request in db
//refactorThis

        if (sseEmitter != null) {
           log.info("Emitter is not null; there is at least one subscribed user (admin)");
            System.out.println(sseEmitter);
            try {
                sseEmitter.send(SseEmitter.event().name(SseVendorRequestUrl).data(vendorRequestDTO));
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

        // when user connected check if there are no read messages (when user is not online) show
        vendorRequestService.findUnreadMessages().forEach( unreadMsg -> {
             VendorRequestDTO unreadVendorRequestDto = VendorRequestDTO.builder()
                       .email(unreadMsg.getUserDetails().getEmail())
                       .createdAt(unreadMsg.getCreatedAt())
                       .respondedAt(unreadMsg.getRespondedAt())
                       .vendorRoleStatus(unreadMsg.getVendorRoleStatus())
                        .id(unreadMsg.getId())
                       .build();
            try {
                sseEmitter.send(SseEmitter.event().name(SseVendorRequestUrl).data(unreadVendorRequestDto));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        sseOnCompletionTimeoutError(sseEmitter);

        return sseEmitter;
    }

    // refactorThis
    @Override
    public VendorRequestDTO readVendorRequest(Long vendorReqId) {
        VendorRequest vendorRequest = vendorRequestService.getById(vendorReqId);

        vendorRequest.setVendorRoleStatus(READ);
        vendorRequest.setRespondedAt(LocalDateTime.now());
        vendorRequestService.save(vendorRequest);

        return VendorRequestDTOConverter.convert(vendorRequest);
    }

    @Override
    public VendorRequestDTO approveVendorRequest(Long vendorReqId) {
        VendorRequest vendorRequest = vendorRequestService.getById(vendorReqId);

        vendorRequest.setVendorRoleStatus(APPROVED);
        vendorRequest.setRespondedAt(LocalDateTime.now());
        vendorRequestService.save(vendorRequest);

        sendMessageToUser(vendorRequest,APPROVED_MESSAGE_TO_USER);

        // give Vendor role to user
        vendorRequest.getUserDetails().addAuthority(ROLE_VENDOR);

        return VendorRequestDTOConverter.convert(vendorRequest);
    }

    @Override
    public VendorRequestDTO rejectVendorRequest(Long vendorReqId) {
        VendorRequest vendorRequest = vendorRequestService.getById(vendorReqId);

        vendorRequest.setVendorRoleStatus(REJECTED);
        vendorRequest.setRespondedAt(LocalDateTime.now());
        vendorRequestService.save(vendorRequest);

        sendMessageToUser(vendorRequest,REJECTED_MESSAGE_TO_USER);

        return VendorRequestDTOConverter.convert(vendorRequest);
    }

    @Override
    @Transactional
    public SseEmitter listenUserNotificationMessage(String sseEmailUrl) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        sseEmitters.put(sseEmailUrl , sseEmitter);
        try {
            sseEmitter.send(SseEmitter.event().name("Connection opened")); // it is just for ui only event name without data
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // when user connected check if there are no read messages (when user is not online) show
        userNotificationService.findUnreadMessagesByUserEmail(sseEmailUrl).forEach( unreadMsg -> {
            try {
                sseEmitter.send(SseEmitter.event().name(sseEmailUrl).data(unreadMsg));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        sseOnCompletionTimeoutError(sseEmitter);

        return sseEmitter;
    }

    @Override
    public UserNotificationDTO readUserNotification(Long notificationId) {
        UserNotification userNotification = userNotificationService.findById(notificationId);

        userNotification.setStatus(UNREAD);
        userNotificationService.save(userNotification);//it means update

        return  UserNotificationMapper.INSTANCE.convertToDTO(userNotification);
    }

    @Override
    @Transactional
    public List<VendorRequestDTO> getAllVendorRequestsByEmail(String email) {
        User userFromDB = findUserByEmail(email);

        checkUserIsSameWithAuthenticatedUser(email,"You cannot access other user's requests");

        UserUtil.checkIsActive(userFromDB);

        return vendorRequestService.getAllVendorRequestsByUserEmail(email);
    }

    @Override
    public List<VendorRequestDTO> getAllVendorRequests() {
        return  vendorRequestService.getAllVendorRequests();
    }


    //\/\/\/\/\//\//\/\/\/\/\/\/\/\/\/\//\/\/\/\/\/\/\/\/\/\/\/\/\///\//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\\/
    // It must be used with @transactional annotation , because we call lazy obj authenticatedUser.getUser()
    public void checkUserIsSameWithAuthenticatedUser(String email,String exMessage) {
        // Check that is updated user same with current user?
        LoggedInUserDetails authenticatedUser = (LoggedInUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!authenticatedUser.getEmail().equals(email)) {
            throw new PermissionDeniedException(exMessage);
        }
    }

    public LoggedInUserDetails getAuthenticatedUser() {
        return (LoggedInUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

                                    // Util methods \\
    //\/\/\/\/\//\//\/\/\/\/\/\/\/\/\/\//\/\/\/\/\/\/\/\/\/\/\/\/\///\//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\\/
    private void sendMessageToUser(VendorRequest vendorRequest ,String content) {
        String sseUserMsgUrl = vendorRequest.getUserDetails().getEmail();// it can be username , it must be unique identifier
        SseEmitter sseEmitter = sseEmitters.get(sseUserMsgUrl);

        UserNotification userNotification = UserNotification.builder()
                .userDetails(vendorRequest.getUserDetails())
                .status(UNREAD)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        UserNotificationDTO userNotificationDTO = userNotificationService.save(userNotification);

        if (sseEmitter != null) {
            log.info("Emitter is not null; there is at least one subscribed user , Emitter id :" + sseEmitter );

            try {
                sseEmitter.send(SseEmitter.event().name(sseUserMsgUrl).data(userNotificationDTO));
                log.info("UserNotification Message sent to proper destination which is: {}", sseUserMsgUrl);
            } catch (IOException e) {
                log.error("Failed to send event via SseEmitter", e);//todo:onCompletion not working it works only after error (it can be for version incompatibility)
                //throw new SseEmitterSendingException("Failed to send event via SseEmitter", e);
            }
        } else {
            // Handle the case where there are no subscribed users (admins) online
            log.info("No subscribed users online to receive the event");
        }
    }
    private void sseOnCompletionTimeoutError(SseEmitter sseEmitter) {
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
    }

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
