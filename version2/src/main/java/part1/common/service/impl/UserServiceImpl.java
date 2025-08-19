package part1.common.service.impl;

import part1.common.pojo.User;
import part1.common.service.UserService;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("getUserByUserId id = " + id);

        Random random = new Random();
        User user = User.builder()
                .id(id)
                .userName(UUID.randomUUID().toString())
                .sex(random.nextBoolean())
                .build();
        return user;
    }

    @Override
    public Integer insertUserId(User user) {
        System.out.println("insertUserId id = " + user.getId());
        return user.getId();
    }
}
