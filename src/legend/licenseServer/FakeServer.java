package legend.licenseServer;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

public class FakeServer extends ServerSocket {

	public FakeServer(String serverAddress, int serverPort) throws IOException {
		// 用指定的端口构造一个ServerSocket
		super(serverPort, 50, InetAddress.getByName(serverAddress));
		System.out.println(String.format("Server Listen %s:%s",
				this.getInetAddress(), this.getLocalPort()));
		// new Logger();

		try {
			while (true) {
				// 监听一端口，等待客户接入
				Socket socket = accept();
				// 将会话交给线程处理
				new ServerThread(socket);
			}
		} catch (BindException e) {
			e.printStackTrace();
		} finally {
			close(); // 关闭监听端口
		}
	}

	// inner-class ServerThread
	class ServerThread extends Thread {
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		// Ready to conversation
		public ServerThread(Socket socket) throws IOException {
			this.socket = socket;
			// 构造该会话中的输入输出流
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), false);
			start();

			System.out.println(String.format("Client at %s:%s",
					socket.getInetAddress(), socket.getPort()));
		}

		public void obtainTicket(Properties parameters, PrintWriter response) {
			// secure false
			// hostName 192.168.0.104
			// clientVersion 3
			// buildDate 20151102
			// salt 1451191810341
			// version 20151102
			// userName legend
			// machineId 9651860e-549b-4852-8aed-b81aaca8e383
			// productCode 342e66b2-956c-4384-81da-f50365b990e9
			// productFamilyId 342e66b2-956c-4384-81da-f50365b990e9
			// //rpc/obtainTicket.action?buildDate=20151102&clientVersion=3&hostName=192.168.0.104&machineId=9651860e-549b-4852-8aed-b81aaca8e383&productCode=342e66b2-956c-4384-81da-f50365b990e9&productFamilyId=342e66b2-956c-4384-81da-f50365b990e9&salt=1451191810341&secure=false&userName=legend&version=20151102&versionNumber=9000
			// HTTP/1.1

			response.println("HTTP/1.1 200 OK");
			response.println();

			String content = String
					.format("<ObtainTicketResponse><message></message><prolongationPeriod>607875500</prolongationPeriod><responseCode>OK</responseCode><salt>%s</salt><ticketId>1</ticketId><ticketProperties>licensee=%s	licenseType=0	</ticketProperties></ObtainTicketResponse>",
							parameters.getProperty("salt"),
							parameters.getProperty("userName"));

			response.print("<!-- "
					+ LicenseService.generateSignature(content) + " -->\n");
			response.print(content);
			response.flush();
		}

		// Execute conversation
		public void run() {

			try {

				String url = null;

				// Communicate with client until "bye " received.
				while (true) {
					// 通过输入流接收客户端信息
					String line = in.readLine();
					if (line == null || "".equals(line.trim()))
						break;

					if (line.startsWith("GET")) {
						url = line.substring(4);
					}

					System.out.println("Received: " + line);
				}

				if (url != null && url.indexOf("/rpc/obtainTicket.action") >= 0) {
					Properties property = new Properties();

					String paramString = url;
					int i;
					String string1;
					String string2;

					i = paramString.indexOf('?');

					if (i > 0) {
						string1 = paramString.substring(0, i);
						string2 = paramString.substring(i + 1);

						paramString = string2;

						i = paramString.indexOf('&');

						while (i > 0) {
							string1 = paramString.substring(0, i);
							string2 = paramString.substring(i + 1);

							paramString = string1;
							i = paramString.indexOf('=');

							if (i > 0) {
								property.put(paramString.substring(0, i),
										paramString.substring(i + 1));
							} else {
								// property.put(paramString, "");
							}

							paramString = string2;
							i = paramString.indexOf('&');
						}

						// Set keyset = property.keySet();
						// for (Object object : keyset)
						// {
						// String propValue=
						// property.getProperty(object.toString()).toString();
						//
						// System.out.println(object + " " + propValue);
						// }

						obtainTicket(property, out);
					}

				}

				out.close();
				in.close();
				socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// main method
	public static void main(String[] args) throws IOException {
		
		String address = getServerAddress(); //"127.0.0.1";

		int port = 2088;

		if (args.length >= 2) {
			address = args[0];
			port = Integer.parseInt(args[1]);
		}

		new FakeServer(address, port);

	}

	public static String getServerAddress() throws SocketException {
		
		Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
					.nextElement();
			
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					String result = ip.getHostAddress();
					if (!result.equals("127.0.0.1")) return result;
				}
			}
		}
		return "127.0.0.1";
	}
}