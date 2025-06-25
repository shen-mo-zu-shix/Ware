package org.example.ware.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.ware.dto.MusicSearchDTO;
import org.example.ware.entity.Song;
import org.example.ware.entity.User;

public interface MusicSearchService extends IService<Song> {
    IPage<Song> searchMusic(MusicSearchDTO dto);
}
