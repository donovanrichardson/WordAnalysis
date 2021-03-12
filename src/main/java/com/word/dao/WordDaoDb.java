package com.word.dao;

import com.word.domain.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WordDaoDb implements WordDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Word createWord(String s) {

        jdbc.update("insert ignore into word (word) values (?)", s);
        Word createdWord = jdbc.queryForObject("select id, word from word where word = ?", new WordMapper(), s);
        return createdWord;
    }

    @Override
    public Word getQualifyingWord(int wordId, int scoreRange, Timestamp endTime) {
        long beginningMillis = endTime.getTime() - (scoreRange * 1000);
        Timestamp beginningTime = new Timestamp(beginningMillis);

        String sql = "select w.id, w.word from word w join word_text wt on w.id = wt.word_id join text t on t.id = wt.text_id where t.time > ? and w.id = ?";

        try{
            Word res = jdbc.queryForObject(sql, new WordMapper(), beginningTime, wordId);
            return res;
        }catch(NullPointerException | EmptyResultDataAccessException e){
            return null;
        }

    }

    @Override
    public List<Word> getQualifyingWords(int scoreRange, Timestamp endTime) {
        List<Word> res = new ArrayList<>();
        long beginningMillis = endTime.getTime() - (scoreRange * 1000);
        Timestamp beginningTime = new Timestamp(beginningMillis);
        String sql = "select w.id, w.word from word w join word_text wt on w.id = wt.word_id join text t on t.id = wt.text_id where t.time > ?";

        try{
            res = jdbc.query(sql, new WordMapper(), beginningTime);
            return res;
        }catch(NullPointerException | EmptyResultDataAccessException e){
            return res;
        }
    }

    private static final class WordMapper implements RowMapper<Word>{

        @Override
        public Word mapRow(ResultSet rs, int i) throws SQLException {
            Word w = new Word();
            w.setWord(rs.getString("word"));
            w.setId(rs.getInt("id"));
            return w;
        }
    }

}
