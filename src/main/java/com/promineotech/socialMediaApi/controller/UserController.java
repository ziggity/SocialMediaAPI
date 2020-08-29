package com.promineotech.socialMediaApi.controller;

import com.promineotech.socialMediaApi.entity.User;
import com.promineotech.socialMediaApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/users")
public class UserController {

    private static String UPLOADED_FOLDER = "./pictures/";

    @Autowired
    private UserService service;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestBody User user) {
        return new ResponseEntity<Object>(service.createUser(user), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Object> login(@RequestBody User user) {
        try {
            return new ResponseEntity<Object>(service.login(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}/follows")
    public ResponseEntity<Object> showFollowedUsers(@PathVariable Long id) {
        try {
            return new ResponseEntity<Object>(service.getFollowedUsers(id), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}/follows/{followId}", method = RequestMethod.POST)
    public ResponseEntity<Object> follow(@PathVariable Long id, @PathVariable Long followId) {
        try {
            return new ResponseEntity<Object>(service.follow(id, followId), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/{id}/profilePicture", method = RequestMethod.POST)
    public ResponseEntity<Object> singleFileUpload(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<Object>("Please upload a file.", HttpStatus.BAD_REQUEST);
        }

        try {
            String url = UPLOADED_FOLDER + file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(url);
            Files.write(path, bytes);
            return new ResponseEntity<Object>(service.updateProfilePicture(id, url), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
