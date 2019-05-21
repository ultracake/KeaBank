package com.example.keabank.Models;

import java.util.ArrayList;

public class AccountNames
{
    //names
    private String accountBudget = "Budget";
    private String accountBusiness = "Business";
    private String accountDefault = "Default";
    private String accountPension = "Pension";
    private String accountSavings = "Savings";

    //for database
    private String balanceBudget = "balanceBudget";
    private String balanceBusiness = "balanceBusiness";
    private String balanceDefault = "balanceDefault";
    private String balancePension = "balancePension";
    private String balanceSavings = "balanceSavings";

    private ArrayList<String> accountNamesList = new ArrayList<>();
    private ArrayList<String> accountDBlist = new ArrayList<>();

    public AccountNames()
    {
        addNames();
    }

    private void addNames()
    {
        accountNamesList.add(accountBudget);
        accountNamesList.add(accountBusiness);
        accountNamesList.add(accountDefault);
        accountNamesList.add(accountPension);
        accountNamesList.add(accountSavings);

        accountDBlist.add(balanceBudget);
        accountDBlist.add(balanceBusiness);
        accountDBlist.add(balanceDefault);
        accountDBlist.add(balancePension);
        accountDBlist.add(balanceSavings);
    }

    public ArrayList<String> getAccountNamesList()
    {
        return accountNamesList;
    }

    public ArrayList<String> getAccountDBlist()
    {
        return accountDBlist;
    }
}
