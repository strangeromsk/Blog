package main.controller;

import main.model.User;
import main.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
public class DefaultController {
    @Autowired
    private UserRepository userRepository;


    @RequestMapping("/")
    public String index(Model model)
    {
//        Iterable<User> userIterable = userRepository.findAll();
//        ArrayList<User> tasks = new ArrayList<>();
//        for(User user : userIterable) {
//            tasks.add(user);
//        }
//        model.addAttribute("tasks", tasks);
//        model.addAttribute("tasksCount", tasks.size());
        return "index";
    }
}