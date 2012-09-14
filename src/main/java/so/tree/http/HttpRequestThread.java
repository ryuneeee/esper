package so.tree.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class HttpRequestThread extends Thread {

	String method;
	String mAddr;
	HashMap<String, String> requestPropertyMap;

	public HttpRequestThread(String method, String addr) {
		this.method = method;
		this.mAddr = addr;
	}

	public HttpRequestThread(String method, String addr,
			HashMap<String, String> requestPropertyMap) {
		this.method = method;
		this.mAddr = addr;
		this.requestPropertyMap = requestPropertyMap;
	}

	public void run() {

		StringBuilder html = new StringBuilder();

		try {
			URL url = new URL(mAddr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn != null) {
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				conn.setUseCaches(false);
				conn.setRequestMethod(method);

				// Set property
				if (method.equals("POST") || method.equals("PUT")) {
					conn.setRequestProperty("Content-Type", "application/json");
					JSONObject jsonObject = new JSONObject();
					for (Map.Entry<String, String> entry : requestPropertyMap
							.entrySet()) {
						if (entry.getKey().equals("keyString"))
							conn.setRequestProperty(entry.getKey(),
									entry.getValue());
						else
							jsonObject.put(entry.getKey(), entry.getValue());
					}

					// Send Request
					OutputStreamWriter sw = new OutputStreamWriter(
							conn.getOutputStream());
					sw.write(jsonObject.toString());
					sw.flush();
					sw.close();

				} else if (method.equals("DELETE")) {
					for (Map.Entry<String, String> entry : requestPropertyMap
							.entrySet())
						conn.setRequestProperty(entry.getKey(),
								entry.getValue());
				}

				switch (conn.getResponseCode()) {

				case HttpURLConnection.HTTP_BAD_REQUEST:

					break;
				case HttpURLConnection.HTTP_OK:
					BufferedReader br = new BufferedReader(
							new InputStreamReader(conn.getInputStream(),
									"UTF-8"));
					for (;;) {
						String line = br.readLine();
						if (line == null)
							break;
						html.append(line + '\n');
					}
					br.close();
					break;
				}
			} else {
				conn.getResponseCode();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}
}