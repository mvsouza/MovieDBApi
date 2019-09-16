package com.gabrielbmoro.programmingchallenge.ui.mainScreen.page

import android.app.Application
import com.gabrielbmoro.programmingchallenge.dependenciesInjector.RepositoryImpl
import com.gabrielbmoro.programmingchallenge.model.MoviesListType
import com.gabrielbmoro.programmingchallenge.ui.base.BaseViewModel
import com.gabrielbmoro.programmingchallenge.ui.mainScreen.page.adapter.MoviesListAdapter
import org.koin.core.inject
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MovieListViewModel(application: Application) : BaseViewModel(application) {

    val adapter = MoviesListAdapter()
    private val api: RepositoryImpl by inject()
    private var type: MoviesListType? = null

    fun setup(listType: MoviesListType) {
        type = listType
        requestForMovies()
    }

    private fun requestForMovies() {
        type?.let { t ->
            when (t) {
                MoviesListType.TOP_RATED_MOVIES, MoviesListType.POPULAR_RATED_MOVIES -> {
                    isLoading = true
                    api.getMovies(t.requestParameter)
                            .doAfterTerminate {
                                isLoading = false
                            }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        adapter.setup(it.results)
                                    },
                                    {

                                    }
                            )
                }
                MoviesListType.FAVORITE_MOVIES -> {

                }
            }
        }
    }

    fun reload() {
        requestForMovies()
    }

}