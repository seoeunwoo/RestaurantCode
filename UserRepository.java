package hello.restaurant.user;

import java.util.List;

public interface UserRepository {
    User save(User user);
    User findById(Long id);
    List<User> findAll();
    Long login(String loginid, String password);
    void steakorder(Long id, int steakcount);
    void pastaorder(Long id, int pastacount);
    void pizzaorder(Long id, int pizzacount);
    void couponcheck(Long id);
}
