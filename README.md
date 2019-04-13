# sebidao
Opinionated and generic data access object implementation for postgreSQL database.

It does not use postgreSQL features per se, but it is only tested with postgreSQL.

This DAO is a work in progress.
It supports the basic CRUD operations in a generic withn little help
of the user-programmer.

The only required helper class to an entity say student is any mapper, like StudentMapper,
which knows enough of the student class to take a student instance apart in an array
of objects and do the reverse.
The Student class would probably be friendly and lend a helping hand to the Mapper by providing just such methods, but only package private, mind you. That will do the work.

The javadoc can be found at the [Fontys Venlo Javabits website](https://javabits.fontysvenlo.org/) under 
[Sebi DAO javadoc](https://javabits.fontysvenlo.org/sebidao/apidocs/index.html)


