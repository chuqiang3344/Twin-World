package com.toroot.TextFetch.clean;

import java.util.List;

import com.toroot.TextFetch.entity.CleanerInfo;
import com.toroot.TextFetch.entity.SliceInfo;

public class BrTagCleaner extends TagCleaner{

	@Override
	public CleanerInfo clean(List<SliceInfo> list, int begin) {		
		return new CleanerInfo(new SliceInfo("\n", null), begin-1, begin-1);
	}

}
