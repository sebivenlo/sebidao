/**
 * PostgreSQL implementation of the sebidao.
 *
 * <p>PostgreSQL has a rich type set beyond the standard set defined for java.sql. Those types are typically a
 * subclass of PGobject, which is relatively easily mapped to a user type. An example would be
 * TSange, which is best expressed as a time range with begin and end point of LocalDateTime.</p>
 * <h2>Mapping registry</h2>
 * 
 * <p>The dao supports registering of mappping user types to postgresql types, to be precise
 * mappings from a type that does not belong to the jdbc standard but is supported in postgresql and has
 * a user type on the application type. The standard example is Date, where a modern use would be apply
 * LocalDate, not java.util.Date or java.sql.Date, which both are depricated.</p>
 *
 * <p>As a more complete example, using the postgresql tsrange or timestamp range, which is quite naturaly
 * mapped to a LocalDateTimeTange with a begin and end of type LocalDateTime.<br>
 *
 *
 * When the user defines a LocalDateTimeRange matching the semantics of TSRange, and provides a mapper
 * from tsrange to LocalDataTimeRange, than such mapper can be registerd to this factory, so that the Dao can easily
 * map from the PGobject to the user object.</p>
 *
 * <pre class='brush:java'>
 *
 * pgdaofactory.registerInMarshaller( LocalDateTimeRange.class, ( PGobject pgo )-{@literal >} LocalDateTimeRange.of(pgo));
 *
 * </pre>
 */
package nl.fontys.sebivenlo.dao.pg;
