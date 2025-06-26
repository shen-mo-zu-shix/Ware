package org.example.ware.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "singer")
public class Singer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long singerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // 关联user表的user_id字段
    private User user;

    @Column(nullable = false)
    private String singerName;

    private String singerAvatar;

    @Lob
    private String introduction;

    @Column(nullable = false)
    private LocalDateTime createTime;


}