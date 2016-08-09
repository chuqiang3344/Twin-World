package com.toroot.TextFetch.repair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.toroot.TextFetch.entity.SliceInfo;

/**
 * 网页标签修复
 * @author mg
 * */
public class HtmlRepair {
private static Map<String, TagRepair> tagRepairMap;
	
	static{
		tagRepairMap = new HashedMap();
		tagRepairMap.put("NOR", new NormalTagRepair());
		
	}
	
	public static TagRepair getRepair(String tag){
		return tagRepairMap.get(tag);
	}
	
	public static void register(String tag, TagRepair tagRepair){
		tagRepairMap.put(tag.toLowerCase().trim(), tagRepair);
	}
	public List<SliceInfo> repair(List<SliceInfo> sliceInfos){
		TagRepair normalRepair=getRepair("NOR");
		if(normalRepair!=null)
		{
			HashSet<Integer> waitRepair=normalRepair.repair(sliceInfos);
			List<SliceInfo> repairList = new ArrayList<SliceInfo>();
			int len = sliceInfos.size();
			for(int i=0;i<len;i++)
			{
				if(!waitRepair.contains(i)){
					repairList.add(sliceInfos.get(i));
				}
			}
			return repairList;
		}
		return sliceInfos;
	}
}
