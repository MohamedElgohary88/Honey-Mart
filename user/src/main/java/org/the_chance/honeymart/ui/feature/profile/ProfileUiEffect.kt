package org.the_chance.honeymart.ui.feature.profile

import org.the_chance.honeymart.ui.base.BaseUiEffect

sealed interface ProfileUiEffect: BaseUiEffect {
    object ClickMyOrderEffect : ProfileUiEffect
    object ClickCouponsEffect : ProfileUiEffect
    object ClickNotificationEffect : ProfileUiEffect
    object ClickLogoutEffect : ProfileUiEffect
    object UnAuthorizedUserEffect : ProfileUiEffect
    object ClickCameraEffect : ProfileUiEffect

}