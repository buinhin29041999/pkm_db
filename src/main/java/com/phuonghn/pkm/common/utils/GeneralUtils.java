package com.phuonghn.pkm.common.utils;

import com.weblaptop.wsl_service.entity.Role;
import com.weblaptop.wsl_service.security.RefreshTokenService;
import com.weblaptop.wsl_service.service.dto.UserDetailsImpl;
import com.weblaptop.wsl_service.service.dto.oauth.SocialProvider;
import com.weblaptop.wsl_service.service.dto.response.AuthRes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GeneralUtils {
    public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<Role> roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getCode().toString()));
        }
        return authorities;
    }

    public static SocialProvider toSocialProvider(String providerId) {
        for (SocialProvider socialProvider : SocialProvider.values()) {
            if (socialProvider.getProviderType().equals(providerId)) {
                return socialProvider;
            }
        }
        return SocialProvider.LOCAL;
    }

    public static AuthRes buildUserInfo(String accessToken) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return new AuthRes(accessToken.substring(7),
                BeanUtils.getBean(RefreshTokenService.class).createRefreshToken(userDetails).getRefreshToken(),
                roles,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(), null);
    }
}
