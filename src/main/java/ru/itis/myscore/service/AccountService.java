package ru.itis.myscore.service;

import ru.itis.myscore.dto.LoginDto;

import java.util.UUID;

public interface AccountService {
    boolean register(LoginDto loginDto);
    boolean confirmAccount(UUID confirmCode);
}
