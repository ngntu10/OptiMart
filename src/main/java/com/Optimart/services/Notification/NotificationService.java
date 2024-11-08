package com.Optimart.services.Notification;

import com.Optimart.constants.MessageKeys;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.Notification;
import com.Optimart.models.User;
import com.Optimart.repositories.AuthRepository;
import com.Optimart.repositories.NotificationRepository;
import com.Optimart.repositories.Specification.NotificationSpecification;
import com.Optimart.repositories.UserRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.utils.JwtTokenUtil;
import com.Optimart.utils.LocalizationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService{
    private final NotificationRepository notificationRepository;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final LocalizationUtils localizationUtils;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public PagingResponse<List<Notification>> getNotifications(Map<Object, String> filters) {
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String order = filters.get("order");
        if (page == -1 && limit == -1) {
            Specification<Notification> notificationSpecification = NotificationSpecification.filtersNotification(filters.get("userId"), filters.get("search"));
            List<Notification> notifications = notificationRepository.findAll(notificationSpecification, Sort.by(Sort.Direction.DESC, "createdAt"));
            int totalNew = (int) notifications.stream()
                    .filter(notification -> notification.getIsRead() == 0)
                    .count();
            return new PagingResponse<>(notifications, localizationUtils.getLocalizedMessage(MessageKeys.NOTIFICATION_GET_LIST_SUCCESS), 1, (long) notifications.size(), totalNew);
        }
        page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Notification> notificationSpecification = NotificationSpecification.filtersNotification(filters.get("userId"), filters.get("search"));
        Page<Notification> notifications = notificationRepository.findAll(notificationSpecification, pageable);
        int totalNew = (int) notifications.getContent().stream()
                .filter(notification -> notification.getIsRead() == 0)
                .count();
        return new PagingResponse<>(notifications.getContent(), localizationUtils.getLocalizedMessage(MessageKeys.NOTIFICATION_GET_LIST_SUCCESS), notifications.getTotalPages(), notifications.getTotalElements(), totalNew);
    }

    @Override
    @Transactional
    public APIResponse<Notification> markReadNotification(String notificationId) {
        Notification notification = notificationRepository.findById(UUID.fromString(notificationId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.NOTIFICATION_NOT_FOUND)));
        notification.setIsRead(1);
        notificationRepository.save(notification);
        return new APIResponse<>(notification, localizationUtils.getLocalizedMessage(MessageKeys.NOTIFICATION_READ));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteNotification(String notificationId) {
        Notification notification = notificationRepository.findById(UUID.fromString(notificationId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.NOTIFICATION_NOT_FOUND)));
        for (User user : notification.getUsers()) {
            user.getNotifications().remove(notification);
        }
        userRepository.saveAll(notification.getUsers());
        notification.getUsers().clear();
        notificationRepository.delete(notification);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.NOTIFICATION_DELETE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> readAllNotification(String token) {
        User user = getUser(token);
        List<Notification> notificationList = user.getNotifications();
        for(Notification notification : notificationList){
            notification.setIsRead(1);
        }
        notificationRepository.saveAll(notificationList);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.NOTIFICATION_READ_ALL));
    }

    private Pageable getPageable(Pageable pageable, int page, int limit, String order) {
        if (StringUtils.hasText(order)) {
            String[] orderParams = order.split(" ");
                if (orderParams.length == 2) {
                    Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                    pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(direction, orderParams[0])));
                }
        }
        return pageable;
    }

    private User getUser(String token){
        String jwtToken = token.substring(7);
        String email = jwtTokenUtil.extractEmail(jwtToken);
        Optional<User> optionalUser = authRepository.findByEmail(email);
        if(optionalUser.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
        return optionalUser.get();
    }
}

