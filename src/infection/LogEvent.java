package infection;

import org.simpleframework.xml.Element;

public interface LogEvent {

	public long getTime();

	public class VertexInfectedEvent implements LogEvent, java.io.Serializable {
		private static final long serialVersionUID = 1L;

		@Element
		String name;

		@Element
		boolean infected;

		@Element
		long time;

		public VertexInfectedEvent(long time, String name) {
			super();
			this.time = time;
			this.name = name;
			this.infected = true;
		}

		public long getTime() {
			return time;
		}

		public VertexInfectedEvent() {
			super();
		}
	}

	public class VertexAddEvent implements LogEvent, java.io.Serializable {
		private static final long serialVersionUID = 1L;
		@Element
		String name;

		@Element
		boolean infected;

		@Element
		boolean secure;

		@Element
		long time;

		public VertexAddEvent(long time, String name, boolean infected,
				boolean secure) {
			super();
			this.time = time;
			this.name = name;
			this.infected = infected;
			this.secure = secure;
		}

		public long getTime() {
			return time;
		}

		public VertexAddEvent() {
			super();
		}
	}

	// public class VertexRemoveEvent implements LogEvent {
	// long time;
	// String name;
	//		
	// public VertexRemoveEvent(long time, String name) {
	// super();
	// this.time = time;
	// this.name = name;
	//		
	// }
	//		
	// public long getTime(){
	// return time;
	// }
	// }

	public class EdgeAddEvent implements LogEvent, java.io.Serializable {
		private static final long serialVersionUID = 1L;
		@Element
		long time;

		@Element
		String from;

		@Element
		String to;

		public EdgeAddEvent(long time, String from, String to) {
			super();
			this.time = time;
			this.from = from;
			this.to = to;
		}

		public long getTime() {
			return time;
		}

		public EdgeAddEvent() {
			super();
		}
	}

	// public class EdgeRemoveEvent implements LogEvent {
	// long time;
	// String from;
	// String to;
	//		
	// public EdgeRemoveEvent(long time, String from, String to) {
	// super();
	// this.time = time;
	// this.from = from;
	// this.to = to;
	// }
	//		
	// public long getTime(){
	// return time;
	//		}
	//	}	

}