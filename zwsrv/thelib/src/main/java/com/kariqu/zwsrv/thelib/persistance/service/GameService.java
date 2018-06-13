package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.GameDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by simon on 07/12/17.
 */
@Service
public class GameService extends BaseService<Game> {

    @Autowired
    private GameDAO gameDAO;

    @Override
    protected JpaRepository<Game, Integer> getDao() {
        return gameDAO;
    }

    public List<Game> findGames(int playerId, int result, int page, int size) {
        return gameDAO.findGames(playerId, result, new PageRequest(page, size, Sort.Direction.DESC, "createTime"));
    }

    public List<Game> findGames(int playerId, int page, int size) {
        return gameDAO.findGames(playerId, new PageRequest(page, size, Sort.Direction.DESC, "createTime"));
    }

    public List<Game> findGames(int playerId, List<Integer> results, int page, int size) {
        return gameDAO.findGames(playerId, results.toArray(new Integer[results.size()]), new PageRequest(page, size, Sort.Direction.DESC, "createTime"));
    }

    public List<Game> findGamesDeliverable(int playerId) {
        return gameDAO.findGamesDeliverable(playerId);
    }


    public List<Game> findRoomGames(int roomId, int page, int size) {
        return gameDAO.findRoomGames(roomId, new PageRequest(page, size, Sort.Direction.DESC, "createTime"));
    }

    public List<Game> findRoomGames(int roomId, List<Integer> results, int page, int size) {
        return gameDAO.findRoomGames(roomId, results.toArray(new Integer[results.size()]), new PageRequest(page, size, Sort.Direction.DESC, "createTime"));
    }

}
