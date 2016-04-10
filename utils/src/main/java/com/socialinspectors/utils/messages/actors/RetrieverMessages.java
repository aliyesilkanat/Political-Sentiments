package com.socialinspectors.utils.messages.actors;

public enum RetrieverMessages {
	EXTRACT {
		@Override
		public String toString() {
			return "EXTRACT";
		}
	},
	START {
		@Override
		public String toString() {
			return "START";
		}
	},
	FETCH {
		@Override
		public String toString() {
			return "FETCH";
		}

	}
}
