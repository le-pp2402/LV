package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.request.FriendRequest;
import com.phatpl.learnvocabulary.exceptions.UnauthorizationException;
import com.phatpl.learnvocabulary.services.FriendService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping
    public ResponseEntity<?> getFriends() {
        try {
            return BuildResponse.ok(friendService.getFriends());
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PutMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody FriendRequest request) {
        try {
            friendService.sendFriendRequest(request.getUserId());
            return BuildResponse.ok("success");
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PutMapping("/refuse")
    public ResponseEntity<?> refuseFriendRequest(@RequestBody FriendRequest request) {
        try {
            friendService.refuseFriendRequest(request.getUserId());
            return BuildResponse.ok("success");
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PostMapping("/send-request")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequest request) {
        try {
            friendService.sendFriendRequest(request.getUserId());
            return BuildResponse.ok("success");
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/friend-recommend")
    public ResponseEntity<?> friendRecommend() {
        try {
            return BuildResponse.ok(friendService.getSuggestFriends());
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/friend-request")
    public ResponseEntity<?> getFriendRequest() {
        try {
            return BuildResponse.ok(friendService.findRequestFriend());
        } catch (UnauthorizationException e) {
            return BuildResponse.unauthorized(e.getMessage());
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }
}
