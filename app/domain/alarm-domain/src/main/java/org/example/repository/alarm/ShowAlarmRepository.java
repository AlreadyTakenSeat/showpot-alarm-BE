package org.example.repository.alarm;

import java.util.Optional;
import java.util.UUID;
import org.example.entity.ShowAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowAlarmRepository extends JpaRepository<ShowAlarm, UUID>,
    ShowAlarmQuerydslRepository {

    Optional<ShowAlarm> findByIdAndIsDeletedFalse(UUID id);

    boolean existsByUserFcmTokenAndCheckedFalse(String fcmToken);

}
