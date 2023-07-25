package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndSenderId(Long commentId, Long ownerId);

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);

    List<Comment> findAllBySenderId(Long eventId, Pageable pageable);

    List<Comment> findAllByEventInitiatorId(Long eventId, Pageable pageable);

}
