package ru.practicum.mapper;

import ru.practicum.dto.CommentAdminDto;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.CommentEntryDto;
import ru.practicum.model.Comment;

import java.time.format.DateTimeFormatter;

import static ru.practicum.mapper.EventMapper.toEventShortDto;
import static ru.practicum.mapper.UserMapper.toShortDto;

public class CommentMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .created(comment.getCreated().format(formatter))
                .updated(comment.getUpdated() != null ? comment.getUpdated().format(formatter) : null)
                .sender(comment.getSender().getId())
                .event(comment.getEvent().getId())
                .content(comment.getContent())
                .usefulness(0L)
                .build();
    }

    public static Comment fromEntryComment(CommentEntryDto comment) {
        return Comment.builder()
                .content(comment.getContent())
                .build();
    }

    public static CommentAdminDto toAdminDto(Comment comment) {
        return CommentAdminDto.builder()
                .id(comment.getId())
                .created(comment.getCreated().format(formatter))
                .updated(comment.getUpdated() != null ? comment.getUpdated().format(formatter) : null)
                .sender(toShortDto(comment.getSender()))
                .event(toEventShortDto(comment.getEvent()))
                .content(comment.getContent())
                .usefulness(0L)
                .build();
    }
}
