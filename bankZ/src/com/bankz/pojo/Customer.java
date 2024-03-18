package com.bankz.pojo;




public class Customer extends User{
	
		
		private long aadharNum;
		private String panNum;
		public long getAadharNum() {
			return aadharNum;
		}
		public void setAadharNum(long aadharNum) {
			this.aadharNum =aadharNum;
		}
		public String getPanNum() {
			return panNum;
		}
		public void setPanNum(String panNum) {
			this.panNum = panNum;
		}
}
