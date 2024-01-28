package com.andreikslpv.flickrrecent.domain.models

enum class NotificationStatus {
    /**
     * Уведомления включены и можно их показывать
     */
    NOTIFICATION_ENABLED_AND_SHOWING,

    /**
     * Уведомления включены, но их нельзя показывать, т.к. приложение показывается на экране.
     * Кроме того, надо поменять картинку
     */
    NOTIFICATION_ENABLED_AND_NOT_SHOWING,

    /**
     * Уведомления выключены
     */
    NOTIFICATION_DISABLED,
}