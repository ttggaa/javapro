package gamebox.web.persistence.service;

import gamebox.web.persistence.entity.WebAccountEntity;
import gamebox.web.persistence.repository.WebAccountRepository;
import gamebox.web.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WebAccountService implements UserDetailsService {
    @Autowired
    private WebAccountRepository webAccountRepository;

    public WebAccountRepository getWebAccountRepository() {
        return webAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WebAccountEntity entity = getWebAccountRepository().findFirstByAccount(username);
        if (entity == null)
            throw new UsernameNotFoundException("can't find WebAccount by username");
        return new SecurityUser(entity);
    }
}
