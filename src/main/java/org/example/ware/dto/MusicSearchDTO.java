package org.example.ware.dto;



import lombok.Data;

@Data
public class MusicSearchDTO {
    private String keyword; // 搜索关键词（歌曲名/歌手名）
    private long current;   // 当前页（MyBatis-Plus 分页参数）
    private long size;      // 每页条数
}
