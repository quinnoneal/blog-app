package com.demo.springbootblogapplication.config;

import com.demo.springbootblogapplication.model.Account;
import com.demo.springbootblogapplication.model.Authority;
import com.demo.springbootblogapplication.model.Post;
import com.demo.springbootblogapplication.repository.AuthorityRepository;
import com.demo.springbootblogapplication.service.AccountService;
import com.demo.springbootblogapplication.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// @Component tells spring framework we want it to work with this object and
// register it once it spins up all the other objects within the application
@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityRepository authorityRepository;

    // dummy posts if no other posts in database for testing
    @Override
    public void run(String... args) throws Exception {
        List<Post> posts = postService.getAll();

        // create base accounts
        Account user = new Account();
        Account donor = new Account();
        Account admin = new Account();

        // set up Authority Roles
        Authority userAuth = new Authority();
        userAuth.setName("ROLE_USER");
        authorityRepository.save(userAuth);

        Authority donorAuth = new Authority();
        donorAuth.setName("ROLE_DONOR");
        authorityRepository.save(donorAuth);

        Authority adminAuth = new Authority();
        adminAuth.setName("ROLE_ADMIN");
        authorityRepository.save(adminAuth);

        // set base account properties
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user.user@domain.com");
        user.setPassword("password");
        Set<Authority> userAuthorities = new HashSet<>();
        Optional<Authority> optionalUserAuthority = authorityRepository.findById("ROLE_USER");
        optionalUserAuthority.ifPresent(userAuthorities::add);
        user.setAuthorities(userAuthorities);

        donor.setFirstName("donor");
        donor.setLastName("donor");
        donor.setEmail("donor.donor@domain.com");
        donor.setPassword("password");
        Set<Authority> donorAuthorities = new HashSet<>();
        Optional<Authority> optionalDonorAuthority = authorityRepository.findById("ROLE_DONOR");
        optionalUserAuthority.ifPresent(donorAuthorities::add);
        optionalDonorAuthority.ifPresent(donorAuthorities::add);
        donor.setAuthorities(donorAuthorities);

        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin.admin@domain.com");
        admin.setPassword("password");
        Set<Authority> adminAuthorities = new HashSet<>();
        Optional<Authority> optionalAdminAuthority = authorityRepository.findById("ROLE_ADMIN");
        optionalUserAuthority.ifPresent(adminAuthorities::add);
        optionalDonorAuthority.ifPresent(adminAuthorities::add);
        optionalAdminAuthority.ifPresent(adminAuthorities::add);
        admin.setAuthorities(adminAuthorities);

        accountService.save(user);
        accountService.save(donor);
        accountService.save(admin);

        if (posts.size() == 0) {
            Post post1 = new Post();
            post1.setTitle("Title of post 1");
            post1.setBody("This is the body of post 1");
            post1.setAccount(user);
            Post post2 = new Post();
            post2.setTitle("Title of post 2");
            post2.setBody("This is the body of post 2");
            post2.setAccount(admin);
            postService.save(post1);
            postService.save(post2);
        }

    }
}
