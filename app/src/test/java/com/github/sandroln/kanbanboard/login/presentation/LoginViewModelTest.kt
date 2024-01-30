package com.github.sandroln.kanbanboard.login.presentation

/**
 * todo move to login module
 */
//class LoginViewModelTest : BaseTest() {
//
//    //region fields
//    private lateinit var functionsCallsStack: FunctionsCallsStack
//    private lateinit var dispatchersList: TestDispatchersList
//    private lateinit var repository: FakeRepository
//    private lateinit var manageResource: TestManageResource
//    private lateinit var communication: FakeCommunication
//    private lateinit var navigation: FakeNavigation
//    private lateinit var viewModel: com.github.sandroln.login.presentation.LoginViewModel
//    //endregion
//
//    @Before
//    fun setUp() {
//        functionsCallsStack = FunctionsCallsStack.Base()
//        dispatchersList = TestDispatchersList()
//        repository = FakeRepository.Base(functionsCallsStack)
//        manageResource = TestManageResource("stub")
//        communication = FakeCommunication.Base(functionsCallsStack)
//        navigation = FakeNavigation.Base(functionsCallsStack)
//        viewModel =
//            com.github.sandroln.login.presentation.LoginViewModel(
//                repository,
//                dispatchersList,
//                manageResource,
//                communication,
//                navigation
//            )
//    }
//
//    @Test
//    fun `test user not logged in`() {
//        repository.initUserLoggedIn(false)
//
//        viewModel.init(true)
//        repository.checkUserNotLoggedInCall()
//        communication.check(com.github.sandroln.login.presentation.LoginUiState.Initial)
//
//        functionsCallsStack.checkStack(2)
//    }
//
//    @Test
//    fun `test user logged in`() {
//        repository.initUserLoggedIn(true)
//
//        viewModel.init(true)
//        repository.checkUserNotLoggedInCall()
//        communication.check(com.github.sandroln.login.presentation.LoginUiState.Auth(manageResource))
//
//        functionsCallsStack.checkStack(2)
//    }
//
//    @Test
//    fun `test login successful`() {
//        viewModel.login()
//        communication.check(com.github.sandroln.login.presentation.LoginUiState.Auth(manageResource))
//
//        viewModel.handleResult(Successful)
//        repository.checkHandleResultCall(Successful)
//        navigation.check(BoardsScreen)
//
//        functionsCallsStack.checkStack(3)
//    }
//
//    @Test
//    fun `test login failed`() {
//        viewModel.login()
//        communication.check(com.github.sandroln.login.presentation.LoginUiState.Auth(manageResource))
//
//        viewModel.handleResult(Failed)
//        repository.checkHandleResultCall(Failed)
//        communication.check(com.github.sandroln.login.presentation.LoginUiState.Error("stub error"))
//        functionsCallsStack.checkStack(3)
//    }
//
//    @Test
//    fun `test second opening`() {
//        viewModel.init(false)
//        functionsCallsStack.checkStack(0)
//    }
//
//    private object Successful : com.github.sandroln.login.presentation.AuthResultWrapper {
//        override fun isSuccessful() = true
//        override fun task() = throw IllegalStateException("not used in test")
//    }
//
//    private object Failed : com.github.sandroln.login.presentation.AuthResultWrapper {
//        override fun isSuccessful() = false
//        override fun task() = throw IllegalStateException("not used in test")
//    }
//
//    private interface FakeRepository : com.github.sandroln.login.data.LoginRepository {
//
//        fun checkHandleResultCall(authResult: com.github.sandroln.login.presentation.AuthResultWrapper)
//        fun checkUserNotLoggedInCall()
//        fun initUserLoggedIn(userLoggedIn: Boolean)
//
//        class Base(private val functionsCallsStack: FunctionsCallsStack) : FakeRepository {
//
//            private var userLoggedIn: Boolean = false
//            private var authResultList = mutableListOf<com.github.sandroln.login.presentation.AuthResultWrapper>()
//            private var authResultListIndex = 0
//
//            override fun initUserLoggedIn(userLoggedIn: Boolean) {
//                this.userLoggedIn = userLoggedIn
//            }
//
//            override fun checkUserNotLoggedInCall() {
//                functionsCallsStack.checkCalled(USER_NOT_LOGGED_IN_CALL)
//            }
//
//            override fun userNotLoggedIn(): Boolean {
//                functionsCallsStack.put(USER_NOT_LOGGED_IN_CALL)
//                return !userLoggedIn
//            }
//
//            override fun checkHandleResultCall(authResult: com.github.sandroln.login.presentation.AuthResultWrapper) {
//                assertEquals(authResult, authResultList[authResultListIndex++])
//                functionsCallsStack.checkCalled(HANDLE_RESULT_CALL)
//            }
//
//            override suspend fun handleResult(authResult: com.github.sandroln.login.presentation.AuthResultWrapper): com.github.sandroln.login.data.LoginResult {
//                authResultList.add(authResult)
//                functionsCallsStack.put(HANDLE_RESULT_CALL)
//                return if (authResult.isSuccessful())
//                    com.github.sandroln.login.data.LoginResult.Success
//                else
//                    com.github.sandroln.login.data.LoginResult.Failed("stub error")
//            }
//
//            companion object {
//                private const val USER_NOT_LOGGED_IN_CALL = "LoginRepository#userNotLoggedIn"
//                private const val HANDLE_RESULT_CALL = "LoginRepository#handleResult"
//            }
//        }
//    }
//
//    private interface FakeCommunication :
//        com.github.sandroln.login.presentation.LoginCommunication {
//
//        fun check(state: com.github.sandroln.login.presentation.LoginUiState)
//
//        class Base(private val functionsCallsStack: FunctionsCallsStack) : FakeCommunication {
//
//            private val list = mutableListOf<com.github.sandroln.login.presentation.LoginUiState>()
//            private var index = 0
//
//            override fun map(source: com.github.sandroln.login.presentation.LoginUiState) {
//                functionsCallsStack.put(MAP_CALL)
//                list.add(source)
//            }
//
//            override fun check(state: com.github.sandroln.login.presentation.LoginUiState) {
//                assertEquals(state, list[index++])
//                functionsCallsStack.checkCalled(MAP_CALL)
//            }
//
//            companion object {
//                private const val MAP_CALL = "LoginCommunication#map"
//            }
//        }
//    }
//}