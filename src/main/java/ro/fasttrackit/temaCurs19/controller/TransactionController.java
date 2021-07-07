package ro.fasttrackit.temaCurs19.controller;

import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.temaCurs19.model.Transaction;
import ro.fasttrackit.temaCurs19.model.Type;
import ro.fasttrackit.temaCurs19.service.TransactionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController( TransactionService transactionService ){
        this.transactionService = transactionService;
    }

    @GetMapping
    List<Transaction> getAllTransactions( @RequestParam(required = false) String name ){
        return transactionService.getAll(name);
    }

    @GetMapping("{transactionId}")
    Transaction getById( @PathVariable int transactionId ){
        return transactionService.findById(transactionId)
                .orElse(null);
    }

    @GetMapping("product")
    List<String> getByProducts(){
        return transactionService.getAllProducts();
    }

    @GetMapping("minimum")
    List<Transaction> getMin(){
        return transactionService.getMinAmmount();
    }

    @GetMapping("maximum")
    List<Transaction> getMax(){
        return transactionService.getMaxAmmount();
    }

    @PostMapping
    Transaction createTransaction( @RequestBody Transaction transaction ){
        return transactionService.addTransactions(transaction);
    }
    @PutMapping("{transactionId}")
    Transaction replaceTransaction(@PathVariable int transactionId,
                                   @RequestBody Transaction newTransaction){
        return transactionService.replaceTrans(transactionId, newTransaction)
                .orElse(null);
    }

    @DeleteMapping("{transactionId}")
    Transaction deleteTransaction (@PathVariable int transactionId){
        return transactionService.deleteTransaction(transactionId)
                .orElse(null);
    }

    @PatchMapping("{transactionId}")
    Transaction patchTransaction(@PathVariable int transactionId,
                                 @RequestBody Transaction newTransaction){
        return transactionService.patchTransaction(transactionId, newTransaction)
                .orElse(null);
    }

    @GetMapping("transaction/amount")
    Map<Type, List<Transaction>> mapTypeToSumAmount(){
        return transactionService.mapTypeToSumAmount() ;
    }

    @GetMapping("product/amount")
    Map<String, List<Transaction>> mapFromProductAmount(){
        return transactionService.mapFromProductAmount();
    }


}
