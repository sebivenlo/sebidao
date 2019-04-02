/**
 *  A DAO makes persistence to a database both simple and flexbile.
 *
 * Here is what you do to setup a DAO factory and prepare it to create a DAO&lt;Employee&gt;.
 * <pre class='brush:java'>
 *
 * PGDAOFactory daof = new PGDAOFactory(dataSource); 
 * daof.registerMapper( Employee.class, new EmployeeMapper() );
 * daof.registerMapper( Departmant.class, new DepartmentMapper() );
 * 
 * </pre>
 *
 * To Use the DAO, create one just before use.
 * <pre class='brush:java'>
 * DAO&lt;Integer, Employee&gt; eDao = daof.createDao( Employee.class );
 * DAO&lt;Integer, Department&gt; dDao = daof.createDao( Department.class );
 * Employee joe = new Employee(....);
 * joe = eDao.save(joe); // note that you get a new Employee with all fields, including id field set.
 * </pre>
 * 
 * <h2>The DAO can also be used transactionally</h2>
 * 
 * <b>Use case</b>: create a new department and set the departments id to joe and hank.
 * Create the eDAO as before, then do:
 * 
 * <pre class='brush:java'>
 * try ( TransactionToken ttok = ddao.startTransaction(); ) {
 *     // create a dDao with same token.
 *     DAO&lt;Integer, Department&gt; dDao = daof.createDao( Department.class, ttok );
 *     // use dDao
 *     Department engineering = dDao.save(new Department(....));
 *     joe.setDepartmentid(engineering.getDepartmentid());
 *     hank.setDepartmentid(engineering.getDepartmentid());
 *     eDao.save(joe);
 *     eDao.save(hank);
 *     ttok.commit();
 * } catch(Exception ex) {
 *    ttok.rollback();
 * }
 * 
 * </pre>
 * The order in which you create the DAOs does not matter, but you must make sure they use the same transaction token
 * to make the cooperate in a transaction.
 */
package nl.fontys.sebivenlo.dao;
