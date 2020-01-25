package cn.xhq1.entily;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class Music {
	private String tile;
	private String description;
	private String 	musicURL;
	private String hqMusicUrl;
	private String thumbMediaId;
	public String getTile() {
		return tile;
	}
	public void setTile(String tile) {
		this.tile = tile;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMusicURL() {
		return musicURL;
	}
	public void setMusicURL(String musicURL) {
		this.musicURL = musicURL;
	}
	public String getHqMusicUrl() {
		return hqMusicUrl;
	}
	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}
	public String getThumbMediaId() {
		return thumbMediaId;
	}
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
	public Music(String tile, String description, String musicURL, String hqMusicUrl, String thumbMediaId) {
		super();
		
		this.tile = tile;
		this.description = description;
		this.musicURL = musicURL;
		this.hqMusicUrl = hqMusicUrl;
		this.thumbMediaId = thumbMediaId;
	}
	
}
