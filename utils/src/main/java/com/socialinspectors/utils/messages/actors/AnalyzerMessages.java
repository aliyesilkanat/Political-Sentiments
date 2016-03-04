package com.socialinspectors.utils.messages.actors;

public enum AnalyzerMessages {
	ANALYZE {
		@Override
		public String toString() {
			return "ANALYZE";
		}
	},

	STORE {
		@Override
		public String toString() {
			return "STORE";
		}
	},
	EXTRACT_SENTIMENT {
		@Override
		public String toString() {
			return "EXTRACT_SENTIMENT";
		}
	}

}
