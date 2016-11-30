package com.jrd.loan.parser;

public interface Parser<T> {
    public T parser(String content, Class<T> tClass);
}