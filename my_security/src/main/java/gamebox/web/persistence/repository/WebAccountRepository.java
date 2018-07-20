package gamebox.web.persistence.repository;

import gamebox.web.persistence.entity.WebAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebAccountRepository extends JpaRepository<WebAccountEntity, Integer> {

    public WebAccountEntity findFirstByAccount(String account);
}
