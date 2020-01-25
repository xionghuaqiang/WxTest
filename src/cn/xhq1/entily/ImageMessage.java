package cn.xhq1.entily;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ImageMessage extends BaseMessage{
	@XStreamAlias("MediaId")
	private String mediaId;
	
	public ImageMessage(Map<String, String> requestMap,String mediaId) {
		
		super(requestMap);
		this.setToUserName("image");
		this.mediaId = mediaId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	

}
