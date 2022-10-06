package com.example.java_proj;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import matcher.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MatcherController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CompletedOrdersService completedOrdersService;
    private MatchOrders matcher = new MatchOrders();
    private Account activeAccount = new Account(0,"","","");

    @RequestMapping
    public String HelloWorld(){
        return "Hello World from Spring Boot";
    }

    @RequestMapping("/goodbye")
    public String goodbye(){
        return "Goodbye from Spring Boot";
    }

    @PostMapping(value = "/addOrder")
    public String addNewOrder(@Valid @RequestBody Orders order){
        ArrayList<CompletedOrders> completedOrdersList = new ArrayList<>();
        orderService.addOrder(order);
        ArrayList<Orders> pendingOrders = orderService.fetchOrderList();
        ArrayList<CompletedOrders> currentCompletedOrders = completedOrdersService.fetchCompletedOrdersList();
        matcher.matchOrder(pendingOrders,order,completedOrdersList);
        if (completedOrdersList.size()>0){
            for (int i=0;i<completedOrdersList.size();i++){
                if (completedOrdersList.get(i).getOrderStatus().equals("complete")){
                    for (int j=0;j<currentCompletedOrders.size();j++){
                        if (currentCompletedOrders.get(j).getOrderNumber()==completedOrdersList.get(i).getOrderNumber()){
                            completedOrdersList.get(i).setQuantity(completedOrdersList.get(i).getQuantity()+currentCompletedOrders.get(j).getQuantity());
                        }
                    }
                    completedOrdersService.addCompletedOrder(completedOrdersList.get(i));
                    orderService.deleteOrderByID(matcher.completeOrderToOrder(completedOrdersList.get(i)));
                }
                else if (completedOrdersList.get(i).getOrderStatus().equals("partial")){
                    for (int j=0;j<currentCompletedOrders.size();j++){
                        if (currentCompletedOrders.get(j).getOrderNumber()==completedOrdersList.get(i).getOrderNumber()){
                            completedOrdersList.get(i).setQuantity(completedOrdersList.get(i).getQuantity()+currentCompletedOrders.get(j).getQuantity());
                        }
                    }
                    completedOrdersService.addCompletedOrder(completedOrdersList.get(i));
                    Orders orderToUpdate = orderService.findByID(completedOrdersList.get(i).getOrderNumber()).get();
                    orderToUpdate.setQuantity(orderToUpdate.getQuantity()-completedOrdersList.get(i).getQuantity());
                }
            }
        }
        return "order added";
    }

    @PostMapping("/addAccount")
    public String addAccount(@Valid @RequestBody Account newAccount){
        boolean duplicateAccount = false;
        for (int i=0;i<accountService.fetchAccountList().size();i++) {
            if (newAccount.getAccountName().equals(accountService.fetchAccountList().get(i).getAccountName())){
                duplicateAccount = true;
                break;
            }
        }
        if (!duplicateAccount) {
            newAccount.setPassword(bCryptPasswordEncoder.encode(newAccount.getPassword()));
            accountService.addAccount(newAccount);
            return newAccount.getAccountNumber() + " added!";
        }
        else return "Account username taken";
    }

    @GetMapping("/getAccounts")
    public List<Account> fetchAccountList()
    {
        return accountService.fetchAccountList();
    }

    @GetMapping("/orders/{id}")
    public ArrayList<Orders> privateOrders(@PathVariable("id")
                                           int accountName) {
        ArrayList<Orders> privateOrders = new ArrayList<>();
        for (int i=0;i<orderService.fetchOrderList().size();i++){
            if (orderService.fetchOrderList().get(i).getAccountNumber()==accountName) {
                privateOrders.add(orderService.fetchOrderList().get(i));
            }
        }
        return privateOrders;
    }

    @PutMapping("/login")
    public String login(@RequestParam("account") String accountName, @RequestParam("password") String pwd) {
        boolean accountFound = false;
        for (int i=0; i<accountService.fetchAccountList().size();i++) {
            if (accountService.fetchAccountList().get(i).getAccountName().equals(accountName) &&
                    bCryptPasswordEncoder.matches(pwd,accountService.fetchAccountList().get(i).getPassword())) {
                int accountNumber = accountService.fetchAccountList().get(i).getAccountNumber();
                String token = getJWTToken(accountNumber);
                activeAccount.setToken(token);
                activeAccount.setPassword(pwd);
                activeAccount.setAccountNumber(accountNumber);
                activeAccount.setAccountName(accountName);
                accountService.updateAccount(activeAccount);
                accountFound = true;
                return token;
            }
        }
        if (!accountFound){
            return "Account not found";
        }
        return null;
    }

    @GetMapping("/aggBuyOrders")
    public HashMap<Double,AggOrder> aggBuyOrders(){
        return aggregateOrders("Buy");
    }

    @GetMapping("/aggSellOrders")
    public HashMap<Double,AggOrder> aggSellOrders(){
        return aggregateOrders("Sell");
    }

    public String getJWTToken(int accountNumber) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("JWT")
                .setSubject(String.valueOf(accountNumber))
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }

    public HashMap<Double, AggOrder> aggregateOrders(String action){
        ArrayList<Orders> ordersArrayList = orderService.fetchOrderList();
        HashMap<Double,AggOrder> ordersHash = new HashMap<>();
        for (int i=0; i<ordersArrayList.size();i++){
            if (ordersArrayList.get(i).getOrderType().equals(action)) {
                if (ordersHash.containsKey(ordersArrayList.get(i).getPrice())) {
                    AggOrder updateAggOrder = new AggOrder(ordersArrayList.get(i).getQuantity() + ordersHash.get(ordersArrayList.get(i).getPrice()).getQuantity(), ordersArrayList.get(i).getPrice());
                    ordersHash.replace(ordersArrayList.get(i).getPrice(), updateAggOrder);
                } else {
                    ordersHash.put(ordersArrayList.get(i).getPrice(), new AggOrder(ordersArrayList.get(i).getQuantity(), ordersArrayList.get(i).getPrice()));
                }
            }
        }
        return ordersHash;
    }
}
