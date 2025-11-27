package SE.demo.service.recommendation;

import SE.demo.dto.ott.OttDto;
import SE.demo.dto.recommendation.DateRangeDto;
import SE.demo.dto.recommendation.RecommendationActionDto;
import SE.demo.dto.recommendation.RecommendationResponseDto;
import SE.demo.dto.wishlist.WishlistProgramDto;
import SE.demo.entity.User;
import SE.demo.repository.ott.OttRepository;
import SE.demo.repository.programs.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private static final double EXPIRATION_WEIGHT_FACTOR = 0.5;
    private static final int COVERAGE_SCORE_FACTOR = 10000;
    private final ProgramRepository programRepository;
    private final OttRepository ottRepository;

    public RecommendationResponseDto getOptimalSchedule(User user) {
        List<WishlistProgramDto> wishlist = programRepository.findWishlistProgramsByUserId(user.getUserId());
        List<OttDto> otts = ottRepository.getOttList();
        Map<Long, OttDto> ottMap = otts.stream().collect(Collectors.toMap(o -> (long) o.getOttId(), o -> o));

        List<RecommendationActionDto> actions = new ArrayList<>();
        int totalRecommendedCost = 0;

        Set<Long> remainingProgramIds = wishlist.stream()
                .map(WishlistProgramDto::getProgramId)
                .collect(Collectors.toSet());

        LocalDate currentDate = LocalDate.now();

        while (!remainingProgramIds.isEmpty()) {
            Map<Long, Double> ottScores = new HashMap<>();
            Map<Long, List<WishlistProgramDto>> programsByOttInPeriod = new HashMap<>();

            final LocalDate loopCurrentDate = currentDate;
            LocalDate periodEndDate = loopCurrentDate.plusMonths(1);

            for (WishlistProgramDto program : wishlist) {
                if (!remainingProgramIds.contains(program.getProgramId())) continue;

                program.getAvailabilities().forEach(availability -> {
                    LocalDate releaseDate = availability.getReleaseDate() != null ? availability.getReleaseDate() : loopCurrentDate;
                    LocalDate expireDate = availability.getExpireDate() != null ? availability.getExpireDate() : loopCurrentDate.plusYears(1);

                    if (loopCurrentDate.isBefore(expireDate) && periodEndDate.isAfter(releaseDate)) {
                        long ottId = availability.getOttId();
                        programsByOttInPeriod.computeIfAbsent(ottId, k -> new ArrayList<>()).add(program);
                    }
                });
            }

            if (programsByOttInPeriod.isEmpty()) {
                currentDate = currentDate.plusMonths(1);
                if (currentDate.isAfter(LocalDate.now().plusYears(2))) {
                    break;
                }
                continue;
            }

            for (Long ottId : programsByOttInPeriod.keySet()) {
                OttDto ott = ottMap.get(ottId);
                if (ott == null) continue;

                List<WishlistProgramDto> programsToWatch = programsByOttInPeriod.get(ottId);
                double coverage = (double) programsToWatch.size() / remainingProgramIds.size();

                double expirationUrgency = programsToWatch.stream()
                        .mapToDouble(p -> p.getAvailabilities().stream()
                                .filter(a -> a.getOttId().equals(ottId) && a.getExpireDate() != null)
                                .mapToLong(a -> ChronoUnit.DAYS.between(loopCurrentDate, a.getExpireDate()))
                                .mapToDouble(days -> days > 0 ? 1.0 / days : 1.0)
                                .sum())
                        .sum();

                double weight = 1 + EXPIRATION_WEIGHT_FACTOR * expirationUrgency;
                double score = (coverage / ott.getPrice()) * COVERAGE_SCORE_FACTOR + weight;
                ottScores.put(ottId, score);
            }

            Optional<Map.Entry<Long, Double>> bestOttEntry = ottScores.entrySet().stream()
                    .max(Map.Entry.comparingByValue());

            if (bestOttEntry.isPresent()) {
                long bestOttId = bestOttEntry.get().getKey();
                OttDto bestOtt = ottMap.get(bestOttId);
                List<WishlistProgramDto> programsForBestOtt = programsByOttInPeriod.get(bestOttId);

                RecommendationActionDto action = new RecommendationActionDto();
                action.setOttName(bestOtt.getName());
                action.setDateRange(new DateRangeDto(loopCurrentDate, periodEndDate.minusDays(1)));
                action.setPrograms(programsForBestOtt);
                actions.add(action);

                totalRecommendedCost += bestOtt.getPrice();
                programsForBestOtt.forEach(p -> remainingProgramIds.remove(p.getProgramId()));

                currentDate = periodEndDate;
            } else {
                break;
            }
        }

        int totalOriginalCost = calculateSimplifiedOriginalCost(actions, otts);
        int savings = Math.max(0, totalOriginalCost - totalRecommendedCost);
        return new RecommendationResponseDto(savings, actions);
    }

    private int calculateSimplifiedOriginalCost(List<RecommendationActionDto> actions, List<OttDto> allOtts) {
        if (actions.isEmpty()) {
            return 0;
        }

        int durationInMonths = actions.size();

        Set<String> recommendedOttNames = actions.stream()
                .map(RecommendationActionDto::getOttName)
                .collect(Collectors.toSet());

        int monthlyCostOfAllRecommendedOtts = allOtts.stream()
                .filter(ott -> recommendedOttNames.contains(ott.getName()))
                .mapToInt(OttDto::getPrice)
                .sum();

        return monthlyCostOfAllRecommendedOtts * durationInMonths;
    }
}
