package com.bookrecommendationsystem.recommendation.repository;

import com.bookrecommendationsystem.recommendation.domain.Recommendation;
import com.bookrecommendationsystem.recommendation.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RecommendationRepository extends CrudRepository<Recommendation, String> {
    List<Recommendation> findAllByUser(User user);
    @Transactional
    void deleteByUser(User user);
}
