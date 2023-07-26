package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CommentAdminDto;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.CommentEntryDto;
import ru.practicum.dto.SortValue;
import ru.practicum.exception.NotAvailableException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.CommentLike;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentLikeRepository;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.CommentMapper.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final EventRepository events;
    private final UserRepository users;
    private final CommentLikeRepository likes;

    public CommentDto addComment(Long userId, Long eventId, CommentEntryDto entryDto) {
        User user = users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found!"));
        Event event = events.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event Not Found!"));
        Comment comment = fromEntryComment(entryDto);
        comment.setCreated(LocalDateTime.now());
        comment.setEvent(event);
        comment.setSender(user);
        return toCommentDto(repository.save(comment));
    }

    public CommentDto updateComment(Long userId, Long commentId, CommentEntryDto entryDto) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment Not Found!"));
        if (userId == null || comment.getSender().getId().longValue() != userId.longValue()) {
            throw new NotAvailableException("Only sender can update!");
        }
        if (entryDto.getContent() != null) {
            comment.setContent(entryDto.getContent());
            comment.setUpdated(LocalDateTime.now());
        }
        return setUsefulness(toCommentDto(repository.save(comment)));
    }

    public CommentAdminDto getAdminCommentById(Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment Not Found!"));
        return setUsefulnessAdmin(toAdminDto(comment));
    }

    public CommentDto getCommentById(Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment Not Found!"));
        return setUsefulness(toCommentDto(comment));
    }

    public void deleteCommentAdmin(Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment Not Found!"));
        repository.deleteById(commentId);
    }

    public void deleteComment(Long userId, Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment Not Found!"));
        if (userId == null || comment.getSender().getId().longValue() != userId.longValue()) {
            throw new NotAvailableException("Only sender or admin can delete it!");
        }
        repository.deleteById(commentId);
    }

    public List<CommentDto> getCommentsByEvent(Long eventId, SortValue sortValue, Boolean asc, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created"));
        if (sortValue.equals(SortValue.DATE)) {
            if (asc) {
                return repository.findAllByEventId(eventId, pageable).stream()
                        .map(CommentMapper::toCommentDto).map(this::setUsefulness).collect(Collectors.toList());
            }
            pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
            return repository.findAllByEventId(eventId, pageable).stream()
                    .map(CommentMapper::toCommentDto).map(this::setUsefulness).collect(Collectors.toList());
        } else if (sortValue.equals(SortValue.USEFULNESS)) {
            List<CommentDto> comments = repository.findAllByEventId(eventId, pageable)
                    .stream().map(CommentMapper::toCommentDto).map(this::setUsefulness).collect(Collectors.toList());
            return setSortByUsefulness(comments, asc);
        }
        throw new NotAvailableException("No such sort");
    }

    public List<CommentDto> getCommentsByUser(Long userId, SortValue sortValue, Boolean asc,
                                              Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created"));
        if (sortValue.equals(SortValue.DATE)) {
            if (asc) {
                return repository.findAllBySenderId(userId, pageable).stream()
                        .map(CommentMapper::toCommentDto).map(this::setUsefulness).collect(Collectors.toList());
            } else {
                pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
                return repository.findAllBySenderId(userId, pageable).stream()
                        .map(CommentMapper::toCommentDto).map(this::setUsefulness).collect(Collectors.toList());
            }
        } else if (sortValue.equals(SortValue.USEFULNESS)) {
            List<CommentDto> comments = repository.findAllBySenderId(userId, pageable)
                    .stream().map(CommentMapper::toCommentDto).map(this::setUsefulness).collect(Collectors.toList());
            return setSortByUsefulness(comments, asc);
        }
        throw new NotAvailableException("No such sort");
    }

    public List<CommentDto> getCommentsByEventInitiator(Long userId, SortValue sortValue, Boolean asc,
                                                        Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created"));
        if (sortValue.equals(SortValue.DATE)) {
            if (asc) {
                return repository.findAllByEventInitiatorId(userId, pageable).stream()
                        .map(CommentMapper::toCommentDto).map(this::setUsefulness).collect(Collectors.toList());
            }
            pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
            return repository.findAllByEventInitiatorId(userId, pageable).stream()
                    .map(CommentMapper::toCommentDto).map(this::setUsefulness).collect(Collectors.toList());
        } else if (sortValue.equals(SortValue.USEFULNESS)) {
            List<CommentDto> comments = repository.findAllByEventInitiatorId(userId, pageable)
                    .stream().map(CommentMapper::toCommentDto).map(this::setUsefulness).collect(Collectors.toList());
            return setSortByUsefulness(comments, asc);
        }
        throw new NotAvailableException("No such sort");
    }

    /* мб будет не так понятно,но здесь при наличии лайка, он снимается при повторном нажатии,
     * а если дизлайк, то поменяется на лайк. Это нужно, чтобы не было варианта, при котором
     * юзер ставит лайк, потом меняет на дизлайк и лайк остается и дизлайк создается -> рейтинг был +1, а стал +1-1=0
     * в моем варианте, если юзер ставит лайк, а потом нажимает на дизлайк, то по сути, рейтинг будет -1, в общем, по формату
     * как на youTube. => Мы можем убрать лайк, либо отзеркалить его */
    public CommentDto likeComment(Long userId, Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment Not Found!"));
        CommentLike like = likes.findByCommentIdAndUserId(commentId, userId);
        /*Соотв. тут он проверяет, если это лайк, то он его убирает*/
        if (like != null && like.getIslike().equals(true)) {
            likes.deleteById(like.getId());
            return setUsefulness(toCommentDto(comment));
        } /*А если дизлайк, то реверсит*/ else if (like != null && like.getIslike().equals(false)) {
            return reverseLike(comment, like);
        }
        return addNewLike(comment, userId);
    }

    public CommentDto dislikeComment(Long userId, Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment Not Found!"));
        CommentLike like = likes.findByCommentIdAndUserId(commentId, userId);
        if (like != null && like.getIslike().equals(false)) {
            likes.deleteById(like.getId());
            return setUsefulness(toCommentDto(comment));
        } else if (like != null && like.getIslike().equals(true)) {
            return reverseLike(comment, like);
        }
        return addNewDislike(comment, userId);
    }

    private CommentDto reverseLike(Comment comment, CommentLike like) {
        like.setIslike(!like.getIslike());
        likes.save(like);
        return setUsefulness(toCommentDto(comment));
    }

    private CommentDto addNewLike(Comment comment, Long userId) {
        if (comment.getSender().getId().longValue() == userId.longValue()) {
            throw new NotAvailableException("");
        }
        likes.save(CommentLike.builder().commentId(comment.getId()).userId(userId).islike(true).build());
        return setUsefulness(toCommentDto(comment));
    }

    private CommentDto addNewDislike(Comment comment, Long userId) {
        if (comment.getSender().getId().longValue() == userId.longValue()) {
            throw new NotAvailableException("");
        }
        likes.save(CommentLike.builder().commentId(comment.getId()).userId(userId).islike(false).build());
        return setUsefulness(toCommentDto(comment));
    }

    private CommentDto setUsefulness(CommentDto commentDto) {
        Long like = likes.countByCommentIdAndIslike(commentDto.getId(), true);
        Long dislike = likes.countByCommentIdAndIslike(commentDto.getId(), false);
        commentDto.setUsefulness(like - dislike);
        return commentDto;
    }

    private CommentAdminDto setUsefulnessAdmin(CommentAdminDto commentDto) {
        Long like = likes.countByCommentIdAndIslike(commentDto.getId(), true);
        Long dislike = likes.countByCommentIdAndIslike(commentDto.getId(), false);
        commentDto.setUsefulness(like - dislike);
        return commentDto;
    }

    private List<CommentDto> setSortByUsefulness(List<CommentDto> comments, Boolean asc) {
        if (asc) {
            return comments.stream()
                    .sorted(Comparator.comparing(CommentDto::getUsefulness)).collect(Collectors.toList());
        }
        return comments.stream()
                .sorted(Comparator.comparing(CommentDto::getUsefulness).reversed()).collect(Collectors.toList());
    }
}
