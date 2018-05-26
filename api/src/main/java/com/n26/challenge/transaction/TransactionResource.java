package com.n26.challenge.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.n26.challenge.utils.DateUtils.getCurrentTimestamp;

@RestController
public class TransactionResource {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<Void> addTransaction(@RequestBody @Valid Transaction transaction) {
        boolean success = this.transactionService.addTransaction(transaction, getCurrentTimestamp());

        return  ResponseEntity.status(success ? HttpStatus.CREATED : HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<Statistics> getStatistics() {
        Statistics statistics = this.transactionService.getStatistics(getCurrentTimestamp());

        return ResponseEntity.ok(statistics);
    }
}
