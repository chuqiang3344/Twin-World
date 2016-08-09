package com.toroot.TextFetch.entity;
/**
 * 标签修复记录对象
 * 
 * **/
public class RepairTagInfo {
	
		private int sign;
		
		private SliceInfo sliceInfo;

		public RepairTagInfo(int sign, SliceInfo sliceInfo) {
			this.sign = sign;
			this.sliceInfo = sliceInfo;
		}

		public int getSign() {
			return sign;
		}

		public void setSign(int sign) {
			this.sign = sign;
		}

		public SliceInfo getSliceInfo() {
			return sliceInfo;
		}

		public void setSliceInfo(SliceInfo sliceInfo) {
			this.sliceInfo = sliceInfo;
		}
		
}
