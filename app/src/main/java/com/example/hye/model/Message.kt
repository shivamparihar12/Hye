package com.example.hye.model

import java.net.IDN

class Message {
    private val messageID: String
        get() {
            return messageID
        }

    private var message: String
        get() {
            return message
        }
    private var timeStamp: Long
        get() {
            return timeStamp
        }
     var senderID:String
        get() {
            return senderID
        }

    constructor(senderID: String, message: String, timeStamp: Long) {
        this.senderID = senderID
        this.message = message
        this.timeStamp = timeStamp
    }

}