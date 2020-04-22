package main.controller;

import main.model.TestUser;
import main.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DefaultController {
//    @Autowired
//    private UserRepository userRepository;


    @GetMapping("/api/init/")
    public String index(Model model) {
//        Iterable<User> userIterable = userRepository.findAll();
//        ArrayList<User> tasks = new ArrayList<>();
//        for(User user : userIterable) {
//            tasks.add(user);
//        }
//        model.addAttribute("tasks", tasks);
//        model.addAttribute("tasksCount", tasks.size());
        TestUser testUser = new TestUser("sdsd","32","dsf","dsf","dsf","dsf");
//
        testUser.setTitle("sd");
        testUser.setCopyright("dfsf");
        testUser.setCopyrightFrom("ert");
        testUser.setEmail("ewrewrwer");
        testUser.setPhone("er");
        testUser.setSubtitle("ewrwerwerewrwer");

        model.addAttribute("title",testUser.getTitle());
        model.addAttribute("subtitle",testUser.getSubtitle());
        model.addAttribute("phone",testUser.getPhone());
        model.addAttribute("email",testUser.getEmail());
        model.addAttribute("copyright",testUser.getCopyright());
        model.addAttribute("copyrightFrom",testUser.getCopyrightFrom());

        return model.toString();
    }
}