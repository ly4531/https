package https;

import java.net.SocketTimeoutException;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

public class HttpsClient {
	public static void main(String[] args) {
		try {
			System.out.println("aaa");
//			https 测试方法
//			host中增加        111.30.77.179   ws.crcrfsp.com
//			银行接口对接地址:https://ws.crcrfsp.com/services/RFPServerForBankService?wsdl
//			企业接口对接地址:https://ws.crcrfsp.com/services/RFPServiceForEnterprise?wsdl
			String uri = "https://ws.crcrfsp.com/services/RFPServiceForEnterprise?wsdl";
			String xml = "<DOCUMENT><TITLE>1234567890</TITLE><HEAD>BEWoNBvYokpTV5BbXOtffW2e8mMfnFhRfACmASGLqVgL8eY5CR5A3X0prUQvO/mNyQhHBAkSI/uR7j84cvR4yOZN/m2cljTxy0G/VeF45UL4vqqTtDrebN+KhREFE6x2LfFxltwBRQGPUV9UOlwjOsJurG0U/pAaihTYo4asmgEN5rGBccpZySTA1MyytFXjJFgeuJKVLaekhPQXDILPf50UX0yYp/xmK5MdLFBwwA+Ec29lqOEuKj634mw28ARjHSI5hB8QIbhBinR5vqBIZP1EAjalFCxBLmxC73R/FxGT0mKZd5fMuIbQYzEH0HQB272OaLAHE91ev+krCbdm6MgP5lz4Mn7MaHqLeWIlSYDn/2cCGNvhgwTpaKS4+j2DiY76NAiZb088a26DudVzxhN6gUEW++RpkMqikqKi/yS6mwwXcVfLykH9yaYAgoEieh0MjHk987GN6qk=</HEAD><BODY>BBvg69fXUNlcPEBslJMB2RG+mHJAD1vk018lpn2zIgnVyNHxZObBm6B0WeGH+Y4VYsc4crIT+aIR0Cf4gn3FtV6U+mLJSTZBZpeo+fJ+TrlkCgEzLQhelw==</BODY></DOCUMENT>";
			
			String bbb = HttpsClient.callHttpService(uri, "serviceAndFile", xml, "");
			System.out.println("bbb===="+bbb);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String callHttpService(String wsdl, String methodName, Object... data) throws Exception{
		String returnStr = null;
		try {
			JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
			Client client = clientFactory.createClient(wsdl);

			//需要添加超时参数等,添加如下代码
			HTTPConduit conduit = (HTTPConduit) client.getConduit();// 获取静态映射
			HTTPClientPolicy policy = new HTTPClientPolicy();// 创建策略
			policy.setAllowChunking(false); //设置允许分块传输   
			policy.setConnectionTimeout(180000);// 连接服务器超时时间3分钟
			policy.setReceiveTimeout(180000);//等待服务器响应超时时间3分钟
			conduit.setClient(policy); //将策略设置进端点

			Object[] result = client.invoke(methodName, data);
			returnStr = result[0].toString();
			
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnStr;
	}
	
}
