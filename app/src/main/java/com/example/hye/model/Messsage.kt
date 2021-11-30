package com.example.hye.model

class Messsage {
    private var messageID: String
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

    constructor(messageID: String, message: String, timeStamp: Long) {
        this.messageID = messageID
        this.message = message
        this.timeStamp = timeStamp
    }

}