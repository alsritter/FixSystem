package com.alsritter.model;

import lombok.Builder;
import lombok.Data;

/**
 * Builder 注解就是可以直接使用建造者模式
 * 如下：
 * ResponseTemplate.builder()
 *                 .code(200)
 *                 .message("登录成功")
 *                 .data(result)
 *                 .build();
 *
 * 范例：
 * public class User {
 *     private Integer id;
 *     private String name;
 *     private String address;
 *
 *     private User() {
 *     }
 *
 *     private User(User origin) {
 *         this.id = origin.id;
 *         this.name = origin.name;
 *         this.address = origin.address;
 *     }
 *
 *     public static class Builder {
 *         private User target;
 *
 *         public Builder() {
 *             this.target = new User();
 *         }
 *
 *         public Builder id(Integer id) {
 *             target.id = id;
 *             return this;
 *         }
 *
 *         public Builder name(String name) {
 *             target.name = name;
 *             return this;
 *         }
 *
 *         public Builder address(String address) {
 *             target.address = address;
 *             return this;
 *         }
 *
 *         public User build() {
 *             return new User(target);
 *         }
 *     }
 *
 * @author alsritter
 * @version 1.0
 **/
@Builder
@Data
public class ResponseTemplate<T> {

    private Integer code;

    private String message;

    private T data;

}