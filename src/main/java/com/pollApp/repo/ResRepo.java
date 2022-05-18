package com.pollApp.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.pollApp.model.Poll;
import com.pollApp.model.Results;

public interface ResRepo extends CrudRepository<Results, Long> {
    // @Query("select qd from QuestionDetails qd where qd.department = :#{#req.
    // department} and qd.year = :#{#req.year}")
    // @Query(value = "select * from results rs where rs.chosen=:#{#req.chosen} and rs.pid= :#{#req.pid}", nativeQuery = true)
    // Optional<Results> selected(@Param("req") Results req);

    

    @Query(value = "select MAX(points),chosen from results where poll_id = ?1 group by chosen",
        nativeQuery = true
    )
    List<Results> fetchHighest(Long poll_id);
    
    @Query( value = "select * from results where points = ?1 and poll_id= ?2", nativeQuery = true )
    List <Results> fetchHighestPoints(int points, Long poll_id);

    @Query(
        value = "select * from results where  poll_id  = ?1", nativeQuery = true
    )
    List<Results> fetchByPid(Long poll_id);

    List<Results> findByPid(Long poll_id);

    @Query(
        value = "select points from results where chosen = ?1 and poll_id  = ?2", nativeQuery = true
    )
   int fetchPointsforChosen(String chosen, Long poll_id);


   List<Results> findByChosenAndPid (String chosen, Long poll_id);
    // Results findByChosen(String chosen, Long id);

    @Transactional
    @Modifying
    @Query("update results set points = points + 1 where chosen=?1 and poll_id = ?2")
    void incrementVotes(String chosen, Long result_id);

    // @Query("update results set points = points - 1 where id = ?1 and chosen =
    // ?2")
    // public void decrementVotes(Long id,String chosen);

    // Optional<Results> findById(Long id);

}
