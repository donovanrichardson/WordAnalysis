package com.word.dao;

import com.word.domain.Word;
import com.word.domain.WordText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class WordTextDaoDb implements WordTextDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public WordText getMostRecentUsage(String s) {
        WordText mostRecent = new WordText();
        try{
            String sql = "select wt.difference, wt.word_id, wt.text_id, t.time from word_text wt join word w on w.id = wt.word_id join text t on t.id = wt.text_id where w.word = ? order by t.time desc limit 1";
            mostRecent = jdbc.queryForObject(sql, new WordTextMapper(), s);
        }catch(NullPointerException | EmptyResultDataAccessException n){
            //
        }
        return mostRecent;
    }

    @Override
    public WordText createWordText(WordText wt) {
        String sql = "insert into word_text (difference, word_id, text_id) values (?,?,?)";
        jdbc.update(sql,wt.getDifference(),wt.getWordId(),wt.getTextId());

        String wordSql = "select time from text where id = ?";
        Timestamp wordTextTime = jdbc.queryForObject(wordSql, new Object[]{wt.getTextId()}, Timestamp.class);

        wt.setTime(wordTextTime);

        return wt;
    }

    @Override
    public List<WordText> getWordTextsWithinInterval(Word w, Timestamp beginTime, Timestamp endTime) {
        String sql = "select word_id, text_id, difference, time from word_text where word_id = ? and time between ? and ? order by time desc";
        List<WordText> res = jdbc.query(sql, new WordTextMapper(), w.getId(), beginTime, endTime);

        return res;
    }

    @Override
    public WordText getPrecedingWordText(Word w, Timestamp beginTime) {
        String sql = "select word_id, text_id, difference, time from word_text where word_id = ? and time between ? and ?";
        WordText res = jdbc.queryForObject(sql, new WordTextMapper(), w.getId(), beginTime);
        return res;
    }

    private static final class WordTextMapper implements RowMapper<WordText> {

        @Override
        public WordText mapRow(ResultSet rs, int i) throws SQLException {

            WordText wt = new WordText();
            wt.setDifference(rs.getDouble("difference"));
            wt.setWordId(rs.getInt("word_id"));
            wt.setTextId(rs.getInt("text_id"));
            wt.setTime(rs.getTimestamp("time"));

            return wt;
        }
    }

}
