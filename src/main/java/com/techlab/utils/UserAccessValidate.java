package com.techlab.utils;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAccessValidate {

    public static boolean isUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        }
        return false;
    }

    public static boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Long) {
            return (Long) auth.getPrincipal();
        }
        return null;
    }


    public static void validateUserAccess(Long targetUserId) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Authentication required");
        }
        // Admin can access all, USER can only access their own profile
        if (!isAdmin() && !targetUserId.equals(currentUserId)) {
            throw new AccessDeniedException("Access denied: insufficient permissions");
        }
    }

}
