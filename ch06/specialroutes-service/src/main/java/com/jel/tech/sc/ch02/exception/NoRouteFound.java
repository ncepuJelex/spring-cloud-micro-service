package com.jel.tech.sc.ch02.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/6 16:57
 * @Description
 **/
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoRouteFound extends RuntimeException {
}
