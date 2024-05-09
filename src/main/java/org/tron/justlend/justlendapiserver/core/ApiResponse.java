package org.tron.justlend.justlendapiserver.core;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
  private int code;
  private String message;
  private T data;

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(HttpStatus.OK.value(), "Success", data);
  }

  public static <T> ApiResponse<T> success() {
    return new ApiResponse<>(HttpStatus.OK.value(), "Success", null);
  }

  public static <T> ApiResponse<T> bad(String message, T data) {
    return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), message, data);
  }


  public static <T> ApiResponse<T> error() {
    return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null);
  }

  public static <T> ApiResponse<T> notFound() {
    return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Request not found", null);
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
