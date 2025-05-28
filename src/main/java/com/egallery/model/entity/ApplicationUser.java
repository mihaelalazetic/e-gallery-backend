
package com.egallery.model.entity;

import com.egallery.model.dto.ApplicationUserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser extends BaseEntity implements UserDetails {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String fullName;

    @Column(length = 1024)
    private String bio;

    private String password;

    private String slug;
    private String profilePictureUrl;

    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive != null && isActive; // or just return true
    }

    public ApplicationUserDTO mapToDto() {
        return mapToDto(null, null, null,null);
    }

    public ApplicationUserDTO mapToDto(Long totalLikes, Long followerCount, Boolean isFollowing, Long artCount) {
        // Convert each Role into its name (e.g. "USER", "ADMIN")
        Set<String> roleNames = getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return ApplicationUserDTO.builder()
                .id(getId())
                .username(getUsername())
                .fullName(getFullName())
                .email(getEmail())
                .bio(getBio())
                .profilePictureUrl(getProfilePictureUrl())
                .roles(roleNames)      // now a Set<String>
                .totalLikes(totalLikes)
                .followerCount(followerCount)
                .isFollowing(isFollowing)
                .artCount(artCount)
                .slug(getSlug())
                .build();
    }


}
