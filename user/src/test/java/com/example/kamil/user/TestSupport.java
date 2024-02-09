package com.example.kamil.user;

import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.utils.converter.UserDTOConverter;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestSupport {

    protected PrivateKey preparePrivateKey(String privateKeyStr) {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.getDecoder().decode(privateKeyStr)
            );
            return kf.generatePrivate(keySpecPKCS8);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected PublicKey preparePublicKey(String publicKeyStr)  {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
            return kf.generatePublic(keySpecX509);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User generateUser(String mail){
        return User.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .email(mail)
                .username("username")
                .isActive(true)
                .build();
    }

    public static UserDTO generateUserDto(User user){
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
    public static List<User> generateUserList(){
       return IntStream.range(0,5).mapToObj( i ->
                    User.builder()
                            .id((long) i)
                            .firstName("firstname" + i)
                            .lastName("lastname" + i)
                            .email("email" + i)
                            .username("username" + i)
                            .isActive(new Random().nextBoolean())
                            .build()
                ).collect(Collectors.toList());
    }


    public static List<UserDTO> generateUserDtoList( List<User> userList){
      return  userList.stream().map(UserDTOConverter::convert).collect(Collectors.toList());
    }
}
