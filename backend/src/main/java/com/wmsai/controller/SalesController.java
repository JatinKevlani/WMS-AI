package com.wmsai.controller;

import com.wmsai.dto.SalesDTO;
import com.wmsai.entity.SalesTransaction;
import com.wmsai.entity.User;
import com.wmsai.exception.InsufficientStockException;
import com.wmsai.repository.UserRepository;
import com.wmsai.service.SalesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<SalesTransaction>> getTransactions(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        if (from != null && to != null) {
            return ResponseEntity.ok(salesService.getTransactions(
                    LocalDateTime.parse(from), LocalDateTime.parse(to)));
        }
        return ResponseEntity.ok(salesService.getAllTransactions());
    }

    @PostMapping
    public ResponseEntity<SalesTransaction> recordSale(
            @Valid @RequestBody SalesDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) throws InsufficientStockException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        return ResponseEntity.ok(salesService.recordSale(dto, user));
    }
}
