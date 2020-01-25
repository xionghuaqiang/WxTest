package cn.xhq1.serves;

public class AccessToken {
		private String accessToken;
//		过期时间
		private long expireTime;
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		public long getExpireTime() {
			return expireTime;
		}
		public void setExpireTime(long expireTime) {
			this.expireTime = expireTime;
		}
		
		public AccessToken(String accessToken, String expires_in) {
			super();
			this.accessToken = accessToken;
			this.expireTime = System.currentTimeMillis()+Integer.parseInt(expires_in)*1000;
		}
		
		//判断是否过期 当前系统时间是否大于 过期时间
		public boolean isExpired() {
			
			return System.currentTimeMillis()>expireTime;
		}
	
}
