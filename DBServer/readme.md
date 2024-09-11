# Database Server JAVA Assignment


## DESCRIPTION:

### This database server will provide the following features:

1. To store Entity-Relationship data as a collection of files
2. To listen for incoming client requests to interrogate and manipulate the stored data
3. To conform to a standard query language that allows operations to be performed on the stored data\


### Supported SQL Keywords:

USE: changes the database against which the following queries will be run

CREATE: constructs a new database or table (depending on the provided parameters)

INSERT: adds a new entity to an existing table

SELECT: searches for entities that match the given condition

UPDATE: changes the data contained within a table

ALTER: changes the structure (rows) of an existing table

DELETE: removes entities that match the given condition from an existing table

DROP: removes a specified table from a database, or removes the entire database

AND / OR: allows conditions to be combined (makes use of parentheses to define ordering)

LIKE: used for comparing partial substrings in conditions

JOIN: performs an inner join on two tables (returning all permutations of all matching entities)


## TO RUN:
1. Go to the DBServer folder.
2. Compile with `javac DBServer`.
3. Run `java DBServer`.
4. The database server will listen on port 8888 and receive incoming messages.
5. The database data will be stored in the "database" folder.


## To Improve:
1. method names are potentially problematic in that they are just single words, start with an uppercase letter and don't conform to recommendations (they aren't verbs, checks or event handlers)
2. blank lines to clearly separate methods
3. Cyclic dependencies
4. high density of IF/CASE statements: SingleExpr.isTarget(), Parser.parseCommand() and Parser.getConditionList(). 
5. complex structures:
6. tightly coupled pairs of classes (SingleExpr<->Value, Table<->Column, Parser<->Tokenizer and Parser<->Token) which may benefit from refactoring to improve encapsulation and separation.
7.  access global variables
8.  Remember to keep your code DRY !
