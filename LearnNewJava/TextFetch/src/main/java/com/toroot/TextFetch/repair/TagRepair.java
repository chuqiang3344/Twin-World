package com.toroot.TextFetch.repair;

import java.util.HashSet;
import java.util.List;

import com.toroot.TextFetch.entity.SliceInfo;
/**
 * 修复标签抽象类
 * @author mg
 * */
public abstract class TagRepair {
	
		public abstract HashSet<Integer> repair(List<SliceInfo> sliceInfos);
}
