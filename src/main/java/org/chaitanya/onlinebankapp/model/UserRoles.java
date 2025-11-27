package org.chaitanya.onlinebankapp.model;

public enum UserRoles {
    USER,
    ADMIN,
    MANAGER
}
//1. USER
//Can create an account for themselves
//Can view their own accounts only
//Cannot update or delete
//
//2. MANAGER
//Can view any user’s accounts
//Can update account details
//Cannot delete accounts
//
//3. ADMIN
//Can perform ALL operations including delete