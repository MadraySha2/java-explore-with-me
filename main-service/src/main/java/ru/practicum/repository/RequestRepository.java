package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;
import ru.practicum.model.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByEvent_idAndEvent_Initiator_id(Long eventId, Long userId);

    Long countByEvent_idAndStatus(Long eventId, RequestStatus status);

    List<Request> findByEvent_id(Long eventId);

    Boolean existsByEvent_idAndRequester_id(Long eventId, Long userId);

    List<Request> findByRequester_id(Long eventId);
}
