package com.word.dao;

import com.word.domain.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TextDaoDb implements TextDao {

    public static final class TextMapper implements RowMapper<Text>{

        @Override
        public Text mapRow(ResultSet rs, int i) throws SQLException {
            Text t = new Text();
            t.setAuthor(rs.getString("author"));
            t.setBody(rs.getString("body"));
            t.setId(rs.getInt("id"));
            t.setLink(rs.getString("link"));
            t.setTime(rs.getTimestamp("date"));

            return null;
        }
    }

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Text createText(Text t) {
        String q = "insert into text (author, body, link, time) values (?,?,?,?)";
        jdbc.update(q, t.getAuthor(), t.getBody(), t.getLink(), t.getTime());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        t.setId(newId);

        return t;
    }

    @Override
    public List<Text> getTexts() {
        String q = "select id, author, body, link, time from text";
        List<Text> texts = jdbc.query(q,new TextMapper());
        return texts;
    }

    @Override
    public Text getTextById(int id) {
        String q = "select id, author, body, link, time from text where id = ?";
        Text res = jdbc.queryForObject(q, new TextMapper(), id);
        return res;
    }

    @Override
    public List<Text> getTextsByAuthor(String author) {
        String q = "select id, author, body, link, time from text where author = ?";
        List<Text> texts = jdbc.query(q,new TextMapper(), author);
        return texts;
    }
}
