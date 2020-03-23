/**
 * PostgreSQL implementation of the sebidao.
 *
 * <p>
 * PostgreSQL has a rich type set beyond the standard set defined for java.sql.
 * Those types are typically a subclass of PGobject, which is relatively easily
 * mapped to a user type. An example would be tsrange, which is best expressed as
 * a time range with begin and end point of LocalDateTime.</p>
 * <h2>Mapping registry</h2>
 *
 * <p>
 * The dao supports registering of mapping user types to PostgreSQL types, to
 * be precise mappings from a type that does not belong to the jdbc standard but
 * is supported in PostgreSQL and has a user type on the application side. The
 * standard example is Date, where a modern use would be apply LocalDate, not
 * java.util.Date or java.sql.Date, which both are deprecated.</p>
 *
 * <p>
 * As a more complete example, using the PostgreSQL tsrange or timestamp range,
 * which is quite naturally mapped to a LocalDateTimeTange with a begin and end
 * of type LocalDateTime.<br>
 *
 *
 * When the user defines a LocalDateTimeRange matching the semantics of TSRange,
 * and provides a mapper from tsrange to LocalDataTimeRange, than such mapper
 * can be registered to this factory, so that the Dao can easily map from the
 * PGobject to the user object.</p>
 * The parameters to the marshallerRegister are the java type (class) for which the
 * marshaling should be applied, the inMarsmaler function, from database <b>to</b> said type, and
 * the outMarshaler which wraps the object in a PGobject and tags it with the database column type.
 *
 * <pre class='brush:java'>
 * 
 * pdaofactory.registerPGMashallers( LocalDateTimeRange.class,
 *                                   LocalDateTimeRange::fromTSRangeObject, 
 *                                   x {@literal ->} PGDAOFactory.pgobject( "tsrange", x ) );
 *
 * </pre>
 */
package nl.fontys.sebivenlo.dao.pg;
