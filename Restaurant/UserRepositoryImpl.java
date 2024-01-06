package hello.restaurant.user;

import hello.itemservice.domain.personal.Personal;
import hello.restaurant.restaurantconnection.DBConnectionUtility;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class UserRepositoryImpl implements UserRepository{
    private final Map<Long, User> data = new HashMap<>();
    private Long id = 0L;
    private Long loginId;
    private int totalmoney = 0;
    private int steakmoney = 0;
    private int pastamoney = 0;
    private int pizzamoney = 0;
    private int vipcount = 0;

    private Long getNextId()
    {
        String sql = "SELECT MAX(id) FROM restaurant";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            if (resultSet.next())
            {
                Long maxId = resultSet.getLong(1);
                // 최대 ID 값에 1을 더한 값을 반환
                return maxId + 1;
            } else
            {
                // 테이블이 비어있을 경우, 1부터 시작
                return 1L;
            }
        }
        catch (SQLException e)
        {
            log.error("데이터베이스 오류입니다", e);
            return null;
        }
    }


    @Override
    public User save(User user) {

        Long nexdId = getNextId();

        user.setId(nexdId);
        user.setRating(Rating.BASIC);
        data.put(nexdId, user);

        String SaveSql = "insert into restaurant(id, loginid, password, name, age, email, rating) values (?, ?, ?, ?, ?, ?, ?)";

        try
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SaveSql);

            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getLoginid());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getName());
            preparedStatement.setInt(5, user.getAge());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getRating().name());


            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            log.error("데이터 오류 입니다", e);
        }
        System.out.println();
        System.out.println(data.toString());
        System.out.println();
        return user;
    }

    @Override
    public User findById(Long id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String findAllSql = "SELECT * FROM restaurant";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllSql);
             ResultSet resultSet = preparedStatement.executeQuery())
        {

            while (resultSet.next())
            {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setLoginid(resultSet.getString("loginid"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("name"));
                user.setAge(resultSet.getInt("age"));
                user.setEmail(resultSet.getString("email"));
                user.setRating(Rating.valueOf(resultSet.getString("rating")));
                user.setSteakcount(resultSet.getInt("steakcount"));
                user.setPastacount(resultSet.getInt("pastacount"));
                user.setPizzacount(resultSet.getInt("pizzacount"));
                user.setSteakmoney(resultSet.getInt("steakmoney"));
                user.setPastamoney(resultSet.getInt("pastamoney"));
                user.setPizzamoney(resultSet.getInt("pizzamoney"));
                user.setTotalmoney(resultSet.getInt("totalmoney"));
                user.setDiscount(resultSet.getInt("discount"));
                user.setVipdiscount(resultSet.getInt("vipdiscount"));
                user.setCouponcheck(resultSet.getBoolean("couponcheck"));
                user.setVipcheck(resultSet.getBoolean("vipcheck"));

                users.add(user);
                log.info("가져온 사용자 수: {}", users.size());
                log.info("사용자: {}", users);
            }
        }
        catch (SQLException e)
        {
            log.error("데이터베이스 오류입니다", e);
        }

        return users;
    }


    @Override
    public Long login(String loginid, String password) {
        for(Map.Entry<Long, User> entry : data.entrySet())
        {
            User user = entry.getValue();
            if (user.getLoginid().equals(loginid) && user.getPassword().equals(password))
            {
                loginId = entry.getKey();
                System.out.println();
                System.out.println("로그인한 id = " + loginId);
                System.out.println();
                return loginId;
            }
        }
        // Map에 저장된 데이터를 기준으로 loginid와 password가 일치하는 곳의 key 값인 id값을 가져와서 loginId에 재할당
        // 일치하는 데이터가 없으면 null을 반환
        return null;
    }



    @Override
    public void steakorder(Long id, int steakcount) {
        User user = data.get(id);
        steakmoney = steakcount * 20000;
        user.setSteakcount(user.getSteakcount() + steakcount);
        user.addSteakmoney(steakmoney);
        user.addTotalmoney(steakmoney);

        // 스테이크 주문이 들어오면 로그인중인 id 값에 value 값을 전달
        // 스테이크 금액은 매개변수로 입력받은 스테이크 개수에 스테이크 값을 곱해서 계산
        // 스테이크 개수는 입력 받은 스테이크 개수를 더해서 누적
        // 스테이크 금액과 총 금액은 각각 스테이크 금액 필드변수, 총 금액 필드변수에 카운트 기법으로 누적

        String SteakOrderSql = "update restaurant set steakcount = ?, steakmoney = ?, totalmoney = ? where id = ?";

        try
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SteakOrderSql);

            preparedStatement.setInt(1, user.getSteakcount());
            preparedStatement.setInt(2, user.getSteakmoney());
            preparedStatement.setInt(3, user.getTotalmoney());
            preparedStatement.setLong(4, user.getId());

            int resultSize = preparedStatement.executeUpdate();

            System.out.println();
            log.info("steakorder result = {}", resultSize);
            System.out.println();
            // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
        }
        catch (SQLException e)
        {
            System.out.println("데이터 오류 입니다" + e);
        }

        if (vipcount == 0 && user.getTotalmoney() > 200000)
        {
            user.setRating(Rating.VIP);
            user.setVipdiscount(5000);
            user.setTotalmoney(user.getTotalmoney() - 5000);
            user.setVipcheck(true);
            vipcount = 1;

            // vip 혜택을 받기 전이고 총 금액이 20만원을 넘으면 등급을 vip로 변경
            // 변경 후 총 금액에서 5000원을 뺌
            // vip 혜택 여부도 true로 변경해서 더 이상 할인을 받지 못하게 함
            // 그 뒤 vip count값을 1로 변경 해서 해당 if문 조건으로 들어오지 못하게 막음

            String SteakOrderVipSql = "update restaurant set rating = ?, vipdiscount = ?, totalmoney = ?, vipcheck = ? where id = ?";

            try
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                connection = getConnection();
                preparedStatement = connection.prepareStatement(SteakOrderVipSql);

                preparedStatement.setString(1, user.getRating().name());
                preparedStatement.setInt(2, user.getVipdiscount());
                preparedStatement.setInt(3, user.getTotalmoney());
                preparedStatement.setBoolean(4, user.getVipcheck());
                preparedStatement.setLong(5, user.getId());


                int resultSize = preparedStatement.executeUpdate();

                System.out.println();
                log.info("steakorder result = {}", resultSize);
                System.out.println();
                // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류 입니다" + e);
            }

        }
        if (vipcount > 0 && user.getTotalmoney() > 200000)
        {
            System.out.println("이미 할인 받으셨습니다");

            // vipcount가 0보다 크고 총 금액이 20만원이 넘으면 더 이상 할인헤택 불가
        }
        System.out.println();
        System.out.println(data.toString());
        System.out.println();
    }

    @Override
    public void pastaorder(Long id, int pastacount) {
        User user = data.get(id);
        pastamoney = pastacount * 10000;
        user.setPastacount(user.getPastacount() + pastacount);
        user.addPastamoney(pastamoney);
        user.addTotalmoney(pastamoney);

        String PastaOrderSql = "update restaurant set pastacount = ?, pastamoney = ?, totalmoney = ? where id = ?";

        try
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            connection = getConnection();
            preparedStatement = connection.prepareStatement(PastaOrderSql);

            preparedStatement.setInt(1, user.getPastacount());
            preparedStatement.setInt(2, user.getPastamoney());
            preparedStatement.setInt(3, user.getTotalmoney());
            preparedStatement.setLong(4, user.getId());


            int resultSize = preparedStatement.executeUpdate();

            System.out.println();
            log.info("pastaorder result = {}", resultSize);
            System.out.println();
            // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
        }
        catch (SQLException e)
        {
            System.out.println("데이터 오류 입니다" + e);
        }

        if (vipcount == 0 && user.getTotalmoney() > 200000)
        {
            user.setRating(Rating.VIP);
            user.setVipdiscount(5000);
            user.setTotalmoney(user.getTotalmoney() - 5000);
            user.setVipcheck(true);
            vipcount = 1;

            String PastaOrderVipSql = "update restaurant set rating = ?, vipdiscount = ?, totalmoney = ?, vipcheck = ? where id = ?";

            try
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                connection = getConnection();
                preparedStatement = connection.prepareStatement(PastaOrderVipSql);

                preparedStatement.setString(1, user.getRating().name());
                preparedStatement.setInt(2, user.getVipdiscount());
                preparedStatement.setInt(3, user.getTotalmoney());
                preparedStatement.setBoolean(4, user.getVipcheck());
                preparedStatement.setLong(5, user.getId());


                int resultSize = preparedStatement.executeUpdate();

                System.out.println();
                log.info("pastaorder result = {}", resultSize);
                System.out.println();
                // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류 입니다" + e);
            }
        }
        if (vipcount > 0 && user.getTotalmoney() > 200000)
        {
            System.out.println("이미 할인 받으셨습니다");
        }
        System.out.println();
        System.out.println(data.toString());
        System.out.println();
    }

    @Override
    public void pizzaorder(Long id, int pizzacount) {
        User user = data.get(id);
        pizzamoney = pizzacount * 15000;
        user.setPizzacount(user.getPizzacount() + pizzacount);
        user.addPizzamoney(pizzamoney);
        user.addTotalmoney(pizzamoney);

        String PizzaOrderSql = "update restaurant set pizzacount = ?, pizzamoney = ?, totalmoney =? where id = ?";

        try
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            connection = getConnection();
            preparedStatement = connection.prepareStatement(PizzaOrderSql);

            preparedStatement.setInt(1, user.getPizzacount());
            preparedStatement.setInt(2, user.getPizzamoney());
            preparedStatement.setInt(3, user.getTotalmoney());
            preparedStatement.setLong(4, user.getId());


            int resultSize = preparedStatement.executeUpdate();

            System.out.println();
            log.info("pizzaorder result = {}", resultSize);
            System.out.println();
            // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
        }
        catch (SQLException e)
        {
            System.out.println("데이터 오류 입니다" + e);
        }
        if (vipcount == 0 && user.getTotalmoney() > 200000)
        {
            user.setRating(Rating.VIP);
            user.setVipdiscount(5000);
            user.setTotalmoney(user.getTotalmoney() - 5000);
            user.setVipcheck(true);
            vipcount = 1;

            String PizzaOrderVipSql = "update restaurant set rating =?, vipdiscount = ?, totalmoney = ?, vipcheck = ? where id = ?";

            try
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                connection = getConnection();
                preparedStatement = connection.prepareStatement(PizzaOrderVipSql);

                preparedStatement.setString(1, user.getRating().name());
                preparedStatement.setInt(2, user.getVipdiscount());
                preparedStatement.setInt(3, user.getTotalmoney());
                preparedStatement.setBoolean(4, user.getVipcheck());
                preparedStatement.setLong(5, user.getId());


                int resultSize = preparedStatement.executeUpdate();

                System.out.println();
                log.info("pizzaorder result = {}", resultSize);
                System.out.println();
                // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
            }
            catch (SQLException e)
            {
                System.out.println("데이터 오류 입니다" + e);
            }
        }
        if (vipcount > 0 && user.getTotalmoney() > 200000)
        {
            System.out.println("이미 할인 받으셨습니다");
        }
        System.out.println();
        System.out.println(data.toString());
        System.out.println();
    }

    @Override
    public void couponcheck(Long id) {
        User user = data.get(id);
        user.setCouponcheck(true);
        user.setTotalmoney(user.getTotalmoney() - 5000);
        user.setDiscount(5000);

        // 홈페이지에서 쿠폰 받기 버튼을 클릭 하면 해당 id value 데이터를 변경
        // 쿠폰체크를 true로 바꾸고 더 이상 할인을 못받게함
        // 총 금액에서 5000원을 뺌
        // 얼마 할인 받았는지 표시하기 위해 할인금액을 따로 저장

        String CouponCheckSql = "update restaurant set couponcheck = ?, totalmoney =?, discount = ? where id = ?";

        try
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            connection = getConnection();
            preparedStatement = connection.prepareStatement(CouponCheckSql);

            preparedStatement.setBoolean(1, user.getCouponcheck());
            preparedStatement.setInt(2, user.getTotalmoney());
            preparedStatement.setInt(3, user.getDiscount());
            preparedStatement.setLong(4, user.getId());


            int resultSize = preparedStatement.executeUpdate();

            System.out.println();
            log.info("couponcheck result = {}", resultSize);
            System.out.println();
            // resultSize가 1이 나오면 성공적으로 수정, 0이 나오면 실패
        }
        catch (SQLException e)
        {
            System.out.println("데이터 오류 입니다" + e);
        }
        System.out.println();
        System.out.println(data.toString());
        System.out.println();
    }

    private Connection getConnection()
    {
        return DBConnectionUtility.getConnection();
    }
}
