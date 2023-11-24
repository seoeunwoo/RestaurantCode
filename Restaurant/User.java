package hello.restaurant.user;

public class User {

    // 회원가입 정보
    private Long id;
    private String loginid;
    private String password;
    private String name;
    private int age;
    private String email;
    private Rating rating;

    // 음식 주문 카운트
    private int steakcount;
    private int pastacount;
    private int pizzacount;

    // 음식 주문 총 가격
    private int steakmoney;
    private int pastamoney;
    private int pizzamoney;

    // 총 매출
    private int totalmoney;

    // 할인
    private int discount;
    // vip할인
    private int vipdiscount;

    // 할인 쿠폰 발급 여부
    private boolean couponcheck;
    // vip 확인 여부
    private boolean vipcheck;

    public User(){

    }

    public User(String loginid, String password, String name, int age, String email) {
        this.loginid = loginid;
        this.password = password;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public int getSteakcount() {
        return steakcount;
    }

    public void setSteakcount(int steakcount) {
        this.steakcount = steakcount;
    }

    public int getPastacount() {
        return pastacount;
    }

    public void setPastacount(int pastacount) {
        this.pastacount = pastacount;
    }

    public int getPizzacount() {
        return pizzacount;
    }

    public void setPizzacount(int pizzacount) {
        this.pizzacount = pizzacount;
    }

    public int getSteakmoney() {
        return steakmoney;
    }

    public void setSteakmoney(int steakmoney) {
        this.steakmoney = steakmoney;
    }

    public int getPastamoney() {
        return pastamoney;
    }

    public void addSteakmoney(int amount)
    {
        steakmoney = steakmoney + amount;
    }

    public void setPastamoney(int pastamoney) {
        this.pastamoney = pastamoney;
    }
    public void addPastamoney(int amount)
    {
        pastamoney = pastamoney + amount;
    }

    public int getPizzamoney() {
        return pizzamoney;
    }

    public void setPizzamoney(int pizzamoney) {
        this.pizzamoney = pizzamoney;
    }
    public void addPizzamoney(int amount)
    {
        pizzamoney = pizzamoney + amount;
    }

    public int getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(int totalmoney) {
        this.totalmoney = totalmoney;
    }
    public void addTotalmoney(int amount)
    {
        totalmoney = totalmoney + amount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean getCouponcheck() {
        return couponcheck;
    }

    public void setCouponcheck(boolean couponcheck) {
        this.couponcheck = couponcheck;
    }

    public int getVipdiscount() {
        return vipdiscount;
    }

    public void setVipdiscount(int vipdiscount) {
        this.vipdiscount = vipdiscount;
    }

    public boolean getVipcheck() {
        return vipcheck;
    }

    public void setVipcheck(boolean vipcheck) {
        this.vipcheck = vipcheck;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginid='" + loginid + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", rating=" + rating +
                ", steakcount=" + steakcount +
                ", pastacount=" + pastacount +
                ", pizzacount=" + pizzacount +
                ", steakmoney=" + steakmoney +
                ", pastamoney=" + pastamoney +
                ", pizzamoney=" + pizzamoney +
                ", totalmoney=" + totalmoney +
                ", discount=" + discount +
                ", vipdiscount=" + vipdiscount +
                ", couponcheck=" + couponcheck +
                ", vipcheck=" + vipcheck +
                '}';
    }
}
