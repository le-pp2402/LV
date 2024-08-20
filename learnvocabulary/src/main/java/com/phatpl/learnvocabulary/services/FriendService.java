package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.mappers.UserResponseMapper;
import com.phatpl.learnvocabulary.repositories.FriendRepository;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.utils.Constant;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendService {

    private static final Logger log = LoggerFactory.getLogger(FriendService.class);
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserService userService;

    @Autowired
    public FriendService(FriendRepository friendRepository, UserRepository userRepository, UserResponseMapper userResponseMapper, UserService userService) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.userService = userService;
    }


    public void sendFriendRequest(Integer toUserID) {
        var fromUserID = userService.extractUserId();
        if (userRepository.findById(toUserID).isEmpty()) throw new EntityNotFoundException(Constant.ACCOUNT_NOT_FOUND);
        friendRepository.createFriendRequest(fromUserID, toUserID);
    }

    public void acceptFriendRequest(Integer toUserID) {
        var fromUserID = userService.extractUserId();
        if (userRepository.findById(toUserID).isEmpty()) throw new EntityNotFoundException(Constant.ACCOUNT_NOT_FOUND);
        friendRepository.createFriendRequest(fromUserID, toUserID);
    }

    public List<UserResponse> findRequestFriend() {
        var userID = userService.extractUserId();
        var friends = friendRepository.findRequestFriend(userID);
        var result = new ArrayList<UserResponse>();
        for (var id : friends) {
            var userOpt = userRepository.findById(id);
            userOpt.ifPresent(t -> result.add(userResponseMapper.toDTO(t)));
        }
        return result;
    }

    public List<UserResponse> findFriends() {
        var userID = userService.extractUserId();
        var friends = friendRepository.findFriends(userID);
        var result = new ArrayList<UserResponse>();
        for (var id : friends) {
            var userOpt = userRepository.findById(id);
            userOpt.ifPresent(t -> result.add(userResponseMapper.toDTO(t)));
        }
        return result;
    }

    public List<UserResponse> getFriendRecommends() {
        var userID = userService.extractUserId();
        var friends = friendRepository.getFriendRecommends(userID);
        var result = new ArrayList<UserResponse>();
        for (var id : friends) {
            var userOpt = userRepository.findById(id);
            userOpt.ifPresent(t -> result.add(userResponseMapper.toDTO(t)));
        }
        return result;
    }

    public void refuseFriendRequest(Integer toUserID) {
        var fromUserID = userService.extractUserId();
        log.info(fromUserID.toString(), toUserID.toString());
        friendRepository.refuseFriendRequest(fromUserID, toUserID);
    }
}
