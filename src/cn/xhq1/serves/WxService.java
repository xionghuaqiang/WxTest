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
	    
	    //���ڴ洢
	    public static AccessToken at;
	    /**
	     * ��ȡtoken
	     */
	    private static void getTkoken() {
	    	//�滻�����еĹؼ���
	    	String url = GET_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET",APPSECRET);
	    	String tokenStr = util.get(url);
	    	System.out.println(tokenStr);
	    	JSONObject jsonObject = JSONObject.fromObject(tokenStr);
	    	String access_token = jsonObject.getString("access_token");
	    	System.out.println("asce/////"+access_token);
	    	String expires_in = jsonObject.getString("expires_in");
	    	//����token���� ��������
	    	 at = new AccessToken(access_token,expires_in);
	    	System.out.println(expires_in);
	    	
	    }
	    /**
	     * ���Ⱪ¶token�ķ���
	     * @return
	     */
	    public static String getAcessToken(){
	    	
	    	if(at==null||at.isExpired()){
	    		getTkoken();
	    		
	    	}
	    	return at.getAccessToken();
	    }
	/**
	 * ������֤
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */

	public static boolean check(String timestamp, String nonce, String signature) {
		
		 // 1����token��timestamp��nonce�������������ֵ������� 
			String[] strs = new String[]{TOKEN,timestamp,nonce};
			Arrays.sort(strs);
		 // 2�������������ַ���ƴ�ӳ�һ���ַ�������sha1���� 
			String str = strs[0]+strs[1]+strs[2];
			String mysig = sha1(str);
			System.out.println(mysig);
			System.out.println(signature);
		  //3�������߻�ü��ܺ���ַ�������signature�Աȣ���ʶ��������Դ��΢��
		 
		return mysig.equalsIgnoreCase(signature);
	}
		/**
		 * ����sha1����
		 * @param str
		 * @return
		 */
	private static String sha1(String src) {
		try {
//			��ȡ���ܶ���
			MessageDigest md = MessageDigest.getInstance("sha1");
			//����
			byte[] digest = md.digest(src.getBytes());
			
			char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			StringBuilder sb = new StringBuilder();
			//������ܽ��
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
		 * ����xml���ݰ�
		 * @param is
		 * @return
		 */
		public static Map<String, String> parseRequest(InputStream is) {
			Map<String,String>map = new HashMap<>();
			SAXReader reader = new SAXReader();
			  try {
				 //��ȡ����������ȡ�ĵ�����	
				Document read = reader.read(is);
				//��ȡ�ĵ����� �õ����ڵ�
				Element root = read.getRootElement();
				//��ȡ���ڵ�������ӽڵ�
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
		 * ���ڴ������е��¼�����Ϣ�ظ�
		 * @param requestMap
		 * @return ���ص���xml���ݰ�
		 */
		public static String getRespose(Map<String, String> requestMap) {
			BaseMessage msg = null;
			String 	msgType = requestMap.get("MsgType");
			switch(msgType) {
			//�����ı���Ϣ
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
				//����Ϣ������Ϊxml���ݰ�
				return beanToXml(msg);
			}else {
				System.out.println("msgΪ��");
				return null;
			}
			
		}
		
		/**
		 * 
		 * ����Ϣ������Ϊxml���ݰ�
		 * 
		 * @param msg
		 * @return
		 */
		private static String beanToXml(BaseMessage msg) {
			XStream stream = new XStream();
			//������Ҫ@XStreamAlias("MediaId")����
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
		 * �����ı���Ϣ
		 * @param requestMap
		 * @return
		 */
		private static BaseMessage dealTextMessage(Map<String, String> requestMap) {
			//�û�����������
			String msg = requestMap.get("Content");
			if(msg.equals("ͼ��")) {
				List<Article> articles = new ArrayList<>();
				articles.add(new Article("ͼ����Ϣ����","����ͼ����Ϣ����ϸ����","http://mmbiz.qpic.cn/mmbiz_jpg/UN8OEcI9OBVmEkPVSOzppZwW2sicYnz5XQAySLcTy0ODLRld2lSVBibfVTwOJSyfarWgy8EaDvVqYwft6Hiaqc66Q/0","http://www.xhq1.cn"));
				NewsMessage nm = new NewsMessage(requestMap,articles);
				return nm;
			}
			//���÷����������������
			String resp = chat(msg);
			TextMessage tm = new TextMessage(requestMap, resp);
			
			return tm;
		}
		/**
		 * 
		 * ���ýӿ�
		 * @param msg
		 * @return
		 */
		private static String chat(String msg) {
			String result =null;
	        String url ="http://apis.juhe.cn/mobile/get";//����ӿڵ�ַ
	        Map params = new HashMap();//�������
	            params.put("phone",msg);//��Ҫ��ѯ���ֻ�������ֻ�����ǰ7λ
	            params.put("key",APPKEY);//Ӧ��APPKEY(Ӧ����ϸҳ��ѯ)
	            params.put("dtype","");//�������ݵĸ�ʽ,xml��json��Ĭ��json	 
	        try {
	            result =util.net(url, params, "GET");
	            	System.out.println("sjii"+result);
	            	com.alibaba.fastjson.JSONObject objs=JSON.parseObject(result);
	            	System.out.println(objs.getString("resultcode"));
	            			int cout = objs.getInteger("error_code");
	            			System.out.println(cout);
	            if(cout==0){
	                System.out.println("���"+objs.get("result"));
	                
	                String resp =objs.getString("city");
	                
	                String province = objs.getJSONObject("result").getString("province");
	                String city =objs.getJSONObject("result").getString("city");
	                String company =objs.getJSONObject("result").getString("company");
	                String dizhi ="���ֻ���������"+province+city+"�����й�"+company;
	                System.out.println(province);
	                return dizhi;
	            }else{
	                System.out.println("ceshi"+objs.get("error_code")+":"+objs.get("reason"));
	                return "<a href=\"http://www.xhq1.cn\">���������ǰ7λ</a>";
	            }
	             
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return null;
		}
		
}
