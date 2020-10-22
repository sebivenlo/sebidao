module sebidaodemo {
    requires nl.fontys.sebidao;
    requires org.postgresql.jdbc;
    requires nl.fontys.sebivenlo.pgranges;
    opens entities;// to nl.fontys.sebidao;
    opens pgtypes;// to nl.fontys.sebidao;
}
