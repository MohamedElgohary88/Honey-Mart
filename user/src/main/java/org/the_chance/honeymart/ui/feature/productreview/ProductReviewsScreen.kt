package org.the_chance.honeymart.ui.feature.productreview

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.the_chance.design_system.R
import org.the_chance.honeymart.ui.LocalNavigationProvider
import org.the_chance.honeymart.ui.composables.ContentVisibility
import org.the_chance.honeymart.ui.composables.PagingLoading
import org.the_chance.honeymart.ui.feature.productreview.ProductReviewsViewModel.Companion.MAX_PAGE_SIZE
import org.the_chance.honeymart.util.defaultTo1IfZero
import org.the_chance.honymart.ui.composables.AverageRating
import org.the_chance.honymart.ui.composables.CardReviews
import org.the_chance.honymart.ui.composables.IconButton
import org.the_chance.honymart.ui.composables.Loading
import org.the_chance.honymart.ui.composables.ReviewsProgressBar
import org.the_chance.honymart.ui.theme.dimens

@Composable
fun ProductReviewsScreen(
    viewModel: ProductReviewsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavigationProvider.current

    LaunchedEffect(key1 = true) {
        viewModel.effect.collect {
            when (it) {
                ProductReviewsUiEffect.OnBackClickEffect -> navController.navigateUp()
            }
        }
    }
    Loading(state = state.isLoading && state.reviews.reviews.isEmpty())
    ContentVisibility(state = state.reviews.reviews.isNotEmpty()) {
        ProductReviewsContent(
            state = state,
            listener = viewModel,
            onChangeReviews = viewModel::onChangeReviews
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductReviewsContent(
    state: ProductReviewsUiState,
    listener: ProductReviewsInteractionsListener,
    onChangeReviews: (Int) -> Unit
) {
    val reviews = state.reviews
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.space16),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.customers_reviews),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = listener::onClickBack,
                        backgroundColor = Color.Transparent
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_arrow_back),
                            contentDescription = "icon back",
                            tint = MaterialTheme.colorScheme.onSecondary,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(top = paddingValues.calculateTopPadding())
        ) {
            item {
                AnimatedVisibility(
                    visible = state.reviews.reviews.isNotEmpty(),
                    enter = fadeIn(animationSpec = tween(durationMillis = 2000)) + slideInVertically(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutHorizontally()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        AverageRating(
                            averageRating = state.reviews.reviewStatisticUiState.averageRating.toFloat(),
                            reviewCount = state.reviews.reviewStatisticUiState.reviewCount.toString(),
                        )

                        ReviewsProgressBar(
                            starNumber = "5",
                            countReview = state.reviews.reviewStatisticUiState.fiveStarsCount.toString(),
                            rating =
                            (state.reviews.reviewStatisticUiState.fiveStarsCount.toFloat() /
                                    state.reviews.reviewStatisticUiState.reviewCount.defaultTo1IfZero()
                                    )
                        )

                        ReviewsProgressBar(
                            starNumber = "4",
                            countReview = state.reviews.reviewStatisticUiState.fourStarsCount.toString(),
                            rating =
                            (state.reviews.reviewStatisticUiState.fourStarsCount.toFloat() /
                                    state.reviews.reviewStatisticUiState.reviewCount
                                        .defaultTo1IfZero())
                        )
                        ReviewsProgressBar(
                            starNumber = "3",
                            countReview = state.reviews.reviewStatisticUiState.threeStarsCount.toString(),
                            rating = (state.reviews.reviewStatisticUiState.threeStarsCount.toFloat() /
                                    state.reviews.reviewStatisticUiState.reviewCount
                                        .defaultTo1IfZero())
                        )
                        ReviewsProgressBar(
                            starNumber = "2",
                            countReview = state.reviews.reviewStatisticUiState.twoStarsCount.toString(),
                            rating = (state.reviews.reviewStatisticUiState.twoStarsCount.toFloat() /
                                    state.reviews.reviewStatisticUiState.reviewCount
                                        .defaultTo1IfZero())
                        )
                        ReviewsProgressBar(
                            starNumber = "1",
                            countReview = state.reviews.reviewStatisticUiState.oneStarCount.toString(),
                            rating = (state.reviews.reviewStatisticUiState.oneStarCount.toFloat() /
                                    state.reviews.reviewStatisticUiState.reviewCount
                                        .defaultTo1IfZero())
                        )
                    }
                }
            }
            items(state.reviews.reviews.size) { position ->
                onChangeReviews(position)
                if ((position + 1) >= (state.page * MAX_PAGE_SIZE)) {
                    listener.onScrollDown()
                }
                val review = reviews.reviews[position]
                CardReviews(
                    userName = review.fullName,
                    rating = review.rating.toFloat(),
                    reviews = review.content,
                    date = review.reviewDate
                )
            }
            item {
                PagingLoading(state = state.isLoading && state.reviews.reviews.isNotEmpty())
            }
        }
    }
}