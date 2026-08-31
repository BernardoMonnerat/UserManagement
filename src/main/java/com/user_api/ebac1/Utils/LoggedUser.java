package com.user_api.ebac1.Utils;

import com.user_api.ebac1.Configuration.DbConfiguration;
import com.user_api.ebac1.Configuration.SecurityConfig;
import com.user_api.ebac1.Exceptions.UserExceptions;
import com.user_api.ebac1.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoggedUser {

    private static final Logger log = LoggerFactory.getLogger(LoggedUser.class);
    private final Map<String, String> activeSession = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> logTime= new ConcurrentHashMap();
    private final SecurityConfig securityConfig;
    private final DbConfiguration db;

    public LoggedUser(SecurityConfig securityConfig, DbConfiguration db) {
        this.securityConfig = securityConfig;
        this.db = db;
    }

    public void addUser(User user){
        if(user.getEmail() == null || user.getPassword() == null ){
            throw new UserExceptions("Please insert valid data!");
        }

        String hashPassword = securityConfig.passwordEncoder().encode(user.getPassword());

        activeSession.put(user.getEmail(), hashPassword);

        try(Connection conn = db.getConnection()){
            String sql = "INSERT INTO users (email, password) VALUES(?,?)";

            PreparedStatement stat = conn.prepareStatement(sql);

            stat.setNString(1, user.getEmail());
            stat.setNString(2, hashPassword);

            stat.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public boolean validateUser(User user){
        if(user.getEmail() == null || user.getPassword() == null ){
            throw new UserExceptions("Please insert valid data!");
        }

        String encriptedPassword = activeSession.get(user.getPassword());

        if(encriptedPassword == null){
            return false;
        }


        boolean validPassword = securityConfig.passwordEncoder().matches(user.getPassword(), encriptedPassword);


        if(!validPassword){
            throw new UserExceptions("The password is incorrect!");
        }

            logTime.put(user.getEmail(), LocalDateTime.now());
            return true;

    }

    public boolean temporarySession(String email){


           LocalDateTime now = LocalDateTime.now();
           LocalDateTime loginTime = logTime.get(email);


           if(loginTime == null){
                return false;
           }

           Long duration = Duration.between(loginTime, now).toSeconds();


           if(duration > 60){
               logTime.remove(email);
               throw new UserExceptions("Session time has been expired!");
           }
           return true;
    }

}
