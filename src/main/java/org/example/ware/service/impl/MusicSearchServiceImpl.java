package org.example.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.ware.dto.MusicSearchDTO;
import org.example.ware.entity.Song;
import org.example.ware.mapper.SongMapper;
import org.example.ware.service.MusicSearchService;
import org.springframework.stereotype.Service;

@Service
public class MusicSearchServiceImpl extends ServiceImpl<SongMapper, Song> implements MusicSearchService {
    @Resource
    private SongMapper songMapper;

    @Override
    public Page<Song> searchMusic(MusicSearchDTO dto) {
        Page<Song> page = new Page<>(dto.getCurrent(), dto.getSize());
        // 调用自定义的关联查询方法
        return songMapper.searchMusicWithSinger(page, "%" + dto.getKeyword() + "%");
    }
}
