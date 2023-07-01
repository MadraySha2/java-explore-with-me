package server.service;

import dto.HitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.exeption.NotSupportedException;
import server.model.Hit;
import server.model.OutStats;
import server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static server.model.HitMapper.fromDto;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository repository;

    public void saveHit(HitDto hitDto) {
        Hit hit = fromDto(hitDto);
        repository.save(hit);
    }

    public List<OutStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new NotSupportedException("Date should be valid!");
        }
        if (uris == null || uris.isEmpty()) {
            return repository.findAllWithoutUris(start, end);
        }
        if (unique) {
            return repository.findAllUnique(start, end, uris);
        }
        return repository.findAllNotUnique(start, end, uris);
    }

}
