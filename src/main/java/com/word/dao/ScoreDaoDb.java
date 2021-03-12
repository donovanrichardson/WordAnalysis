package com.word.dao;

import com.word.domain.Score;
import com.word.domain.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScoreDaoDb implements ScoreDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<Score> saveScores(List<Score> scores) {

        List<Score> res = new ArrayList<>();
        for(Score s : scores){
            res.add(saveScore(s));
        }
        return res;
    }

    @Override
    @Transactional
    public Score saveScore(Score s) {
        String sql = "insert into score (word_id, value, ratio, begin, end) values (?,?,?,?,?)";

        jdbc.update(sql, s.getWord().getId(), s.getValue(), s.isRatio(), s.getBegin(), s.getEnd());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        s.setId(newId);
        return s;
    }


    @Override
    public Score getRecentScoreForWord(Word w, boolean ratio, Timestamp beginTime, Timestamp endTime) {
        String sql = "select id, ratio, begin, end, value from score where word_id = ? and ratio = ? and begin = ? and end = ?  order by end desc limit 1";
        try{
            Score s = jdbc.queryForObject(sql, new ScoreMapper(), w.getId(), ratio, beginTime, endTime);
            s.setWord(w);
            return s;
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    private static final class ScoreMapper implements RowMapper<Score>{

        @Override
        public Score mapRow(ResultSet rs, int i) throws SQLException {
            Score s = new Score();

            s.setRatio(rs.getBoolean("ratio"));
            s.setId(rs.getInt("id"));
            s.setBegin(rs.getTimestamp("begin"));
            s.setEnd(rs.getTimestamp("end"));
            s.setValue(rs.getDouble("value"));

            return s;

        }
    }

}
