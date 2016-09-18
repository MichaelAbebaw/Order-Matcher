# Order Matcher

Order Matcher is a simulation of mini-trading platform for executing buy and sell orders at pre-defined prices for
multiple securities. In the platform you can add and remove securities as well as trade.

## Prerequisites

Java 1.8

## Operations

`add abc` adds new security `abc`.  
`buy abc x@y` buys a security `abc` for `x` volume at `y` price.  
`sell abc x@y` sells security `abc` at `x` volume and `y` price.  
`print abc` shows active orders of security `abc`.  
`price abc` shows the current bid and ask price for security `abc`.  
`history abc` lists trade history for security `abc`.  
`remove abc` removes security `abc` from the list.  

## NOTES
Positive integers are only allowed for volume and price operations!

## Class Diagram

## Built With

* Maven 4.0.0
* Intellij

## Author

[Michael A.](https://se.linkedin.com/in/michaelabebaw)
