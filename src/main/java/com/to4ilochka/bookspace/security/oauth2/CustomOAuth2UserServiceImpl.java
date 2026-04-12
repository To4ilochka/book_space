package com.to4ilochka.bookspace.security.oauth2;

import com.to4ilochka.bookspace.model.Client;
import com.to4ilochka.bookspace.model.User;
import com.to4ilochka.bookspace.model.enums.Role;
import com.to4ilochka.bookspace.repo.ClientRepository;
import com.to4ilochka.bookspace.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setName(name != null ? name : "Google User");
            user.setPassword(UUID.randomUUID().toString());
            user.setLocked(false);
            user.setRoles(Set.of(Role.ROLE_CLIENT));

            Client client = new Client();
            client.setUser(user);
            client.setBalance(BigDecimal.ZERO);

            clientRepository.save(client);
        }

        return oAuth2User;
    }
}