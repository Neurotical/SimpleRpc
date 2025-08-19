package part1.common.service;

import part3.common.pojo.User;

public interface UserService {
    User getUserByUserId(Integer id);

    Integer insertUserId(User user);
}
