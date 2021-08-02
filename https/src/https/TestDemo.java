package https;

import java.io.File;
import java.util.UUID;

import com.rfp.ws.utils.ECCFileUtils;
import com.rfp.ws.utils.ECCUtils;

/**
 * 
* @ClassName: TestDemo 
* @Description: 完成测试案例，包含报文格式，加解密，接口调用
* @author 中国人民银行征信中心动产融资服务平台
* @date 2018年12月6日 上午9:39:05 
*
 */
public class TestDemo {

	public static void main(String[] args) {
		try {
			//核心企业/银行组织机构代码
			String orgCode = "1234567890";
			//业务数据文件文件路径
//		String filePath = "D:/rfpfile/B2804.txt";
			
			//请求报文的加密key
			String pubKey1 = "D:/rfpfile/key/key_000001.pub";
			String priKey1 = "D:/rfpfile/key/key_000001.pri";
			//响应报文的解密key
			String priKey2 = "D:/rfpfile/key/key_000002.pri";
			
			//消息主键，唯一编码
			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
			//head节点拼装
			String head = "<TRANSCODE>B2804</TRANSCODE> " +
						  "<MSGID>" + uuid + "</MSGID>" +
						  "<ORGNLMSGID>12345678901234567890</ORGNLMSGID>" +
						  "<SESSIONNO>82549361-ad38-46b5-b67c-ffc9aa8fada4</SESSIONNO>" +
						  "<SENDDATE>20171124</SENDDATE>" +
						  "<SENDTIME>202020</SENDTIME>" +
						  "<ERRCODE></ERRCODE>" +
						  "<ERRMSG></ERRMSG>";
			//body节点拼装
			String body = "<ORG_NO>" + orgCode + "</ORG_NO><CD_NEW>1</CD_NEW>";

			//节点加密
			String headEncryptStr = ECCUtils.encrypt(head, pubKey1);
			String bodyEncryptStr = ECCUtils.encrypt(body, pubKey1);
			
			//本地解密测试
			System.out.println("本地加密测试_head: "+ECCUtils.decrypt(headEncryptStr, priKey1));
			System.out.println("本地加密测试_body: "+ECCUtils.decrypt(bodyEncryptStr, priKey1));
			System.out.println("本地解密测试_head: "+ECCUtils.decrypt(headEncryptStr, priKey1));
			System.out.println("本地解密测试_body: "+ECCUtils.decrypt(bodyEncryptStr, priKey1));
			
			//生成data
			String data1 = "<DOCUMENT>"
					+ "<TITLE>" + orgCode + "</TITLE>"
					+ "<HEAD>" + head + "</HEAD>"
					+ "<BODY>" + body + "</BODY>"
				+ "</DOCUMENT>";
			String data = "<DOCUMENT>"
					+ "<TITLE>" + orgCode + "</TITLE>"
					+ "<HEAD>" + headEncryptStr + "</HEAD>"
					+ "<BODY>" + bodyEncryptStr + "</BODY>"
				+ "</DOCUMENT>";

			System.out.println("发送报文明文: "+data1);
			System.out.println("发送报文密文: "+data);
			
			//根据数据文件获取file节点数据
//			System.out.println("本地文件加密测试"+filePath);
//			String file = ECCFileUtils.encryptFile(filePath, pubKey1);
			System.out.println("本地文件解密测试"+"E:/rfpfile/B2804-2.txt");
//			ECCFileUtils.decryptFile("E:/rfpfile/B2804-2.txt", file, priKey1);
			//调用接口
			//host中增加        111.30.77.179   ws.crcrfsp.com
			//企业接口对接地址:https://ws.crcrfsp.com/services/RFPServiceForEnterprise?wsdl
			//银行接口对接地址:https://ws.crcrfsp.com/services/RFPServerForBankService?wsdl
			String uri = "https://ws.crcrfsp.com/services/RFPServiceForEnterprise?wsdl";
			//无业务文件接口,只写参数data， 参数file不用写
			System.out.println("调用接口开始");
			String responseMsg = HttpsClient.callHttpService(uri, "serviceAndFile", data, "");
			//返回信息解密
			System.out.println("接口返回报文密文: "+responseMsg);
			System.out.println("调用接口结束");
			//服务器f
			if(responseMsg.indexOf("<ERRCODE>") == -1) {
				//解密返回报文的body节点
				String head2 = responseMsg.substring(responseMsg.lastIndexOf("<HEAD>") + "<HEAD>".length(), responseMsg.lastIndexOf("</HEAD>"));
				String body2 = responseMsg.substring(responseMsg.lastIndexOf("<BODY>") + "<BODY>".length(), responseMsg.lastIndexOf("</BODY>"));

				String data2 = "<DOCUMENT>"
						+ "<TITLE>" + orgCode + "</TITLE>"
						+ "<HEAD>" + ECCUtils.decrypt(head2, priKey2) + "</HEAD>"
						+ "<BODY>" + ECCUtils.decrypt(body2, priKey2) + "</BODY>"
					+ "</DOCUMENT>";
				
				System.out.println("接口返回报文明文: " + data2);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
}
