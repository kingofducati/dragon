package com.ximucredit.dragon;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.qq.weixin.mp.aes.WechatUser;
import com.ximucredit.dragon.service.LoginService;
import com.ximucredit.dragon.service.QYMessageService;

/**
 * Servlet implementation class WeChatServlet
 */
public class WeChatServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 572008189795857458L;
	
	private QYMessageService weiXinService;
	private LoginService loginService;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public WeChatServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		WebApplicationContext wac =   
                WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		weiXinService=wac.getBean(QYMessageService.class);
		loginService=wac.getBean(LoginService.class);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command=request.getParameter("state");
		if(command!=null&&command.length()>0){
			if("login".equals(command)){
				String code=request.getParameter("code");
				if(code!=null&&code.length()>0){
					WechatUser user=weiXinService.getQYUser(code);
					
					if(user!=null){
						saveLogin(user,code,weiXinService.getToken());
						
						request.getSession().setAttribute("wcuser", user);
						response.sendRedirect("td/index.html");
					}else{
						response.sendRedirect("td/error.html?user=none");
					}
					
				}else{
					response.sendRedirect("td/error.html?code=na");
				}
			}else{
				response.sendRedirect("td/error.html?command=no_login");
			}
		}else{
			response.sendRedirect("td/error.html?command=na");
		}
	}

	private void saveLogin(WechatUser user, String code, String token) {
		loginService.updateWechat(user.getUserId(), code, user.getDeviceId(), token, new Date(), "");
	}

}
