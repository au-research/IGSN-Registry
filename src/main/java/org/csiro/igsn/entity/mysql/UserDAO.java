package org.csiro.igsn.entity.mysql;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.igsn.security.RegistryUser;
import org.csiro.igsn.security.UserDetailsContextMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Repository
public class UserDAO {

    private final Log logger = LogFactory.getLog(UserDAO.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public UserDAO(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:../applicationContext.xml");
        this.jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
    }


    public RegistryUser getUserInfo(String username){
        RegistryUser userInfo = null;
        String UserSql = "select r.role_id, r.name, ab.passphrase_sha1, 'ROLE_REGISTRANT' as role "+
                "FROM roles r INNER JOIN authentication_built_in ab ON r.role_id = ab.role_id "+
                "WHERE enabled = 1 and r.role_id =?";
        try {
            String roleSql = "select name FROM roles WHERE role_id =?";
            Object[] inputs = new Object[] {username};
            logger.debug(this.jdbcTemplate.toString());
            String empName = this.jdbcTemplate.queryForObject(roleSql, inputs, String.class);
            logger.debug(empName);

            userInfo = (RegistryUser)this.jdbcTemplate.queryForObject(UserSql, inputs,
                    new RowMapper<RegistryUser>() {
                        public RegistryUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                            RegistryUser user = new RegistryUser();
                            user.setUsername(rs.getString("role_id"));
                            user.setName(rs.getString("name"));
                            user.setPassword(rs.getString("passphrase_sha1"));
                            user.addRole(rs.getString("role"));
                            return user;
                        }
                    });
        }
        catch(Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.error("EXCEPTION IN  UserDAO" + errors);
        }


        logger.info(UserSql);
        return userInfo;
    }
}