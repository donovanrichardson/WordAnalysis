package com.word.dao;

import com.word.domain.WordText;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class WordTextDaoDb implements WordTextDao {

    JdbcTemplate jdbc;

    @Override
    public WordText getMostRecentUsage(String s) {
        String sql = "select wt.difference, wt.word_id, wt.text_id, t.time from word_text wt join word w on w.id = wt.word_id join text t on t.id = wt.text_id where w.word = ? sort by t.time desc limit 1";
        WordText mostRecent = jdbc.queryForObject(sql, new WordTextMapper(), s);
        return mostRecent;
    }

    @Override
    public WordText createWordText(WordText wt) {
        String sql = "insert into word_text (difference, word_id, text_id) values (?,?,?)";
        jdbc.update(sql,wt.getDifference(),wt.getWordId(),wt.getTextId());

        String wordSql = "select time from text where id = ?";
        Timestamp wordTextTime = jdbc.queryForObject(wordSql, new Object[]{wt.getWordId()}, Timestamp.class);

        wt.setTime(wordTextTime);

        return wt;
    }

    private static final class WordTextMapper implements RowMapper<WordText> {

        @Override
        public WordText mapRow(ResultSet rs, int i) throws SQLException {

            WordText wt = new WordText();
            wt.setDifference(rs.getDouble("difference"));
            wt.setWordId(rs.getInt("word_id"));
            wt.setTextId(rs.getInt("text_id"));
            wt.setTime(rs.getTimestamp("time"));

            return null;
        }
    }

}
