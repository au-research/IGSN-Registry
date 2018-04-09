package org.csiro.igsn.entity.autheticators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.igsn.security.RegistryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class BuiltInUserDAO {

    private final Log logger = LogFactory.getLog(RegistryUserDAO.class);
    @Autowired
    private JdbcTemplate jdbcPGSQLTemplate;

    public BuiltInUserDAO(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:../applicationContext.xml");
        this.jdbcPGSQLTemplate = (JdbcTemplate) ctx.getBean("jdbcPGSQLTemplate");
    }


    public RegistryUser getAllocator(String username){
        RegistryUser userInfo = null;
        String UserSql = "SELECT allocatorid, username, password, 'ROLE_ALLOCATOR' AS role "+
                "FROM version30.allocator "+
                "WHERE isactive = TRUE and username =?";
        try {
            Object[] inputs = new Object[] {username};
            logger.debug(this.jdbcPGSQLTemplate.toString());
            userInfo = (RegistryUser)this.jdbcPGSQLTemplate.queryForObject(UserSql, inputs,
                    new RowMapper<RegistryUser>() {
                        public RegistryUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                            RegistryUser user = new RegistryUser();
                            user.setUsername(rs.getString("username"));
                            user.setName(rs.getString("username"));
                            user.setPassword(rs.getString("password"));
                            user.addRole(rs.getString("role"));
                            return user;
                        }
                    });
        }
        catch(Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.error("EXCEPTION IN  BuiltInUserDAO" + errors);
        }


        logger.info(UserSql);
        return userInfo;
    }

    public RegistryUser getRegistrant(String username){
        RegistryUser userInfo = null;
        String UserSql = "SELECT registrantid, username, password, 'ROLE_REGISTRANT' AS role "+
                "FROM version30.registrant "+
                "WHERE isactive = TRUE and username =?";
        try {
            Object[] inputs = new Object[] {username};
            logger.debug(this.jdbcPGSQLTemplate.toString());
            userInfo = (RegistryUser)this.jdbcPGSQLTemplate.queryForObject(UserSql, inputs,
                    new RowMapper<RegistryUser>() {
                        public RegistryUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                            RegistryUser user = new RegistryUser();
                            user.setUsername(rs.getString("username"));
                            user.setName(rs.getString("username"));
                            user.setPassword(rs.getString("password"));
                            user.addRole(rs.getString("role"));
                            return user;
                        }
                    });
        }
        catch(Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.error("EXCEPTION IN  BuiltInUserDAO" + errors);
        }


        logger.info(UserSql);
        return userInfo;
    }
}