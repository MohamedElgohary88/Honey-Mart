package org.the_chance.honeymart.ui.feature.coupons

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import org.the_chance.honeymart.domain.model.CouponEntity
import org.the_chance.honeymart.domain.usecase.ClipCouponUseCase
import org.the_chance.honeymart.domain.usecase.GetClippedUserCouponsUseCase
import org.the_chance.honeymart.domain.util.ErrorHandler
import org.the_chance.honeymart.ui.base.BaseViewModel
import org.the_chance.honeymart.ui.feature.home.toCouponUiState
import javax.inject.Inject

@HiltViewModel
class CouponsViewModel @Inject constructor(
    private val getClippedUserCouponsUseCase: GetClippedUserCouponsUseCase,
    private val clipCouponsUseCase: ClipCouponUseCase,
) : BaseViewModel<CouponsUiState, CouponsUiEffect>(CouponsUiState()),
    CouponsInteractionListener {

    override val TAG: String = this::class.java.simpleName

    override fun getData() {
        _state.update {
            it.copy(isLoading = true, error = null, isError = false)
        }
        tryToExecute(
            { getClippedUserCouponsUseCase() },
            ::onGetClippedUserCouponsSuccess,
            ::onGetClippedUserCouponsError
        )
    }

    private fun onGetClippedUserCouponsSuccess(coupons: List<CouponEntity>) {
        _state.update {
            it.copy(
                isLoading = false,
                coupons = coupons.map { coupon -> coupon.toCouponUiState() },
                updatedCoupons = coupons.map { coupon -> coupon.toCouponUiState() }
            )
        }
    }

    private fun onGetClippedUserCouponsError(error: ErrorHandler) {
        _state.update {
            it.copy(
                isLoading = false,
                error = error,
                isError = error is ErrorHandler.NoConnection
            )
        }
    }


    override fun onClickGetCoupon(couponId: Long) {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            { clipCouponsUseCase(couponId) },
            { onClipCouponSuccess() },
            ::onClipCouponError
        )
    }

    private fun onClipCouponSuccess() {
        _state.update { it.copy(isLoading = false) }
    }

    private fun onClipCouponError(errorHandler: ErrorHandler) {
        _state.update {
            it.copy(
                isLoading = false,
                error = errorHandler,
                isError = errorHandler is ErrorHandler.NoConnection,
            )
        }
    }


    override fun onClickAllCoupons() {
        _state.update {
            it.copy(
                couponsState = CouponsState.ALL,
                updatedCoupons = it.coupons
            )
        }
    }

    override fun onClickValidCoupons() {
        _state.update {
            it.copy(
                couponsState = CouponsState.VALID,
                updatedCoupons = it.coupons.filter { coupon ->
                    !coupon.isExpired
                }
            )
        }
    }

    override fun onClickExpiredCoupons() {
        _state.update {
            it.copy(couponsState = CouponsState.EXPIRED,
                updatedCoupons = it.coupons.filter { coupon ->
                    coupon.isExpired
                }
            )
        }
    }
}