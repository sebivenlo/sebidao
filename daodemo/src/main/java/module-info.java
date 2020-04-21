module sebidaodemo {
    requires nl.fontys.sebidao;
    requires org.postgresql.jdbc;
    requires nl.fontys.sebivenlo.pgranges;
    exports entities to nl.fontys.sebidao;
    exports pgtypes to nl.fontys.sebidao;
}
