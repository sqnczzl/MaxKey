package org.maxkey.web.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * <@basePath/>
   *  获取请求地址及应用上下文标签
 * get Http Context full Path,if port equals 80 443 is omitted
 * @return String
 * eg:http://192.168.1.20:9080/webcontext or http://www.website.com/webcontext
 * @author Crystal.Sea
 *
 */

@FreemarkerTag("basePath")
public class BasePathTagDirective implements TemplateDirectiveModel {
	@Autowired
    private HttpServletRequest request;
	
	private String basePath = null;

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		if(basePath==null) {
			basePath = request.getScheme()+"://"+request.getServerName();
			int port=request.getServerPort();
			if(port==443 && request.getScheme().equalsIgnoreCase("https")){
				
			}else if(port==80 && request.getScheme().equalsIgnoreCase("http")){
				
			}else{
				basePath	+=	":"+port;
			}
			basePath += request.getContextPath()+"";
		}
		env.getOut().append(basePath);
		

	}

}
