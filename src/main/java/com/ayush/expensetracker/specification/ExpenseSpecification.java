package com.ayush.expensetracker.specification;

import com.ayush.expensetracker.entity.Category;
import com.ayush.expensetracker.entity.Expense;
import com.ayush.expensetracker.entity.User;
import com.ayush.expensetracker.enums.PaymentMethod;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseSpecification {

    private ExpenseSpecification() {}

    public static Specification<Expense> belongsToUser(User user) {
        return (root, query, cb) ->
                cb.equal(root.get("user"), user);
    }

    public static Specification<Expense> hasCategory(Category category) {
        return (root, query, cb) ->
                cb.equal(root.get("category"), category);
    }

    public static Specification<Expense> hasPaymentMethod(PaymentMethod paymentMethod) {
        return (root, query, cb) ->
                cb.equal(root.get("paymentMethod"), paymentMethod);
    }

    public static Specification<Expense> expenseBetween(LocalDate start,
                                                        LocalDate end) {

        return (root, query, cb) ->
                cb.between(root.get("expenseDate"), start, end);
    }

    public static Specification<Expense> amountBetween(BigDecimal min,
                                                       BigDecimal max) {

        return (root, query, cb) ->
                cb.between(root.get("amount"), min, max);
    }
}
