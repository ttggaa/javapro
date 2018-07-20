package gamebox.web.persistence.repository;

import gamebox.web.persistence.entity.UserEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("select l from UserEntity l")
    public List<UserEntity> findAllUser(PageRequest pageRequest);
}
