package cn.xhq1.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.xhq1.serves.WxService;

/**
 * Servlet implementation class WxServlet
 */
@WebServlet("/wx")
public class WxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet");
		
		String signature = request.getParameter("signature");
		String  timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		System.out.println(echostr);
		if(WxService.check(timestamp,nonce,signature)) {
				System.out.println("接入成功");
				PrintWriter out = response.getWriter();
				//原样返回
				out.print(echostr);
				out.flush();
				
				out.close();
		}else {
			System.out.println("接入失败");
		}
	}
	/**
	 * 
	 * 接收和推送消息
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		response.setCharacterEncoding("utf8");
		//处理消息和事件推送
		 Map<String,String> requestMap = WxService.parseRequest(request.getInputStream());
		 System.out.println(requestMap);
		 //准备回复的数据包
		String respXml =WxService.getRespose(requestMap);
		System.out.println(respXml);
		 PrintWriter out = response.getWriter();
		 out.print(respXml);
		 out.flush();
		 out.close();
	}

}
