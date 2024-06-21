package com.phatpl.learnvocabulary.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    @JsonProperty("code")
    Integer code;
    @JsonProperty("data")
    Object data;
    @JsonProperty("message")
    String message;
    @JsonProperty("status")
    String status;


    public static Response rok(Object o){
        return Response.builder()
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(o)
                .build();
    }

    public static Response rcreated(Object o){
        return Response.builder()
                .message("CREATED")
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .data(o)
                .build();
    }

    public static Response rbadRequest(Object o){
        return Response.builder()
                .message("BAD_REQUEST")
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .data(o)
                .build();
    }
    public static Response runauthorized(Object o){
        return Response.builder()
                .message("UNAUTHORIZED")
                .code(HttpStatus.UNAUTHORIZED.value())
                .status(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .data(o)
                .build();
    }
    public static Response rforbidden(Object o){
        return Response.builder()
                .message("FORBIDDEN")
                .code(HttpStatus.FORBIDDEN.value())
                .status(HttpStatus.FORBIDDEN.getReasonPhrase())
                .data(o)
                .build();
    }

    public static Response rnotFound(Object o){
        return Response.builder()
                .message("NOT_FOUND")
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .data(o)
                .build();
    }

    /* RESPONSE ENTITY */
    public static ResponseEntity ok(Object o) {
        return ResponseEntity.status(HttpStatus.OK).body(Response.rok(o));
    }

    public static ResponseEntity<?> created(Object o){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.rcreated(o));
    }

    public static ResponseEntity badRequest(Object o){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.rbadRequest(o));
    }
    public static ResponseEntity unauthorized(Object o){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Response.runauthorized(o));
    }
    public static ResponseEntity forbidden(Object o){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Response.rforbidden(o));
    }

    public static ResponseEntity notFound(Object o){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.rnotFound(o));
    }
}