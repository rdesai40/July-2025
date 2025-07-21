package com.rdesai.lpl.commentslistpoc.presentation.comments.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rdesai.lpl.commentslistpoc.data.model.Comment
import com.rdesai.lpl.commentslistpoc.data.repository.CommentsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommentsListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()
    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: CommentsListViewModel
    private val repository = mockk<CommentsRepository>()

    @Before
    fun setup() {
        viewModel = CommentsListViewModel(repository)
    }

    private val mockComments = listOf(
        Comment(
            postId = 1,
            id = 1,
            name = "Test Comment 1",
            email = "test1@example.com",
            body = "This is a test comment 1"
        ),
        Comment(
            postId = 1,
            id = 2,
            name = "Test Comment 2",
            email = "test2@example.com",
            body = "This is a test comment 2"
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CommentsListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test loadComments success`() = runTest {
        //setup
        coEvery { repository.getComments() } returns mockComments

        //Execute
        viewModel.handleAction(CommentsAction.LoadComments)
        testDispatcher.scheduler.advanceUntilIdle()

        //Verify
        val viewState = viewModel.viewState.value
        assert(viewState is CommentsViewState.Success)
        val successState = viewState as CommentsViewState.Success
        assert(successState.comments == mockComments)
        coVerify { repository.getComments() }
    }

    @Test
    fun `test loadComments failure`()  = runTest {
        //setup
        val errorMessage = "Failed to load comments"
        coEvery { repository.getComments() } throws Exception(errorMessage)
        //Execute
        viewModel.handleAction(CommentsAction.LoadComments)
        testDispatcher.scheduler.advanceUntilIdle()
        //Verify
        val viewState = viewModel.viewState.value
        assert(viewState is CommentsViewState.Error)
        val errorState = viewState as CommentsViewState.Error
        assert(errorState.message == errorMessage)
        coVerify { repository.getComments() }
    }

    @Test
    fun `test RequestImagePicker action event`() = runTest {
        // Setup
        val commentId = 1
        val events = mutableListOf<CommentsEvent>()
        val job = launch(unconfinedTestDispatcher) {
            viewModel.events.toList(events)
        }

        // Execute
        viewModel.handleAction(CommentsAction.RequestImagePicker(commentId))
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify
        job.cancel()
        assertTrue(events.any { it is CommentsEvent.OpenImagePicker && it.commentId == commentId })
    }

    @Test
    fun `test UpdateProfileImage action`() = runTest {
        //Setup
        val commentId = 1
        val imageUrl = "https://example.com/image.jpg"
        coEvery { repository.getComments() } returns mockComments
        viewModel.handleAction(CommentsAction.LoadComments)
        testDispatcher.scheduler.advanceUntilIdle()


        //Execute
        viewModel.handleAction(CommentsAction.UpdateProfileImage(commentId, imageUrl))
        testDispatcher.scheduler.advanceUntilIdle()

        //Verify
        val viewState = viewModel.viewState.value
        assert(viewState is CommentsViewState.Success)
        val successState = viewState as CommentsViewState.Success
        assert(successState.profileImages[commentId] == imageUrl)
    }

    @Test
    fun `test multiple profile image updates preserves all images`() = runTest {
        // Setup
        coEvery { repository.getComments() } returns mockComments
        viewModel.handleAction(CommentsAction.LoadComments)
        testDispatcher.scheduler.advanceUntilIdle()

        // Execute multiple updates
        viewModel.handleAction(CommentsAction.UpdateProfileImage(1, "url1"))
        viewModel.handleAction(CommentsAction.UpdateProfileImage(2, "url2"))
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify
        val viewState = viewModel.viewState.value
        assertTrue(viewState is CommentsViewState.Success)
        val successState = viewState as CommentsViewState.Success
        assertEquals(successState.profileImages[1], "url1")
        assertEquals(successState.profileImages[2], "url2")
        assertEquals(successState.profileImages.size, 2)

        //Update one more image
        viewModel.handleAction(CommentsAction.UpdateProfileImage(3, "url3"))
        testDispatcher.scheduler.advanceUntilIdle()
        // Verify 3rd image
        assertEquals((viewModel.viewState.value as CommentsViewState.Success).profileImages.size, 3)
    }



}