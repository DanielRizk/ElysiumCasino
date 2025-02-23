package org.daniel.elysium.user.profile;

import org.daniel.elysium.user.database.UserDAO;

public class UserProfile {
    private final String name;
    private String pass;
    private double balance;
    private final UserDAO userDAO;

    public UserProfile(String name, String pass, double balance) {
        this.name = name;
        this.pass = pass;
        this.balance = balance;
        this.userDAO = new UserDAO();
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void increaseBalanceBy(double amount){
        this.balance += amount;
        userDAO.updateBalance(this, this.name, this.balance);
    }

    public void decreaseBalanceBy(double amount){
        this.balance -= amount;
        userDAO.updateBalance(this, this.name, this.balance);
    }
}