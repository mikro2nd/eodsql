package net.lemnik.eodsql;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 *    <code>@Update</code> is the counter-part to the {@link Select} annotation.
 *    There are a few differences between the two. While <code>Select</code> could
 *    technically be used to perform an update on the database, it is not well suited
 *    to do so, as it requires the return of a <code>DataSet</code>. Methods
 *    annotated with <code>@Update</code> may return an <code>int</code> and
 *    correspond to the {@link java.sql.Statement#executeUpdate} method. The
 *    annotated method will return the number of rows in the database that were
 *    modified by it's execution, the method may also return <code>void</code>
 *    in which case any response from the database is discarded.
 * </p><p>
 *    A method annotated by <code>@Update</code> may return a {@link DataSet};
 *    or other well known collection or data-type (see {@link Select} for more
 *    info); to get back {@link AutoGeneratedKeys} from the database.
 *    In order for this to be valid, the method's <code>@Update</code> must have
 *    it's "keys" attribute set to something other than
 *    {@link GeneratedKeys#NO_KEYS_RETURNED GeneratedKeys.NO_KEYS_RETURNED}.
 * </p><p>
 *    An update method can take any arguments, and map either them directly
 *    into the query, or map fields within the passed objects into the
 *    SQL statement. An example of several different query syntax's:
 *    
 *    <pre>
 *        public class User {
 *            public Integer id;
 *            public String username;
 *        }
 *        
 *        public interface UserQuery extends BaseQuery {
 *            <span style="color: #00f;">@Update("UPDATE user SET username = ?1 WHERE id = ?2")</span>
 *            public int updateUser(String username, int id);
 *            
 *            <span style="color: #00f;">@Update("UPDATE user SET username = ?{1.username} WHERE id = ?{1.id}")</span>
 *            public int updateUser(User user);
 *        }
 *    </pre>
 *    
 *    This example will also work if the <code>User</code> class was:
 *    <pre>
 *        public class User {
 *            private Integer id;
 *            priavte String username;
 *            
 *            public Integer getId() {
 *                return id;
 *            }
 *            
 *            public String getUsername() {
 *                return username;
 *            }
 *        }
 *    </pre>
 *    
 *    The first example of an <code>@Update</code> syntax in the above example
 *    is compatible with the original Java JDBC EoD syntax. The query parser
 *    actually ignores any SQL code, and only parses code after the '?', here are
 *    some more examples:
 *    
 *    <ul>
 *        <li>?1 - the first parameter</li>
 *        <li>?{1} - the first parameter</li>
 *        <li>?{1.username}</li>
 *        <li>?{1.author.username}</li>
 *    </ul>
 *    
 *    Field's are mapped by first looking for a field of the given name, and then
 *    for a JavaBeans compatible getter method.
 * </p>
 * @author jason
 * @see Select
 * @see GeneratedKeys
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Update {

    /**
     * The SQL query to execute on the database.
     */
    String value() default "";

    /**
     * The SQL query to execute on the database.
     */
    String sql() default "";

    /**
     * If <code>true</code>, the update will be performed as a batch update. All parameters of the 
     * method need to be either arrays or lists of the same length in that case. The <code>sql</code> 
     * query applies to each element in the array / list individually.  
     */
    public boolean batchUpdate() default false;
    
    /**
     *<p>
     * The specification of how auto-generated keys are to be returned for this
     * <code>@Update</code> query.
     *</p><p>
     * <b>Note:</b> to use this feature (specify anything other than
     * {@link GeneratedKeys#NO_KEYS_RETURNED}) your underlying JDBC driver
     * must have support for the required <code>PreparedStatement</code>s.
     *</p>
     */
    GeneratedKeys keys() default GeneratedKeys.NO_KEYS_RETURNED;

    /**
     * <p>
     * The custom type bindings to be used on the parameters of this query. The binding is matched 
     * by position. The default type binding will be used for each parameter that has no  
     * <code>parameterBindings</code> entry or an entry that is of type {@link TypeMapper} itself. 
     * </p>
     * 
     * @since 2.2
     */
    Class<? extends TypeMapper>[] parameterBindings() default {};
}
