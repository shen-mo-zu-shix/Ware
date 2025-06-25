package org.example.ware.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.ware.dto.MusicSearchDTO;
import org.example.ware.entity.Song;
import org.example.ware.service.IUserService;
import org.example.ware.service.MusicSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Music/")
@RequiredArgsConstructor
public class MusicSearchController {

    private final MusicSearchService musicSearchService;

    //搜索接口：支持歌曲名、歌手名，返回分页结果
    @PostMapping("/search")
    public IPage<Song> searchMusic(@RequestBody MusicSearchDTO dto) {
        // 调用 Service 执行搜索 + 分页
        return musicSearchService.searchMusic(dto);
    }
}
