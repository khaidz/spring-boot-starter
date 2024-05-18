package net.khaibq.springbootstater.utils;

/**
 * @author KhaiPC
 * @since 12/11/2022 - 9:15 AM
 */

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import net.khaibq.springbootstater.config.security.CustomUser;
import net.khaibq.springbootstater.exception.BaseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {
    public static Optional<String> getCurrentUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).noneMatch(item -> item.equals("ROLE_ANONYMOUS"));
    }

    public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (
                authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
        );
    }

    public static boolean hasCurrentUserThisAuthority(String authority) {
        return hasCurrentUserAnyOfAuthorities(authority);
    }

    public static Optional<UserDetails> getCurrentUser() {
        val securityContext = SecurityContextHolder.getContext();
        val authentication = securityContext.getAuthentication();
        val principal = authentication.getPrincipal();
        if (principal instanceof CustomUser customUser) {
            return Optional.of(customUser);
        } else if (principal instanceof UserDetails userDetails) {
            return Optional.of(userDetails);
        }
        throw new BaseException("User does not login");
    }


    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (authentication.getPrincipal() instanceof String string) {
            return string;
        }
        return null;
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    }
}

