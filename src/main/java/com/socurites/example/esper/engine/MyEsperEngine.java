package com.socurites.example.esper.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.socurites.example.esper.event.CommentLog;
import com.socurites.example.esper.listener.MyListener;

public class MyEsperEngine {
	public static void main(String[] args) throws InterruptedException {
		// <Setting a Configuration>
		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.socurites.example.esper.event");
		// </Setting a Configuration>

		// <Creating a Statement>
		final EPServiceProvider epService = EPServiceProviderManager
				.getDefaultProvider(config);
		String epl = "select * from CommentLog.win:time(1 min) having count(*) >= 5";
		EPStatement statement = epService.getEPAdministrator().createEPL(epl);
		// </Creating a Statement>

		// <Adding a Listener>
		MyListener listener = new MyListener();
		statement.addListener(listener);
		// </Adding a Listener>

		// <Sending events>

		try {

			ServerSocket server = new ServerSocket(51515);
			System.out.println("Wating Connect ..");

			Socket sock = server.accept();

			InetAddress inetaddr = sock.getInetAddress();
			System.out.println(inetaddr.getHostAddress() + " Connecting");

			OutputStream out = sock.getOutputStream();
			InputStream in = sock.getInputStream();

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println("Received Message : " + line);
				pw.println(line);

				if (line.contains("comment;;;add") == true) {
					String[] logSplit = line.split(";;;");
					epService.getEPRuntime().sendEvent(
							new CommentLog(logSplit[5], logSplit[8], logSplit[9]));
				}
				
				pw.flush();
			}

			pw.close();
			br.close();
			sock.close();

		} catch (Exception e) {
			System.out.println(e);
		}


	}
}