package cn.xhq1.serves;

public class AccessToken {
		private String accessToken;
//		����ʱ��
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
		
		//�ж��Ƿ���� ��ǰϵͳʱ���Ƿ���� ����ʱ��
		public boolean isExpired() {
			
			return System.currentTimeMillis()>expireTime;
		}
	
}
