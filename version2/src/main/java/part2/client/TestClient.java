package part2.client;

import part2.client.proxy.ClientProxy;
import part2.common.pojo.User;
import part2.common.service.UserService;

public class TestClient {
    public static void main(String[] args) {
        ClientProxy clientProxy = null;
        try {
            clientProxy = new ClientProxy();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        UserService userService = clientProxy.getProxy(UserService.class);

        User userByUserId = userService.getUserByUserId(1);
        System.out.println("get User from Server where id = 1:" + userByUserId);

        User insertUser = User.builder()
                .id(1234)
                .userName("testInsertUser")
                .sex(true)
                .build();
        Integer id = userService.insertUserId(insertUser);
        System.out.println("insert User from Server where id =" + id);
    }
}
