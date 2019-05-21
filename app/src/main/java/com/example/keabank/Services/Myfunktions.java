package com.example.keabank.Services;

import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.User;


public class Myfunktions
{
    //int
    private User user;
    private AccountNames accountNames;
    private int currentVal;
    private int id;

    //names
    private String accountBudget;
    private String accountBusiness ;
    private String accountDefault;
    private String accountPension ;
    private String accountSavings;

    public Myfunktions()
    {
        this.accountNames = new AccountNames();

        this.accountBudget = "Budget";
        this.accountBusiness = "Business";
        this.accountDefault = "Default";
        this.accountPension = "Pension";
        this.accountSavings = "Savings";

        this.currentVal = 0;
        this.id = 0;
    }

    public int findAccountID(String name)
    {
        for (int i = 0; i < accountNames.getAccountNamesList().size(); i++)
        {
            if(name.equals(accountNames.getAccountNamesList().get(i)))
            {
                id = i;
                return id;
            }
        }
        return id;
    }

    public int checkWhichAccountValToUse(User user, String name)
    {
        //myuser = user;
        if (name.equals(accountBudget))
        {
            return currentVal = user.getBalanceBudget();
        }
        if (name.equals(accountBusiness))
        {
            return currentVal = user.getBalanceBusiness();
        }
        if (name.equals(accountDefault))
        {
            return currentVal = user.getBalanceDefault();
        }
        if (name.equals(accountPension))
        {
            return currentVal = user.getBalancePension();
        }
        if (name.equals(accountSavings))
        {
            return currentVal = user.getBalanceSavings();
        }
        return currentVal;
    }

    public int checkWhichAccountValueByIdToUse(User user, int id)
    {
        //myuser = user;
        if (id == 0)
        {
            return currentVal = user.getBalanceBudget();
        }
        if (id == 1)
        {
            return currentVal = user.getBalanceBusiness();
        }
        if (id == 2)
        {
            return currentVal = user.getBalanceDefault();
        }
        if (id == 3)
        {
            return currentVal = user.getBalancePension();
        }
        if (id == 4)
        {
            return currentVal = user.getBalanceSavings();
        }
        return currentVal;
    }

    public AccountNames checkIfExistForViews(User user, AccountNames accountNames)
    {
        if (user.getBalanceBudget() <= 0)
        {
            accountNames.getAccountNamesList().remove("Budget");
        }
        if (user.getBalanceBusiness() <= 0)
        {
            accountNames.getAccountNamesList().remove("Business");
        }
        if (user.getBalancePension() <= 0)
        {
            accountNames.getAccountNamesList().remove("Pension");
        }
        if (user.getBalanceSavings() <= 0)
        {
            accountNames.getAccountNamesList().remove("Savings");
        }
        return accountNames;
    }

    public AccountNames checkIfExistForCreate(User user, AccountNames accountNames)
    {
        if (user.getBalanceBudget() > 0)
        {
            accountNames.getAccountNamesList().remove("Budget");
        }
        if (user.getBalanceBusiness() > 0)
        {
            accountNames.getAccountNamesList().remove("Business");
        }
        if ( user.getBalanceDefault() > 0)
        {
            accountNames.getAccountNamesList().remove("Default");
        }
        if (user.getBalancePension() > 0)
        {
            accountNames.getAccountNamesList().remove("Pension");
        }
        if (user.getBalanceSavings() > 0)
        {
            accountNames.getAccountNamesList().remove("Savings");
        }

        return accountNames;
    }

    public AccountNames getAccountNames() {
        return accountNames;
    }
}
