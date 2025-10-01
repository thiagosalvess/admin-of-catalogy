package com.thiagosalvess.admin.catalogy.domain.exceptions;

import com.thiagosalvess.admin.catalogy.domain.validation.handler.Notification;

public class NotificationException extends DomainException {
    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
}
