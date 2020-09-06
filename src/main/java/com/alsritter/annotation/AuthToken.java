package com.alsritter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 客户端登录，输入用户名和密码，后台进行验证，如果验证失败则返回登录失败的提示。
 * 如果验证成功，则生成 token 然后将 username 和 token 双向绑定 （可以根据 username 取出 token 也可以根据 token 取出username）存入redis，
 * 同时使用 token+username 作为key把当前时间戳也存入redis。并且给它们都设置过期时间。
 *
 * 如果该接口标注了 @AuthToken 注解，则要检查客户端传过来的 Authorization 字段，获取 token。
 * 由于 token 与 username 双向绑定，可以通过获取的 token 来尝试从 redis 中获取 username，
 * 如果可以获取则说明 token 正确，反之，说明错误，返回鉴权失败。
 *
 * token可以根据用户使用的情况来动态的调整自己过期时间。
 * 在生成 token 的同时也往 redis 里面存入了创建 token 时的时间戳，
 * 每次请求被拦截器拦截 token 验证成功之后，
 * 将当前时间与存在 redis 里面的 token 生成时刻的时间戳进行比较，
 * 当当前时间的距离创建时间快要到达设置的redis过期时间的话，
 * 就重新设置token过期时间，将过期时间延长。
 * 如果用户在设置的 redis 过期时间的时间长度内没有进行任何操作（没有发请求），则token会在redis中过期。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthToken {
}
