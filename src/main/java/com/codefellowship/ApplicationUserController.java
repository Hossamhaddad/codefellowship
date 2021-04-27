package com.codefellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class ApplicationUserController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    PostRepository postRepository;

    @GetMapping("/")
    public String homeHandler(){
        return "home.html";
    }
    @GetMapping("/login")
    public String loginHandler(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login.html";
        }
        return "home.html";
    }
    @GetMapping("/signup")
    public String singUpHandler(){
        return "signup.html";
    }

    @PostMapping("/signup")
    public RedirectView signup(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String firstname,
                               @RequestParam String lastname,
                               @RequestParam String dateOfBirth,
                               @RequestParam String bio){
        ApplicationUser newUser = new ApplicationUser(username,bCryptPasswordEncoder.encode(password),firstname,lastname,dateOfBirth,bio);
        newUser = applicationUserRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/");
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model m, Principal p){
     ApplicationUser user=applicationUserRepository.findByUsername(username);
     if(user==null){
         return "login.html";
     }
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      return "home.html";

    }
    @GetMapping("/user/{id}")
    public String user(Model m, @PathVariable("id") Integer id){
       m.addAttribute("user",  applicationUserRepository.findById(id).get());

    return "user.html";
    }
    @GetMapping("/profile")
    public String profile(Model m, Principal p){
        ApplicationUser userDetails = (ApplicationUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();
        m.addAttribute("user", applicationUserRepository.findById (userDetails.getId()).get());
        System.out.println(applicationUserRepository.findById (userDetails.getId()).get().getPosts());
        return "profile.html";
    }

    @PutMapping("/user/{id}")
    public RedirectView editProfile(Principal p, @PathVariable Integer id,
                                    @RequestParam String firstname,
                                    @RequestParam String lastname,
                                    @RequestParam String dateOfBirth,
                                    @RequestParam String password,
                                    @RequestParam String bio){
        String loggedInUserName = p.getName();
        ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
            loggedInUser.setFirstname(firstname);
            loggedInUser.setLastname(lastname);
            loggedInUser.setDateOfBirth(dateOfBirth);
            loggedInUser.setBio(bio);
            loggedInUser.setPassword(bCryptPasswordEncoder.encode(password));
            applicationUserRepository.save(loggedInUser);
            return new RedirectView("/user/"+id);
        }
    }


