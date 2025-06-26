package org.example.ware.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.ware.entity.Song;
import org.example.ware.entity.User;

import java.util.List;

public interface SongMapper extends BaseMapper<Song> {
    // 自定义 SQL 实现关联查询，同时查询出歌手名称
    @Select("""
        SELECT s.*, si.singer_name 
        FROM song s 
        LEFT JOIN singer si ON s.singer_id = si.singer_id 
        WHERE s.song_name LIKE #{keyword} OR si.singer_name LIKE #{keyword}
        """)
    Page<Song> searchMusicWithSinger(Page<Song> page, @Param("keyword") String keyword);

    @Select("SELECT * FROM song WHERE singer_id = #{singerId} ORDER BY create_time DESC")
    List<Song> selectBySingerId(Long singerId);
}
