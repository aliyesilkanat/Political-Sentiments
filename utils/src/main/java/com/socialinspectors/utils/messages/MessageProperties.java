package com.socialinspectors.utils.messages;

public enum MessageProperties {
	HEADER {
		@Override
		public String toString() {
			return "header";
		}
	},
	BODY {
		@Override
		public String toString() {
			return "body";
		}
	}
}
