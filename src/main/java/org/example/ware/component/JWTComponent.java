package org.example.ware.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.example.ware.exception.Code;
import org.example.ware.exception.xException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JWTComponent {
    @Value("${my.secretkey}")//配置文件中读取名为 my.secretkey 的值
    private String secretkey;//用于存储读取的密钥

    private Algorithm algorithm;//存储 JWT 签名算法，这里使用 HMAC256 算法

    @PostConstruct//表示 init 方法在构造函数执行后会被调用，适合用于初始化操作
    private void init(){
        algorithm=Algorithm.HMAC256(secretkey);
    }
    //encode方法负责生成 JWT
    public String encode(Map<String, Object> map){
        LocalDateTime time=LocalDateTime.now().plusDays(1);//获取当前时间，并将其增加一天，设定 JWT 的过期时间为 1 天后
        return JWT.create()
                .withPayload(map)//传入的 map 作为有效载荷添加到 JWT 中
                .withIssuedAt(new Date())//设置 JWT 的签发时间为当前时间。
                .withExpiresAt(Date.from(time.atZone(ZoneId.systemDefault()).toInstant()))//设置 JWT 的过期时间，使用前面计算的时间
                .sign(algorithm);//使用指定的算法对 JWT 进行签名
    }

    //decode方法用于服务器端验证和解析客户端发送的 JWT
    //token:需要验证和解析的 JWT 字符串
    public DecodedJWT decode(String token) {
        try {
            return JWT.require(algorithm).build().verify(token);//创建一个验证器，使用先前的算法验证传入的 token。如果有效，返回一个 DecodedJWT 对象，包含 JWT 的所有信息。
        } catch (TokenExpiredException | SignatureVerificationException e) {
            if (e instanceof SignatureVerificationException) {
                throw xException.builder().code(Code.FORBIDDEN).build();
            }
            throw xException.builder().code(Code.TOKEN_EXPIRED).build();
        }
    }
}
