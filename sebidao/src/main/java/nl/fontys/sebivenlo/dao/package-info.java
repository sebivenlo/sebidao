/**
 *  A DAO makes persistence to a database both simple and flexbile.
 *
 * Here is what you do to setup a DAO factory and prepare it to create a DAO&lt;Employee&gt;.
 * <pre class='brush:java'>
 *
 * PGDAOFactory daof = new PGDAOFactory(dataSource); daof.registerMapper(
 * Employee.class, new EmployeeMapper() ); daof.registerMapper(
 * Department.class, new DepartmentMapper() ); daof.registerMapper(
 * Companies.class, new CompanyMapper() );
 *
 * </pre>
 *
 * To Use the DAO, create one just before use.
 * <pre class='brush:java'>
 * DAO&lt;Integer, Employee&gt; 
 * eDao = daof.createDao( Employee.class );
 * DAO&lt;Integer, Department&gt; dDao = daof.createDao( Department.class );
 * Employee joe = new Employee(....); 
 * joe = eDao.save(joe); // note that you get a new Employee with all fields, including id field set.
 * </pre>
 *
 * <h2>The DAO can also be used transactionally</h2>
 *
 * <b>Use case</b>: create a new department and set the departments id to joe
 * and hank. Create the eDAO as before, then do:
 *
 * <pre class='brush:java'>
 * try ( DAO&lt;Integer, Department&gt; ddao = daof.createDao( Department.class ); 
 *       TransactionToken tok = ddao.startTransaction(); 
 *       DAO&lt;Integer,Employee&gt; edao = daof.createDao( Employee.class, tok ); ) {
 *
 *    savedDept = ddao.save( engineering );
 *    int depno = savedDept.getDepartmentid(); 
 *    dilbert.setDepartmentid( depno );
 *    savedDilbert = edao.save( dilbert ); 
 *    System.out.println( "savedDilbert = " + savedDilbert ); 
 *     tok.commit(); 
 * } catch ( Exception ex ) { 
 *    tok.rollback();
 *    Logger.getLogger( TransactionTest.class.getName() ). log( Level.SEVERE, null, 
 *    ex ); 
 * }
 * </pre> The order in which you create the DAOs does not matter, but you must
 * make sure they use the same transaction token to make the cooperate in a
 * transaction.
 *
 * <h2>Entity and Mapper Recipes</h2>
 *
 * 
 * <h3>Entity with natural and generated (numeric) key</h3>
 * <p>Add {@code @ID} to the key field as in</p>
 * <pre class='brush:java'>
 *  class Employee extends Entity2&lt;Integer&gt; {
 *    &#64;ID 
 *    private int employeeid;
 *    // remainder removed for brevity.
 * </pre>
 * 
 * <h3>Entity with only non numeric primary key.</h3>
 * <p>Use case: the key is generated outside of the
 * control of the underlying database.</p>
 * <p> Add {@code @ID(generated = false)} to the key field, as in</p>
 * <pre class='brush:java'>
 *    &#64;ID(generated = false) 
 *    private String tickerSymbol;
 * </pre>
 * 
 * <h3>Entity with non numeric primary key and generated serial number.</h3>
 * <p>
 * Use case: You would like to have a surrogate key anyway.
 *  Add {@code @ID(generated = false)} as above and add {@code @Generated} to the surrogate key field, as in </p>
 * <pre class='brush:java'>
 *   &#64;ID(generated = false) 
 *   private String tickerSymbol;
 *   // more fields
 *   &#64;Generated
 *   private int companyid;
 * </pre>
 * <p>In the data definition of the table a primary key to the natural non-numeric key define the surrogate key as <code>serial unique not null</code>.</p>
 * <h3>Simple mapper</h3>
 * <p>Create a mapper by extending {@link AbstractMapper}
 *  like</p>
 * <pre class='brush:java'>
 *   public class EmployeeMapper extends AbstractMapper&lt;Integer,Employee&gt; {
 *   //
 *   public EmployeeMapper(  Class&lt;Integer&gt; keyType ,Class&lt;Employee&gt; entityType) {
 *      super( keyType,entityType );
 *  }
 * </pre>
 * <p>
 * and implement the required methods {@link nl.fontys.sebivenlo.dao.Mapper#explode Mapper.explode}, 
 * {@link nl.fontys.sebivenlo.dao.Mapper#implode Mapper.implode} and {@link nl.fontys.sebivenlo.dao.Mapper#keyExtractor Mapper.keyExtractor}.
 * </p>
 * <h3>Mappers: Custom for table</h3>
 * <p>You disapprove of the result of the simple plural table name generated and want your own decision.<br>
 *  In your mapper overwrite the method table name as in:</p>
 * 
 * <pre class='brush:java'>
 *  &#64;Override
 *  public String tableName() {
 *      return "companies";
 *  }
 *  </pre>
 * 
 */
package nl.fontys.sebivenlo.dao;
