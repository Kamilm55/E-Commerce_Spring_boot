package com.example.kamil.user.service.impl;

import com.example.kamil.user.model.dto.LoggedInUserDetailsDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.entity.security.LoggedInUserDetails;
import com.example.kamil.user.model.enums.Role;
import com.example.kamil.user.model.payload.UpdateUserDetailsPayload;
import com.example.kamil.user.repository.LoggedInUserDetailsRepository;
import com.example.kamil.user.service.LoggedInUserDetailsService;
import com.example.kamil.user.service.UserService;
import com.example.kamil.user.utils.UserUtil;
import com.example.kamil.user.utils.converter.LoggedInUserDetailsDTOConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LoggedInUserDetailsServiceImpl implements LoggedInUserDetailsService {
    private final LoggedInUserDetailsRepository userDetailsRepository;
    private final UserService userService;

    @Override
    public LoggedInUserDetails getUserDetailsForSecurity(User user) {
        return userDetailsRepository.findByUserEmail(user.getEmail());
    }

    @Override
    @Transactional
    public LoggedInUserDetailsDTO getUserDetailsByEmail(String email) {
        LoggedInUserDetails userDetails = getUserDetails(email);
        checkActive(email);

        LoggedInUserDetails authenticatedUser = userService.getAuthenticatedUser();

        if (!authenticatedUser.getAuthorities().contains(Role.ROLE_ADMIN)) {
            userService.checkUserIsSameWithAuthenticatedUser(email,"You cannot get other user information!");
        }

        return LoggedInUserDetailsDTOConverter.convert(userDetails);
    }

    @Override
    @Transactional
    public LoggedInUserDetailsDTO updateUserUserDetails(String email, UpdateUserDetailsPayload updateUserDetailsPayload) {
        LoggedInUserDetails userDetails = getUserDetails(email);

        checkActive(email);
        userService.checkUserIsSameWithAuthenticatedUser(email,"You cannot update  other user information!");
        setUserDetails(updateUserDetailsPayload, userDetails);

        return LoggedInUserDetailsDTOConverter.convert(userDetailsRepository.save(userDetails));
    }

    @Override
    @Transactional
    public void addAdminRole(String email) {
        checkActive(email);
        addSpecificRole(email,Role.ROLE_ADMIN);
    }

    @Override
    @Transactional
    public void addSuperAdminRole(String email) {
        checkActive(email);
        addSpecificRole(email,Role.ROLE_SUPER_ADMIN);
    }

    @Override
    @Transactional
    public void deleteAdminRole(String email) {
        checkActive(email);
        deleteSpecificRole(email,Role.ROLE_ADMIN);
    }
    @Override
    @Transactional
    public void addVendorRole(String email) {
        checkActive(email);
        addSpecificRole(email,Role.ROLE_VENDOR);
    }

    @Override
    @Transactional
    public void deleteVendorRole(String email) {
        checkActive(email);
        deleteSpecificRole(email,Role.ROLE_VENDOR);
    }

    @Override
    @Transactional
    public void requestForVendorRole(String email) { //TODO: security config =>only ROlE.USER
        checkActive(email);
        userService.checkUserIsSameWithAuthenticatedUser(email,"You cannot send request for other user information!");
        // we sure that user sends request for own and user is active

        //todo: check isRequestRejected (for db) and if application is valid then continue

        //todo:rabbit mq sends notifications to all admins , if any admin accept role must be Vendor
    }

    @Override
    public void approveVendorRequest(String email) {
        //todo: implement
    }

    @Override
    public void rejectVendorRequest(String email) {
        //todo: implement
    }

    //
    private void checkActive(String email) {
        User user = userService.findUserByEmail(email);
        UserUtil.checkIsActive(user);
    }
    private void deleteSpecificRole(String email,Role role) {
        modifyUserRole(email,role,false);
    }
    private void addSpecificRole(String email, Role role) {
        modifyUserRole(email,role,true);
    }
    private void modifyUserRole(String email, Role role, boolean addRole) {
        LoggedInUserDetails userDetails = getUserDetails(email);

        if (addRole) { // true == add , false == delete
            userDetails.addAuthority(role);
        } else {
            userDetails.deleteAuthority(role);
        }

        userDetailsRepository.save(userDetails);
    }
    private LoggedInUserDetails getUserDetails(String email) {
        User user = userService.findUserByEmail(email);
        return user.getUserDetails();
    }
    private void setUserDetails(UpdateUserDetailsPayload updateUserDetailsPayload, LoggedInUserDetails userDetails) {
        userDetails.setAddress(updateUserDetailsPayload.getAddress());
        userDetails.setCity(updateUserDetailsPayload.getCity());
        userDetails.setCountry(updateUserDetailsPayload.getCountry());
        userDetails.setPostCode(updateUserDetailsPayload.getPostCode());
        userDetails.setPhoneNumber(updateUserDetailsPayload.getPhoneNumber());
    }


//    @Override
//    public void insertUserDetails(CreateUserDetailsPayload payload) {
//        LoggedInUserDetails userDetails = LoggedInUserDetails.builder()
//                .user(payload.getUser())
//                .authorities(payload.getAuthorities())
//                .build();
//
//        userDetailsRepository.save(userDetails);
//    }

    // Test \\
//    @Override
//    @Transactional // It makes active hibernate session , we cannot get lazy init obj without this
//    public LoggedInUserDetails getUserDetails(User user) {
//        User userFromDb = userService.findUserByEmail(user.getEmail());
//
//       // System.out.println(userFromDb.getUserDetails());
//        //todo: why when i print it works otherwise not?
//        LoggedInUserDetails userDetails = userFromDb.getUserDetails();
//        return userDetails;
//    }

}
