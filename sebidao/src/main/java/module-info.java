/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
