# Expense Analyzer
Expense Analyzer is an application that read all messages and fetch transactional messages(i.e. all income/credited and expense/debited messages) and show the comparision in a pie chart.

## Requirement
- Android 5.1 or later (Minimum SDK level 21)
- Android Studio (to compile and use)

## Language Used
- Kotlin

## Features
- Filter-out Transactional messages, Credited and Debited messages
- Show Income vs. Expense Pie Chart 

## Architecture
- MVVM

## Application Flow
When we run this application first it'll start with a **Splash Screen** and it will go to **TransactionActivity** in which it will ask permission to read all messages from URI if allow to read SMS then differentiate all transactional messages in 3 parts i.e all transactions, Expense and Income. In TransactionActivity three buttons are there by which we can filter-out **_Transaction messages, Income messages_** and **_Expense Messages_**. After that there is a button which will show a graph on click in another activity. In **GraphActivity** It will show a graph based on Income and Expense amount.

