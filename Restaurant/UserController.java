package hello.restaurant.usercontroller;

import hello.restaurant.user.Rating;
import hello.restaurant.user.User;
import hello.restaurant.user.UserService;
import hello.restaurant.user.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/restaurant")
public class UserController {
    UserService userService = new UserServiceImpl();

    private Long restaurantId;
    private String userloginId;

    // 회원가입, 로그인화면
    @GetMapping("/index")
    public String index()
    {
        return "/restaurant/index";
    }

    // 회원가입
    @GetMapping("/join")
    public String Join()
    {
        return "/restaurant/join";
    }

    @PostMapping("/join")
    public String JoinComplete(@ModelAttribute User user)
    {
        userService.save(user);
        return "redirect:/restaurant/index";
    }

    // 로그인
    @GetMapping("/login")
    public String Login(Model model)
    {
        model.addAttribute("loginid", restaurantId);
        return "/restaurant/login";
    }

    @PostMapping("/login")
    public String LoginComplete(@RequestParam("loginid") String loginId,
                                @RequestParam("password") String password, Model model)
    {
        restaurantId = userService.login(loginId, password);
        System.out.println("loginId = " + loginId);


        if (restaurantId != null)
        {
            userloginId = loginId;
            return "redirect:/restaurant/main";
        }
        else
        {
            model.addAttribute("loginid", restaurantId);
            model.addAttribute("loginFailed", "로그인 정보가 없습니다.");
            return "/restaurant/login";
        }

    }

    // 메인화면
    @GetMapping("/main")
    public String Main(Model model)
    {
        User user = userService.findById(restaurantId);
        System.out.println("userloginId = " + userloginId);
        model.addAttribute("user", user);
        model.addAttribute("userloginid", userloginId);
        return "/restaurant/main";
    }

    // 음식주문
    @GetMapping("/order/{id}")
    public String Order(@PathVariable("id") Long id, Model model)
    {
        User user = userService.findById(id);
        System.out.println("넘겨 받은 id = " + id);
        model.addAttribute("user", user);
        model.addAttribute("userloginid", userloginId);
        return "/restaurant/order";
    }
    // 음식 사진 클릭하면 @PathVarible로 id값 부여하고 저장

    // 스테이크 주문
    @GetMapping("/steakorder/{id}")
    public String SteakOrder(@PathVariable("id") Long id, Model model)
    {
        User user = userService.findById(id);
        System.out.println("넘겨 받은 id = " + id);
        model.addAttribute("user", user);
        model.addAttribute("userloginid", userloginId);
        return "/restaurant/steakorder";
    }

    @PostMapping("/steakorder/{id}")
    public String SteakOrderComplete(@PathVariable("id") Long id,
                                     @RequestParam("steakcount") int steakcount)
    {
        userService.steakorder(id, steakcount);
        return "redirect:/restaurant/order/{id}";
    }

    // 파스타 주문
    @GetMapping("/pastaorder/{id}")
    public String PastaOrder(@PathVariable("id") Long id, Model model)
    {
        User user = userService.findById(id);
        System.out.println("넘겨 받은 id = " + id);
        model.addAttribute("user", user);
        model.addAttribute("userloginid", userloginId);
        return "/restaurant/pastaorder";
    }

    @PostMapping("/pastaorder/{id}")
    public String PastaOrderComplete(@PathVariable("id") Long id, int pastacount)
    {
        userService.pastaorder(id, pastacount);
        return "redirect:/restaurant/order/{id}";
    }

    // 피자 주문
    @GetMapping("/pizzaorder/{id}")
    public String PizzaOrder(@PathVariable("id") Long id, Model model)
    {
        User user = userService.findById(id);
        System.out.println("넘겨 받은 id = " + id);
        model.addAttribute("user", user);
        model.addAttribute("userloginid", userloginId);
        return "/restaurant/pizzaorder";
    }

    @PostMapping("/pizzaorder/{id}")
    public String PizzaOrderComplete(@PathVariable("id") Long id, int pizzacount)
    {
        userService.pizzaorder(id, pizzacount);
        return "redirect:/restaurant/order/{id}";
    }

    // 내 주문 리스트
    @GetMapping("/myorderlist/{id}")
    public String MyOrderList(@PathVariable("id") Long id, Model model)
    {
        User user = userService.findById(id);
        int steakcount = user.getSteakcount();
        int steakmoney = user.getSteakmoney();
        int pastacount = user.getPastacount();
        int pastamoney = user.getPastamoney();
        int pizzacount = user.getPizzacount();
        int pizzamoney = user.getPizzamoney();
        int totalmoney = user.getTotalmoney();
        Rating rating = user.getRating();
        boolean coupon = user.getCouponcheck();
        int discount = user.getDiscount();
        model.addAttribute("user", user);
        model.addAttribute("steakcount", steakcount);
        model.addAttribute("steakmoney", steakmoney);
        model.addAttribute("pastacount", pastacount);
        model.addAttribute("pastamoney", pastamoney);
        model.addAttribute("pizzacount", pizzacount);
        model.addAttribute("pizzamoney", pizzamoney);
        model.addAttribute("rating", rating);
        model.addAttribute("coupon", coupon);
        if (user.getRating() == Rating.VIP)
        {
            model.addAttribute("vipdiscount", user.getVipdiscount());
        }
        model.addAttribute("discount", discount);
        model.addAttribute("totalmoney", totalmoney);
        model.addAttribute("userloginid", userloginId);
        return "/restaurant/myorderlist";
    }

    // 쿠폰발급
    @GetMapping("/coupon/{id}")
    public String Coupon(@PathVariable("id") Long id, Model model)
    {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("userloginid", userloginId);
        return "/restaurant/coupon";
    }

    @PostMapping("/coupon/{id}")
    public String ConponIssue(@PathVariable("id") Long id, Model model)
    {
        userService.couponcheck(id);
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("userloginid", userloginId);
        return "redirect:/restaurant/coupon/{id}";
    }

    // 전체 주문자 리스트
    @GetMapping("/allorderlist")
    public String AllSales(Model model)
    {
        List<User> users = userService.findAll();
        model.addAttribute("user", users);
        return "/restaurant/allorderlist";
    }

}
