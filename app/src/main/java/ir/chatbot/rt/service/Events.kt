package ir.chatbot.rt.service


object Events {

    var SocketConnectionReceiver = "SocketConnectionReceiver"
    var SocketConnectionFailureReceiver = "SocketConnectionFailureReceiver"

    /// receiver
    var onReady = "onReady"
    var onMessage = "onMessage"
    var onPendingMessages = "onPendingMessages"
    var onAuthFailure = "onAuthFailure"
    var onError = "onError"


    /// sender
    var eventMessageReceived = "eventMessageReceived"
    var eventPendingMessagesReceived = "eventPendingMessagesReceived"
    var eventAppAuth = "eventAppAuth"
    var eventAppNewMessage = "eventAppNewMessage"
    var eventRequestJoin = "eventRequestJoin"



    //// broadcast
    var onMessageReceiver = "onMessageReceiver"
    var onPendingMessagesReceiver = "onPendingMessagesReceiver"
    var onReadyReceiver = "onReadyReceiver"
    var onAuthFailureReceiver = "onAuthFailureReceiver"
    var onErrorReceiver = "onErrorReceiver"

}
