package cn.xhq1.serves;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;

import cn.xhq1.entily.Article;
import cn.xhq1.entily.BaseMessage;
import cn.xhq1.entily.ImageMessage;
import cn.xhq1.entily.MusicMessage;
import cn.xhq1.entily.NewsMessage;
import cn.xhq1.entily.TextMessage;
import cn.xhq1.entily.VideoMessage;
import cn.xhq1.entily.VoiceMessage;
import cn.xhq1.util.util;
import net.sf.json.JSONObject;




public class WxService {
		private static final String TOKEN ="abc";
		private static final String APPKEY ="c7b1d707a9f2d598e6a4231af3502135";
		private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	    private static final String APPID = "wxa13579ea6268b895";
	    private static final String APPSECRET = "b2965138681935d6bc5e51942f3f5df8";
	    
	    //用于存储
	    public static AccessToken at;
	    /**
	     * 获取token
	     */
	    private static void getTkoken() {
	    	//替换连接中的关键字
	    	String url = GET_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET",APPSECRET);
	    	String tokenStr = util.get(url);
	    	System.out.println(tokenStr);
	    	JSONObject jsonObject = JSONObject.fromObject(tokenStr);
	    	String access_token = jsonObject.getString("access_token");
	    	System.out.println("asce/////"+access_token);
	    	String expires_in = jsonObject.getString("expires_in");
	    	//创建token对象 并存起来
	    	 at = new AccessToken(access_token,expires_in);
	    	System.out.println(expires_in);
	    	
	    }
	    /**
	     * 向外暴露token的方法
	     * @return
	     */
	    public static String getAcessToken(){
	    	
	    	if(at==null||at.isExpired()){
	    		getTkoken();
	    		
	    	}
	    	return at.getAccessToken();
	    }
	/**
	 * 接入验证
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */

	public static boolean check(String timestamp, String nonce, String signature) {
		
		 // 1）将token、timestamp、nonce三个参数进行字典序排序 
			String[] strs = new String[]{TOKEN,timestamp,nonce};
			Arrays.sort(strs);
		 // 2）将三个参数字符串拼接成一个字符串进行sha1加密 
			String str = strs[0]+strs[1]+strs[2];
			String mysig = sha1(str);
			System.out.println(mysig);
			System.out.println(signature);
		  //3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		 
		return mysig.equalsIgnoreCase(signature);
	}
		/**
		 * 进行sha1加密
		 * @param str
		 * @return
		 */
	private static String sha1(String src) {
		try {
//			获取加密对象
			MessageDigest md = MessageDigest.getInstance("sha1");
			//加密
			byte[] digest = md.digest(src.getBytes());
			
			char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			StringBuilder sb = new StringBuilder();
			//处理加密结果
			for(byte b :digest) {
				sb.append(chars[(b>>4)&15]);
				sb.append(chars[b&15]);
				
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
		/**
		 * 解析xml数据包
		 * @param is
		 * @return
		 */
		public static Map<String, String> parseRequest(InputStream is) {
			Map<String,String>map = new HashMap<>();
			SAXReader reader = new SAXReader();
			  try {
				 //读取输入流，获取文档对象	
				Document read = reader.read(is);
				//获取文档对象 拿到根节点
				Element root = read.getRootElement();
				//获取根节点的所有子节点
				List<Element> element = root.elements();
				for(Element e:element) {
					map.put(e.getName(), e.getStringValue());
				}
				
			} catch (DocumentException e) {
				
				e.printStackTrace();
			}
			
			
			return map;
		}
		/**
		 * 
		 * 用于处理所有的事件和消息回复
		 * @param requestMap
		 * @return 返回的是xml数据包
		 */
		public static String getRespose(Map<String, String> requestMap) {
			BaseMessage msg = null;
			String 	msgType = requestMap.get("MsgType");
			switch(msgType) {
			//处理文本消息
			case"text":
				msg = dealTextMessage(requestMap);
				break;
			case"image":
				
				break;
			case"voice":
				break;
			case"video":
				break;
			case"shortvideo":
				break;
			case"location":
				break;
			case"link":
				break;
			default:
				break;
			}
			System.out.println(msg);
			if(msg!=null) {
				//把消息对象处理为xml数据包
				return beanToXml(msg);
			}else {
				System.out.println("msg为空");
				return null;
			}
			
		}
		
		/**
		 * 
		 * 把消息对象处理为xml数据包
		 * 
		 * @param msg
		 * @return
		 */
		private static String beanToXml(BaseMessage msg) {
			XStream stream = new XStream();
			//设置需要@XStreamAlias("MediaId")数据
			stream.processAnnotations(TextMessage.class);
			stream.processAnnotations(ImageMessage.class);
			stream.processAnnotations(MusicMessage.class);
			stream.processAnnotations(NewsMessage.class);
			stream.processAnnotations(VideoMessage.class);
			stream.processAnnotations(VoiceMessage.class);
			String xml = stream.toXML(msg);
			return xml;
		}
		/**
		 * 处理文本消息
		 * @param requestMap
		 * @return
		 */
		private static BaseMessage dealTextMessage(Map<String, String> requestMap) {
			//用户发来的内容
			String msg = requestMap.get("Content");
			if(msg.equals("图文")) {
				List<Article> articles = new ArrayList<>();
				articles.add(new Article("图文消息标题","这是图文消息的详细介绍","http://mmbiz.qpic.cn/mmbiz_jpg/UN8OEcI9OBVmEkPVSOzppZwW2sicYnz5XQAySLcTy0ODLRld2lSVBibfVTwOJSyfarWgy8EaDvVqYwft6Hiaqc66Q/0","http://www.xhq1.cn"));
				NewsMessage nm = new NewsMessage(requestMap,articles);
				return nm;
			}
			//调用方法返回聊天的内容
			String resp = chat(msg);
			TextMessage tm = new TextMessage(requestMap, resp);
			
			return tm;
		}
		/**
		 * 
		 * 调用接口
		 * @param msg
		 * @return
		 */
		private static String chat(String msg) {
			String result =null;
	        String url ="http://apis.juhe.cn/mobile/get";//请求接口地址
	        Map params = new HashMap();//请求参数
	            params.put("phone",msg);//需要查询的手机号码或手机号码前7位
	            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
	            params.put("dtype","");//返回数据的格式,xml或json，默认json	 
	        try {
	            result =util.net(url, params, "GET");
	            	System.out.println("sjii"+result);
	            	com.alibaba.fastjson.JSONObject objs=JSON.parseObject(result);
	            	System.out.println(objs.getString("resultcode"));
	            			int cout = objs.getInteger("error_code");
	            			System.out.println(cout);
	            if(cout==0){
	                System.out.println("输出"+objs.get("result"));
	                
	                String resp =objs.getString("city");
	                
	                String province = objs.getJSONObject("result").getString("province");
	                String city =objs.getJSONObject("result").getString("city");
	                String company =objs.getJSONObject("result").getString("company");
	                String dizhi ="该手机号码属于"+province+city+"属于中国"+company;
	                System.out.println(province);
	                return dizhi;
	            }else{
	                System.out.println("ceshi"+objs.get("error_code")+":"+objs.get("reason"));
	                return "<a href=\"http://www.xhq1.cn\">请输入号码前7位</a>";
	            }
	             
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return null;
		}
		
}
