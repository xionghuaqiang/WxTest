package cn.xhq1.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.example.analoglib.Analog4150ServiceAPI;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import cn.xhq1.entily.ImageMessage;
import cn.xhq1.entily.MusicMessage;
import cn.xhq1.entily.NewsMessage;
import cn.xhq1.entily.TextMessage;
import cn.xhq1.entily.VideoMessage;
import cn.xhq1.entily.VoiceMessage;
import cn.xhq1.serves.WxService;

public class TestWx {
	
	@Test
	public void testToken() {
		System.out.println("1111111");
		System.out.println(WxService.getAcessToken());
	}
	@Test
	public void testMsg() {
		System.out.println("进入");
		Map<String,String> map = new HashMap<>();
		map.put("ToUserName", "to");
		map.put("FromUserName","from");
		map.put("MsgType","tyoe");
		TextMessage tm = new TextMessage(map, "还好");
		System.out.println(tm);
		XStream stream = new XStream();
		//设置需要@XStreamAlias("MediaId")数据
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(ImageMessage.class);
		stream.processAnnotations(MusicMessage.class);
		stream.processAnnotations(NewsMessage.class);
		stream.processAnnotations(VideoMessage.class);
		stream.processAnnotations(VoiceMessage.class);
		
		String  xml = stream.toXML(tm);
		System.out.println(xml);
	}
}
