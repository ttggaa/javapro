package gamebox.web.security;

import gamebox.web.persistence.entity.WebAccountEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class SecurityUser implements UserDetails {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUser.class);

    private String username;
    private String password;
    private String roles;
    private HashSet<SimpleGrantedAuthority> set;

    public SecurityUser(WebAccountEntity entity) {
        this.username = entity.getAccount();
        this.password = entity.getPassword();
        set = new HashSet<>();
        set.add(new SimpleGrantedAuthority(entity.getRoles()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return set;
    }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public String getUsername() { return this.username; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
