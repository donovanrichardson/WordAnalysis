package com.word.dao;

import com.word.domain.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WordDaoDb implements WordDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Word createWord(String s) {

        jdbc.update("insert ignore into word (word) values ()", s);
        Word createdWord = jdbc.queryForObject("select id, word from word where word = ?", new WordMapper(), s);
        return createdWord;
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
