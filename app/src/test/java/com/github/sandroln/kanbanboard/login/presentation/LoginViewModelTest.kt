package com.github.sandroln.kanbanboard.login.presentation

import com.github.sandroln.kanbanboard.boards.presentation.BoardsScreen
import com.github.sandroln.kanbanboard.login.BaseTest
import com.github.sandroln.kanbanboard.login.data.LoginRepository
import com.github.sandroln.kanbanboard.login.data.LoginResult
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.main.Screen
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class LoginViewModelTest : BaseTest() {

    //region fields
    private lateinit var functionsCallsStack: FunctionsCallsStack
    private lateinit var dispatchersList: TestDispatchersList
    private lateinit var repository: FakeRepository
    private lateinit var manageResource: TestManageResource
    private lateinit var communication: FakeCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var viewModel: LoginViewModel
    //endregion

    @Before
    fun setUp() {
        functionsCallsStack = FunctionsCallsStack.Base()
        dispatchersList = TestDispatchersList()
        repository = FakeRepository.Base(functionsCallsStack)
        manageResource = TestManageResource("stub")
        communication = FakeCommunication.Base(functionsCallsStack)
        navigation = FakeNavigation.Base(functionsCallsStack)
        viewModel =
            LoginViewModel(repository, dispatchersList, manageResource, communication, navigation)
    }

    @Test
    fun `test user not logged in`() {
        repository.initUserLoggedIn(false)

        viewModel.init(true)
        repository.checkUserNotLoggedInCall()
        communication.check(LoginUiState.Initial)

        functionsCallsStack.checkStack(2)
    }

    @Test
    fun `test user logged in`() {
        repository.initUserLoggedIn(true)

        viewModel.init(true)
        repository.checkUserNotLoggedInCall()
        communication.check(LoginUiState.Auth(manageResource))

        functionsCallsStack.checkStack(2)
    }

    @Test
    fun `test login successful`() {
        viewModel.login()
        communication.check(LoginUiState.Auth(manageResource))

        viewModel.handleResult(Successful)
        repository.checkHandleResultCall(Successful)
        navigation.check(BoardsScreen)

        functionsCallsStack.checkStack(3)
    }

    @Test
    fun `test login failed`() {
        viewModel.login()
        communication.check(LoginUiState.Auth(manageResource))

        viewModel.handleResult(Failed)
        repository.checkHandleResultCall(Failed)
        communication.check(LoginUiState.Error("stub error"))
        functionsCallsStack.checkStack(3)
    }

    @Test
    fun `test second opening`() {
        viewModel.init(false)
        functionsCallsStack.checkStack(0)
    }

    private object Successful : AuthResultWrapper {
        override fun isSuccessful() = true
        override fun task() = throw IllegalStateException("not used in test")
    }

    private object Failed : AuthResultWrapper {
        override fun isSuccessful() = false
        override fun task() = throw IllegalStateException("not used in test")
    }

    private interface FakeRepository : LoginRepository {

        fun checkHandleResultCall(authResult: AuthResultWrapper)
        fun checkUserNotLoggedInCall()
        fun initUserLoggedIn(userLoggedIn: Boolean)

        class Base(private val functionsCallsStack: FunctionsCallsStack) : FakeRepository {

            private var userLoggedIn: Boolean = false
            private var authResultList = mutableListOf<AuthResultWrapper>()
            private var authResultListIndex = 0

            override fun initUserLoggedIn(userLoggedIn: Boolean) {
                this.userLoggedIn = userLoggedIn
            }

            override fun checkUserNotLoggedInCall() {
                functionsCallsStack.checkCalled(USER_NOT_LOGGED_IN_CALL)
            }

            override fun userNotLoggedIn(): Boolean {
                functionsCallsStack.put(USER_NOT_LOGGED_IN_CALL)
                return !userLoggedIn
            }

            override fun checkHandleResultCall(authResult: AuthResultWrapper) {
                assertEquals(authResult, authResultList[authResultListIndex++])
                functionsCallsStack.checkCalled(HANDLE_RESULT_CALL)
            }

            override suspend fun handleResult(authResult: AuthResultWrapper): LoginResult {
                authResultList.add(authResult)
                functionsCallsStack.put(HANDLE_RESULT_CALL)
                return if (authResult.isSuccessful())
                    LoginResult.Success
                else
                    LoginResult.Failed("stub error")
            }

            companion object {
                private const val USER_NOT_LOGGED_IN_CALL = "LoginRepository#userNotLoggedIn"
                private const val HANDLE_RESULT_CALL = "LoginRepository#handleResult"
            }
        }
    }

    private interface FakeCommunication : LoginCommunication {

        fun check(state: LoginUiState)

        class Base(private val functionsCallsStack: FunctionsCallsStack) : FakeCommunication {

            private val list = mutableListOf<LoginUiState>()
            private var index = 0

            override fun map(source: LoginUiState) {
                functionsCallsStack.put(MAP_CALL)
                list.add(source)
            }

            override fun check(state: LoginUiState) {
                assertEquals(state, list[index++])
                functionsCallsStack.checkCalled(MAP_CALL)
            }

            companion object {
                private const val MAP_CALL = "LoginCommunication#map"
            }
        }
    }

    private interface FakeNavigation : NavigationCommunication.Update {

        fun check(screen: Screen)

        class Base(private val functionsCallsStack: FunctionsCallsStack) : FakeNavigation {

            private val list = mutableListOf<Screen>()
            private var index = 0

            override fun check(screen: Screen) {
                assertEquals(screen, list[index++])
                functionsCallsStack.checkCalled(MAP_CALL)
            }

            override fun map(source: Screen) {
                functionsCallsStack.put(MAP_CALL)
                list.add(source)
            }

            companion object {
                private const val MAP_CALL = "NavigationCommunication.Update#map"
            }
        }
    }
}