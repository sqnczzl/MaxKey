package org.maxkey.web.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.maxkey.constants.ConstantsOperateMessage;
import org.maxkey.constants.ConstantsTimeInterval;
import org.maxkey.crypto.ReciprocalUtils;
import org.maxkey.crypto.password.PasswordReciprocal;
import org.maxkey.domain.UserInfo;
import org.maxkey.persistence.service.UserInfoService;
import org.maxkey.util.StringUtils;
import org.maxkey.web.WebConstants;
import org.maxkey.web.WebContext;
import org.maxkey.web.message.Message;
import org.maxkey.web.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value={"/safe"})
public class SafeController {
	final static Logger _logger = LoggerFactory.getLogger(SafeController.class);
	
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@ResponseBody
	@RequestMapping(value="/forward/changePasswod") 
	public ModelAndView fowardChangePasswod() {
			ModelAndView modelAndView=new ModelAndView("safe/changePassword");
			modelAndView.addObject("model", WebContext.getUserInfo());
			return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value="/changePassword") 
	public Message changePasswod(
			@RequestParam(value ="oldPassword",required = true) String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword) {
		
			if(changeUserPassword(oldPassword,newPassword,confirmPassword)) {
				return  new Message(WebContext.getI18nValue(ConstantsOperateMessage.UPDATE_SUCCESS),MessageType.success);
			}else {
				return  new Message(WebContext.getI18nValue(ConstantsOperateMessage.UPDATE_ERROR),MessageType.error);
			}	
	}

	@RequestMapping(value="/changeExpiredPassword") 
	public ModelAndView changeExpiredPassword(
			@RequestParam(value ="oldPassword",required = false) String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword) {
			ModelAndView modelAndView=new ModelAndView("passwordExpired");
		
			if(changeUserPassword(oldPassword,newPassword,confirmPassword)){
				return WebContext.redirect("/index");
				//modelAndView.setViewName("index");
			}
				
		
			new Message(WebContext.getI18nValue(ConstantsOperateMessage.UPDATE_ERROR),MessageType.error);
		 
			return modelAndView;
	}
	
	
	@RequestMapping(value="/changeInitPassword") 
	public ModelAndView changeInitPassword(
			@RequestParam(value ="oldPassword",required = false) String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword) {
		ModelAndView modelAndView=new ModelAndView("passwordInitial");
		
		if(changeUserPassword(oldPassword,newPassword,confirmPassword)){
			return WebContext.redirect("/index");
			//modelAndView.setViewName("index");
		}
		
		  new Message(WebContext.getI18nValue(ConstantsOperateMessage.UPDATE_ERROR),MessageType.error);
		  return modelAndView;
	}
	
	public boolean changeUserPassword(String oldPassword,
									String newPassword,
									String confirmPassword){
		UserInfo userInfo =WebContext.getUserInfo();
		_logger.debug("decipherable old : "+userInfo.getDecipherable());
		_logger.debug("decipherable new : "+ReciprocalUtils.encode(PasswordReciprocal.getInstance().rawPassword(userInfo.getUsername(), newPassword)));
		if(newPassword.equals(confirmPassword)){
			if(oldPassword==null || 
					passwordEncoder.matches(PasswordReciprocal.getInstance().rawPassword(userInfo.getUsername(),oldPassword), userInfo.getPassword())){
				userInfo.setPassword(newPassword);
				userInfoService.changePassword(userInfo);
				//TODO syncProvisioningService.changePassword(userInfo);
				return true;
			}
		}
		return false;
		
	}

	@ResponseBody
	@RequestMapping(value="/forward/changeAppLoginPasswod") 
	public ModelAndView fowardChangeAppLoginPasswod() {
			ModelAndView modelAndView=new ModelAndView("safe/changeAppLoginPasswod");
			modelAndView.addObject("model", WebContext.getUserInfo());
			return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value="/changeAppLoginPasswod") 
	public Message changeAppLoginPasswod(
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword) {
		
		UserInfo userInfo =WebContext.getUserInfo();
		_logger.debug("App Login Password : "+userInfo.getAppLoginPassword());
		_logger.debug("App Login new Password : "+ReciprocalUtils.encode(newPassword));
		if(newPassword.equals(confirmPassword)){
			if(StringUtils.isNullOrBlank(userInfo.getAppLoginPassword())||userInfo.getAppLoginPassword().equals(ReciprocalUtils.encode(oldPassword))){
				userInfo.setAppLoginPassword(ReciprocalUtils.encode(newPassword));
				boolean change= userInfoService.changeAppLoginPassword(userInfo);
				_logger.debug(""+change);
				return  new Message(WebContext.getI18nValue(ConstantsOperateMessage.UPDATE_SUCCESS),MessageType.prompt);
			}
		}
		
		return  new Message(WebContext.getI18nValue(ConstantsOperateMessage.UPDATE_ERROR),MessageType.error);
		
	}
	
	
	@RequestMapping(value="/forward/setting") 
	public ModelAndView fowardSetting() {
			ModelAndView modelAndView=new ModelAndView("safe/setting");
			modelAndView.addObject("model", WebContext.getUserInfo());
			return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value="/setting") 
	public Message setting(
	        HttpServletRequest request,
            HttpServletResponse response,
			@RequestParam("authnType") String authnType,
			@RequestParam("mobile") String mobile,
			@RequestParam("mobileVerify") String mobileVerify,
			@RequestParam("email") String email,
			@RequestParam("emailVerify") String emailVerify,
			@RequestParam("theme") String theme) {
		UserInfo userInfo =WebContext.getUserInfo();
		userInfo.setAuthnType(Integer.parseInt(authnType));
		userInfoService.changeAuthnType(userInfo);
		
		userInfo.setMobile(mobile);
		userInfoService.changeMobile(userInfo);
		
		userInfo.setEmail(email);

        userInfo.setTheme(theme);
        WebContext.setCookie(response, WebConstants.THEME_COOKIE_NAME, theme, ConstantsTimeInterval.ONE_WEEK);
        
		userInfoService.changeEmail(userInfo);
		
		
		return  new Message(WebContext.getI18nValue(ConstantsOperateMessage.UPDATE_SUCCESS),MessageType.success);
		
	}
	
}
