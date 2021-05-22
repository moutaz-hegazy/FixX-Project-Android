package com.example.fixx.POJOs

class ChatMessage(val text: String, val fromId: String, val timestamp: Long) {
  constructor() : this( "", "", -1)
}