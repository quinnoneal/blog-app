package com.demo.springbootblogapplication.controller;

import com.demo.springbootblogapplication.model.Account;
import com.demo.springbootblogapplication.model.Post;
import com.demo.springbootblogapplication.service.AccountService;
import com.demo.springbootblogapplication.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/posts")
    public String postsHome(Model model) {
        List<Post> posts = postService.getAll();
        model.addAttribute("posts", posts);
        return "posts_home";
    }

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post";
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/new")
    public String getNewPostPage(Model model, Principal principal) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Optional<Account> optionalAccount = accountService.findByEmail(authUsername);
        if (optionalAccount.isPresent()) {
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_new";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/posts/new")
    public String saveNewPost(@ModelAttribute Post post, Principal principal) {
        String authUsername = "anonymousUser";

        if (principal != null) {
            authUsername = principal.getName();
        }

        if (post.getAccount().getEmail().compareToIgnoreCase(authUsername) < 0) {
            return "404";
        }
        postService.save(post);
        return "redirect:/posts/" + post.getId();
    }
}
