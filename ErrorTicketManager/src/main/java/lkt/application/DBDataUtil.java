package lkt.application;

import lkt.model.User;
import lkt.repository.IUserRepository;
import lkt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = "lkt")
public class DBDataUtil {
    private static IUserRepository userRepository;

    static {
        System.setProperty("user.timezone", "Asia/Ho_Chi_Minh");
    }

    @Autowired
    private void setUserRepository(IUserRepository userRepository) {DBDataUtil.userRepository = userRepository;}

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ErrorTicketManagerApplication.class, args);
        setAllUsersPasswordToBCrypt(1, 51);
    }

    // UserIDEnd is exclusive
    private static void setAllUsersPasswordToBCrypt(int userIDStart, int userIDEnd) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        for (int i = userIDStart; i < userIDEnd; i++) {
            User user = userRepository.findByUserID(i);
            if (user == null) continue;
            user.setPassword(bCryptPasswordEncoder.encode(user.getUsername()));
            userRepository.update(user);
        }
    }
}
