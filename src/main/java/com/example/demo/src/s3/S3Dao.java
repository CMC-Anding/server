package com.example.demo.src.s3;

//import com.example.demo.src.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class S3Dao {
    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static void createImage(String url, int postId){
        String createImageQuery = "insert into POST_PHOTO (URL, POST_ID) values (?, ?)";
        Object[] createImageParams = new Object[]{url, postId};
        jdbcTemplate.update(createImageQuery, createImageParams);

    }
    
}
