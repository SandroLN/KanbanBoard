package com.github.sandroln.kanbanboard.board.settings.data

//todo
//import com.github.sandroln.chosenboard.ChosenBoardCache
//import com.github.sandroln.common.UserProfileCloud
//import com.github.sandroln.kanbanboard.BaseTest
//import com.github.sandroln.kanbanboard.board.main.data.BoardUser
//import com.github.sandroln.kanbanboard.board.main.data.MemberName
//import org.junit.Assert
//import org.junit.Before
//import org.junit.Test
//
//class BoardInvitationRepositoryTest : BaseTest() {
//
//    private lateinit var functionsCallsStack: FunctionsCallsStack
//    private lateinit var invitationsCloudDataSource: FakeInvitationsCloudDataSource
//    private lateinit var memberCloudDataSource: FakeMemberCloudDataSource
//    private lateinit var chosenBoardCache: FakeChosenBoardCache
//    private lateinit var repository: BoardInvitationRepository
//
//    @Before
//    fun setup() {
//        functionsCallsStack = FunctionsCallsStack.Base()
//        invitationsCloudDataSource = FakeInvitationsCloudDataSource.Base(functionsCallsStack)
//        memberCloudDataSource = FakeMemberCloudDataSource.Base(functionsCallsStack)
//        chosenBoardCache = FakeChosenBoardCache.Base()
//        repository = BoardInvitationRepository.Base(
//            invitationsCloudDataSource,
//            memberCloudDataSource,
//            chosenBoardCache
//        )
//    }
//
//    @Test
//    fun `invite one member`() {
//        val expectedUsersList = mutableListOf<BoardUser>()
//
//        var count = 0
//        val callback = object : BoardInvitationRepository.Callback {
//            override fun provideInvitations(users: List<BoardUser>) {
//                Assert.assertEquals(expectedUsersList, users)
//                when (count) {
//                    0 -> memberCloudDataSource.checkHandleCalled("1")
//                    1 -> functionsCallsStack.checkStack(2)
//                }
//                count++
//            }
//        }
//        repository.init(callback)
//        invitationsCloudDataSource.checkHandleCalledWith("fakeBoardId")
//
//        expectedUsersList.add(BoardUser.Base("1", "person1", "1@gmail.com"))
//        invitationsCloudDataSource.returnInvitations(listOf("1"))
//    }
//
//    @Test
//    fun `invite two members`() {
//        val expectedUsersList = mutableListOf<BoardUser>()
//
//        var count = 0
//        val callback = object : BoardInvitationRepository.Callback {
//            override fun provideInvitations(users: List<BoardUser>) {
//                Assert.assertEquals(expectedUsersList, users)
//                when (count++) {
//                    0 -> memberCloudDataSource.checkHandleCalled("1")
//                    1 -> {
//                        expectedUsersList.add(BoardUser.Base("2", "person2", "2@gmail.com"))
//                        invitationsCloudDataSource.returnInvitations(listOf("1", "2"))
//                    }
//
//                    2 -> memberCloudDataSource.checkHandleCalled("2")
//                    3 -> functionsCallsStack.checkStack(3)
//                }
//            }
//        }
//        repository.init(callback)
//        invitationsCloudDataSource.checkHandleCalledWith("fakeBoardId")
//
//        expectedUsersList.add(BoardUser.Base("1", "person1", "1@gmail.com"))
//        invitationsCloudDataSource.returnInvitations(listOf("1"))
//    }
//
//    @Test
//    fun `invite two members then one member accepted invitation`() {
//        val expectedUsersList = mutableListOf<BoardUser>()
//
//        var count = 0
//        val callback = object : BoardInvitationRepository.Callback {
//            override fun provideInvitations(users: List<BoardUser>) {
//                Assert.assertEquals(expectedUsersList, users)
//                when (count++) {
//                    0 -> memberCloudDataSource.checkHandleCalled("1")
//                    1 -> {
//                        expectedUsersList.add(BoardUser.Base("2", "person2", "2@gmail.com"))
//                        invitationsCloudDataSource.returnInvitations(listOf("1", "2"))
//                    }
//
//                    2 -> memberCloudDataSource.checkHandleCalled("2")
//
//                    3 -> {
//                        expectedUsersList.removeFirst()
//                        invitationsCloudDataSource.returnInvitations(listOf("2"))
//                    }
//
//                    4 -> functionsCallsStack.checkStack(3)
//                }
//            }
//        }
//        repository.init(callback)
//        invitationsCloudDataSource.checkHandleCalledWith("fakeBoardId")
//
//        expectedUsersList.add(BoardUser.Base("1", "person1", "1@gmail.com"))
//        invitationsCloudDataSource.returnInvitations(listOf("1"))
//    }
//
//    private interface FakeInvitationsCloudDataSource : Invitations.CloudDataSource.Mutable {
//
//        fun returnInvitations(membersList: List<String>)
//
//        fun checkHandleCalledWith(boardId: String)
//
//        class Base(private val functionsCallsStack: FunctionsCallsStack) :
//            FakeInvitationsCloudDataSource {
//
//            private var callback: Invitations.Callback = Invitations.Callback.Empty
//
//            override fun init(callback: Invitations.Callback) {
//                this.callback = callback
//            }
//
//            private val handleCalledList = mutableListOf<String>()
//            private var handleCalledIndex = 0
//
//            override fun handle(boardId: String) {
//                functionsCallsStack.put(HANDLE_CALLED)
//                handleCalledList.add(boardId)
//            }
//
//            override fun checkHandleCalledWith(boardId: String) {
//                Assert.assertEquals(boardId, handleCalledList[handleCalledIndex++])
//                functionsCallsStack.checkCalled(HANDLE_CALLED)
//            }
//
//            override fun returnInvitations(membersList: List<String>) {
//                callback.provideInvitations(membersList)
//            }
//
//            companion object {
//                private const val HANDLE_CALLED = "Invitations.CloudDataSource.Mutable#handle"
//            }
//        }
//    }
//
//    private interface FakeChosenBoardCache : ChosenBoardCache.Read {
//
//        class Base : FakeChosenBoardCache {
//
//            override fun read() = BoardInfo("fakeBoardId", "fakeBoardName", true)
//        }
//    }
//
//    private interface FakeMemberCloudDataSource : MemberName.CloudDataSource {
//
//        fun checkHandleCalled(arg: String)
//
//        class Base(
//            private val functionsCallsStack: FunctionsCallsStack
//        ) : FakeMemberCloudDataSource {
//
//            private var callback: MemberName.Callback = MemberName.Callback.Empty
//
//            override fun init(callback: MemberName.Callback) {
//                this.callback = callback
//            }
//
//            private val list = mutableListOf<String>()
//            private var index = 0
//
//            override fun checkHandleCalled(arg: String) {
//                Assert.assertEquals(arg, list[index++])
//                functionsCallsStack.checkCalled(HANDLE_CALL)
//            }
//
//            override fun handle(memberId: String) {
//                list.add(memberId)
//                functionsCallsStack.put(HANDLE_CALL)
//                callback.provideMember(
//                    UserProfileCloud(
//                        "$memberId@gmail.com",
//                        "person$memberId"
//                    ),
//                    memberId
//                )
//            }
//
//            companion object {
//                private const val HANDLE_CALL = "MemberName.CloudDataSource#handle"
//            }
//        }
//    }
//}