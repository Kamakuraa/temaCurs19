package ro.fasttrackit.temaCurs19.service;

import org.springframework.stereotype.Service;
import ro.fasttrackit.temaCurs19.model.Transaction;
import ro.fasttrackit.temaCurs19.model.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final List<Transaction> transactions = new ArrayList<>();

    public TransactionService(){
        transactions.addAll(List.of(
                new Transaction(1, "TV", Type.SELL, 1.22),
                new Transaction(2, "Fridge", Type.BUY, 100.3),
                new Transaction(3, "Computer", Type.BUY, 300.22),
                new Transaction(4, "Laptop", Type.SELL, 400.22)
        ));

    }

    public Optional<Transaction> findById( int productId ){
        return transactions.stream()
                .filter(transaction -> transaction.getId() == productId)
                .findFirst();
    }

    public List<Transaction> getAll( String name ){
        return transactions.stream()
                .filter(transaction -> name == null || transaction.getProduct().contains(name))
                .collect(Collectors.toList());
    }

    public List<String> getAllProducts(){
        return this.transactions.stream()
                .map(Transaction::getProduct)
                .collect(Collectors.toList());
    }

    public List<Transaction> getMinAmmount(){
        return transactions.stream()
                .filter(transaction -> transaction.getAmount() == fetchMinAmmount())
                .collect(Collectors.toList());
    }

    public List<Transaction> getMaxAmmount(){
        return (List<Transaction>) this.transactions.stream()
                .filter(transaction -> transaction.getAmount() == fetchMaxAmmount());
    }

    private double fetchMaxAmmount(){
        return this.transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .max()
                .orElse(1);
    }

    private double fetchMinAmmount(){
        return this.transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .min()
                .orElse(1);
    }


    public Transaction addTransactions( Transaction transaction ){
        return addTransactions(fetchMaxId() + 1, transaction);
    }

    public Transaction addTransactions( int transactionId, Transaction transaction ){
        Transaction newTransaction = new Transaction(
                fetchMaxId() + 1,
                transaction.getProduct(),
                transaction.getType(),
                transaction.getAmount()
        );
        this.transactions.add(transactionId - 1, newTransaction);
        return newTransaction;
    }

    private int fetchMaxId(){
        return this.transactions.stream()
                .mapToInt(Transaction::getId)
                .max()
                .orElse(1);
    }

    public Optional<Transaction> deleteTransaction( int transactionId ){
        Optional<Transaction> transactionOptional = findById(transactionId);
        transactionOptional.
                ifPresent(transactions::remove);
        return transactionOptional;
    }

    public Optional<Transaction> replaceTrans( int transactionId, Transaction newTransaction ){
        Optional<Transaction> replaceTransaction = deleteTransaction(transactionId);
        replaceTransaction
                .ifPresent(deletedTrans -> addTransactions(transactionId, newTransaction));
        return replaceTransaction;
    }

    public Optional<Transaction> patchTransaction( int transactionId, Transaction newTransaction ){
        Optional<Transaction> transactionById = findById(transactionId);
        Optional<Transaction> patchedTransaction = transactionById
                .map(oldTransaction -> new Transaction(
                        oldTransaction.getId(),
                        newTransaction.getProduct() != null ? newTransaction.getProduct() : oldTransaction.getProduct(),
                        newTransaction.getType() != null ? newTransaction.getType() : oldTransaction.getType(),
                        newTransaction.getAmount() != 0 ? newTransaction.getAmount() : oldTransaction.getAmount()
                ));
        patchedTransaction.ifPresent(newTransactions -> replaceTrans(transactionId, newTransactions));
        return patchedTransaction;
    }

    public Map<Type, List<Transaction>> mapTypeToSumAmount(){
        return transactions.stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getType()));
    }

    public Map<String, List<Transaction>> mapFromProductAmount(){
        return transactions.stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getProduct()));
    }
}
