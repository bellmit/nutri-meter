package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.MicroTarget;

import java.util.Optional;

@Repository
public interface MicroTargetRepository extends JpaRepository<MicroTarget, String> {

    Optional<MicroTarget> findByLifeStageGroupId(String id);

    @Query("FROM MicroTarget AS m LEFT JOIN m.users AS u WHERE u.id = :userId")
    MicroTarget findByUserId(@Param("userId") String id);
}
