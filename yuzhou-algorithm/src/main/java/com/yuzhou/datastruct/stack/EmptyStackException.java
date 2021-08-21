package com.yuzhou.datastruct.stack;

import java.io.Serializable;

/**
 * 自定义异常
 */
public class EmptyStackException extends RuntimeException implements Serializable {


    private static final long serialVersionUID = -8766038608920134747L;


    public EmptyStackException(){
        super();
    }

    public EmptyStackException(String msg){
        super(msg);
    }
}