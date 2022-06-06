package ru.itis.myscore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.myscore.dto.LoginDto;
import ru.itis.myscore.dto.PlayerDto;
import ru.itis.myscore.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("")
public class SecurityController {
    private final AccountService accountService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.register(loginDto));
    }


    @GetMapping(value = "/confirm/{confirmCode}")
    public ResponseEntity<Boolean> confirmAccount(@PathVariable("confirmCode") UUID confirmCode) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(accountService.confirmAccount(confirmCode));
    }
}

