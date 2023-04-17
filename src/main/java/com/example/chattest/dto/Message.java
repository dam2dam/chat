package com.example.chattest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Message {

	private String type;
	private String sender;
	private String receiver;
	private Object data;

	public void newConnect() {
		this.type = "new";
	}

	public void closeConnect() {
		this.type = "close";
	}
}
