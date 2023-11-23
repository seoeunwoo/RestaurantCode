package hello.restaurant.user;

import java.util.List;

public class UserServiceImpl implements UserService{

    private UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Override
    public Long login(String loginid, String password) {
        return userRepository.login(loginid, password);
    }



    @Override
    public void steakorder(Long id, int steakcount) {
        userRepository.steakorder(id, steakcount);
    }

    @Override
    public void pastaorder(Long id, int pastacount) {
        userRepository.pastaorder(id, pastacount);
    }

    @Override
    public void pizzaorder(Long id, int pizzacount) {
        userRepository.pizzaorder(id, pizzacount);
    }

    @Override
    public void couponcheck(Long id) {
        userRepository.couponcheck(id);
    }
}
