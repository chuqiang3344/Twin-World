package com.tyaer.basic.utils;

import java.lang.reflect.Field;

public class ReverseUtils {
	
	public static <T> T reverseToString(String str,Class<T> cls){
		try {
			T resultObject = cls.newInstance();
			String[] fields=str.substring(str.indexOf("[")+1, str.lastIndexOf("]")).split(",");
			for (String field:fields) {
				System.out.println(field);
				String key=field.split("=")[0].trim();
				String value;
				try {
					value=field.split("=")[1];
				} catch (ArrayIndexOutOfBoundsException e) {
					value=null;
				}
				try {
					Field fie=cls.getDeclaredField(key);
					fie.setAccessible(true);
					fie.set(resultObject, value);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
			return resultObject;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String str="Vehicle [CarPlate=湘NE6298, PlateColor=2, VehicleSpeed=35, PlateType=02, IdentifyStatus=0, VehicleColor=Z, VehicleType=1, DriveStatus=0, TollgateID=KK-HHXYXXQ-WEST, PassTime=20160426172320886, RecordID=739,  PicNumber=2, LaneID=1, LaneType=1, Direction=1, VehicleBrand=99, PlateNumber=1, VehicleBody=, VehicleLength=0, DealTag=, PlaceCode=4312020028, EquipmentType=, PlateConfidence=95, RearPlateConfidence=0, GlobalComposeFlag=0, RedLightStartTime=, RedLightEndTime=, RedLightTime=0, VehicleTopX=1910, VehicleTopY=1457, VehicleBotX=2028, VehicleBotY=1489, LPRRectTopX=1452, LPRRectTopY=0884, LPRRectBotX=2484, LPRRectBotY=1700, DressColor=, ApplicationType=, MarkedSpeed=, RearPlateColor=, RearPlateType=, RearVehiclePlateID=, PlateCoincide=, DirectionName=, CamID=, ImageURL2=, ImageURL3=, ImageURL4=, TollgateName=]";
	}
	
}
