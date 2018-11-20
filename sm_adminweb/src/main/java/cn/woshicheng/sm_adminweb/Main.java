package cn.woshicheng.sm_adminweb;

public class Main {
	public static void main(String... anArgs) throws Exception {	
		new Main().start();
	}

	private JettyServer server;

	public Main() {
		server = new JettyServer(8080);
	}

	public void start() throws Exception {
		server.start();
		server.join();
	}
}
