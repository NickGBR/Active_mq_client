package com.example.client.produser.impl;

public interface Producer {
    void withdraw(Long id, Double amount);
    void deposit(Long id, Double amount);
    void transferTo(Long idFrom, Long idTo, Double amount);
    void createAccount(Long id);
    void closeAccount(Long id);
}
