package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.models.graph.NUser;
import com.phatpl.learnvocabulary.models.graph.relationship.Friendship;
import com.phatpl.learnvocabulary.repositories.graph.EFriendshipRepo;
import com.phatpl.learnvocabulary.repositories.graph.UserRepo;
import com.phatpl.learnvocabulary.utils.Constant;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendService {

    static final Logger log = LoggerFactory.getLogger(FriendService.class);
    final EFriendshipRepo eFriendshipRepo;
    final UserRepo userRepo;
    final UserService userService;

    @Autowired
    public FriendService(EFriendshipRepo eFriendshipRepo, UserRepo userRepo, UserService userService) {
        this.eFriendshipRepo = eFriendshipRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    public void sendFriendRequest(Long toUserId) {
        var userId = userService.extractUserId();

        var otherUser = userService.findById(Math.toIntExact(toUserId));

        Long chkSentRequest = eFriendshipRepo.findFriendship(Long.valueOf(userId), toUserId);
        if (chkSentRequest != null) return;

        Long id = eFriendshipRepo.findFriendship(toUserId, Long.valueOf(userId));
        if (id != null) {
            var friendship = eFriendshipRepo.findById(id);
            if (friendship.isPresent()) {
                friendship.get().setStatus(1L);
                eFriendshipRepo.save(friendship.get());
            }
        } else {
            var friendship = new Friendship();
            var fromUser = userRepo.findByUsername(userService.me().getUsername());
            var toUser = userRepo.findByUsername(otherUser.getUsername());

            if (fromUser.isEmpty() || toUser.isEmpty()) throw new EntityNotFoundException(Constant.ACCOUNT_NOT_FOUND);

            friendship.setFromNUser(fromUser.get());
            friendship.setToNUser(toUser.get());
            friendship.setStatus(0L);

            eFriendshipRepo.save(friendship);
        }
    }

    public List<NUser> findRequestFriend() {
        var userID = userService.extractUserId();
        return userRepo.getFriendRequests(Long.valueOf(userID));
    }

    public List<NUser> getFriends() {
        var userID = userService.extractUserId();
        return userRepo.getFriends(Long.valueOf(userID));
    }

    public List<NUser> getSuggestFriends() {
        var userId = userService.extractUserId();
        return userRepo.suggestFriends(Long.valueOf(userId));
    }

    public void refuseFriendRequest(Long toUserID) {
        var userId = userService.extractUserId();
        Long id = eFriendshipRepo.findFriendship(Long.valueOf(userId), toUserID);

        if (id != null) {
            eFriendshipRepo.deleteById(id);
        }

        Long oId = eFriendshipRepo.findFriendship(toUserID, Long.valueOf(userId));

        if (oId != null) {
            eFriendshipRepo.deleteById(oId);
        }
    }
}
