/**
 * 
 */
package com.toroot.TextFetch.clean;


/**
 * @author longkailiang
 *
 */
public class AtagCleaner extends NormalTagCleaner {

	
	protected void setTag() {
		TAG = "a";
		TAG_END = "/a";
	}
	
//	@Override
//	public CleanerInfo clean(List<SliceInfo> list, int begin) {
//		setTag();
//		int beginNum = 1, endNum = 0, i=begin;
//		int len = list.size();
//		StringBuffer content = new StringBuffer();
//		String tag;
//		SliceInfo sliceInfo;
//		while (i < len) {
//			sliceInfo = list.get(i);	
//			tag = sliceInfo.getTag();
//			if(tag != null){
//				if(TAG.equals(tag)) beginNum++;
//				if(TAG_END.equals(tag)) endNum++;
//				if(beginNum<=endNum) break;
//			}
//			else {
//				
//				if(TAG.equals(tag) && sliceInfo.getContent().length()>=count)
//				{
//					continue;
//				}
//				content.append(sliceInfo.getContent()+" ");
//			}
//			i++;
//		}
//		return new CleanerInfo(new SliceInfo(content.toString(), null), begin, i);
//	}
	
}
