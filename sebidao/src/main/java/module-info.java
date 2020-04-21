/**
 * Simple DAO using postgresql features.
 * 
 * Sebi DAO is created as a service to students and as example of a rather
 * simple but not naive DAO implementation.
 * 
 *
 */
module nl.fontys.sebidao {
    exports nl.fontys.sebivenlo.dao;
    exports nl.fontys.sebivenlo.dao.memory;
    exports nl.fontys.sebivenlo.dao.pg;
    requires java.logging;
    requires java.naming;
    requires java.sql;
    requires org.postgresql.jdbc;
}
