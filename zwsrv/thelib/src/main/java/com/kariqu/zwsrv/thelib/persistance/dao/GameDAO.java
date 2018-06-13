package com.kariqu.zwsrv.thelib.persistance.dao;

import com.kariqu.zwsrv.thelib.persistance.entity.AccountLog;
import com.kariqu.zwsrv.thelib.persistance.entity.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by simon on 07/12/17.
 */
public interface GameDAO extends JpaRepository<Game, Integer> {

    @Query("select l from Game l where l.playerId=:playerId and l.result=:result")
    public List<Game> findGames(@Param("playerId") int playerId, @Param("result") int result, Pageable pageable);

    @Query("select l from Game l where l.playerId=:playerId")
    public List<Game> findGames(@Param("playerId") int playerId, Pageable pageable);

    @Query("select l from Game l where l.playerId=:playerId and l.result in :results")
    public List<Game> findGames(@Param("playerId") int playerId, @Param("results") Integer[] results, Pageable pageable);

    @Query("select l from Game l where l.playerId=:playerId and l.result=1 and l.isExchange=0 and l.isDelivery=0 order by createTime desc")
    public List<Game> findGamesDeliverable(@Param("playerId") int playerId);

    @Query("select l from Game l where l.roomId=:roomId")
    public List<Game> findRoomGames(@Param("roomId") int roomId, Pageable pageable);

    @Query("select l from Game l where l.roomId=:roomId and l.result in :results")
    public List<Game> findRoomGames(@Param("roomId") int roomId, @Param("results") Integer[] results, Pageable pageable);

}

