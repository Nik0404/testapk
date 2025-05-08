package com.acroninspector.app.presentation.fragment.comment

import com.acroninspector.app.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CommentPresenterTest {

    private lateinit var presenter: CommentPresenter

    @Mock
    lateinit var viewState: `CommentView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = CommentPresenter()
        presenter.setViewState(viewState)
    }

    @Test
    fun onApplyClicked_IncorrectCommentLength() {
        val comment =
            "commentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcomment"
        presenter.onApplyClicked(comment)

        verify(viewState).showSnackbar(R.string.length_too_long)
    }

    @Test
    fun onApplyClicked_EmptyComment() {
        val comment = ""
        presenter.onApplyClicked(comment)

        verify(viewState).passComment("")
    }

    @Test
    fun onApplyClicked_SpaceComment() {
        val comment = "       "
        presenter.onApplyClicked(comment)

        verify(viewState).passComment("")
    }

    @Test
    fun onApplyClicked_CorrectComment() {
        val comment = "test comment"
        presenter.onApplyClicked(comment)

        verify(viewState).passComment(comment)
    }
}