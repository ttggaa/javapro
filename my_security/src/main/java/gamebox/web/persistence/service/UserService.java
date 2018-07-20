package gamebox.web.persistence.service;

import gamebox.web.persistence.entity.UserEntity;
import gamebox.web.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public long count() {
        return getUserRepository().count();
    }

    public List<UserEntity> findAllUser(PageRequest pageRequest) {
        return getUserRepository().findAllUser(pageRequest);
    }
}
