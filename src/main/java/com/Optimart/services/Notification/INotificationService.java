package com.Optimart.services.Notification;

import com.Optimart.models.Notification;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface INotificationService {
    PagingResponse<List<Notification>> getNotifications(Map<Object, String> filters);
    APIResponse<Notification> markReadNotification(String notificationId);
    APIResponse<Boolean> deleteNotification(String notificationId);
    APIResponse<Boolean> readAllNotification(String token);
}
