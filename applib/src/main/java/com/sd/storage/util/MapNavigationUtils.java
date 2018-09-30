package com.sd.storage.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MapNavigationUtils {

	/**
	 * 调用导航
	 * 
	 * @param context
	 * @param navigationModel
	 * @return 
	 */
	public static boolean doNavigation(Context context,NavigationModel navigationModel) {
		
		if (!doBaiduNavigation(context, navigationModel)) {
			return doWebNavigation(context, navigationModel);
		}
		return false;
	}

	/**
	 * 调用百度导航
	 * 
	 * @param context
	 * @param navigationModel
	 * @return
	 */
	public static boolean doBaiduNavigation(Context context,
			NavigationModel navigationModel) {
		try {// 如果有安装百度地图 就启动百度地图
			double[] bd_lat_lon = gaoDeToBaidu(navigationModel.toLon, navigationModel.toLat);
			Intent intent = Intent.getIntent("intent://map/marker?location="+bd_lat_lon[1]+","+bd_lat_lon[0]+"&title="+navigationModel.name+"&content="+navigationModel.toLocationAddress+"&src=顺道天下#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
			context.startActivity(intent);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 是否存在高德
	 * @param context
	 * @return
     */
	public static boolean isHasGaoDe(Context context){
		return TDevice.isAvilible(context, "com.autonavi.minimap");
	}

	/**
	 * 是否存在百度
	 * @param context
	 * @return
     */
	public static boolean isHasBaidu(Context context){
		return TDevice.isAvilible(context, "com.baidu.BaiduMap");
	}

	/**
	 * 高德导航
	 * @param context
	 * @param navigationModel
	 * @return
	 */
	public static boolean doGaoDeNavigation(Context context, NavigationModel navigationModel) {

		try {
			// 导航
//			Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=慧医&poiname=我的目的地&lat="+gd_lat_lon[0]+"&lon="+gd_lat_lon[1]+"&dev=0");
//			context.startActivity(intent);
			// 地图
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			String data="androidamap://route?sourceApplication=顺道天下&dlat="+navigationModel.toLat+"&dlon="+navigationModel.toLon+"&dname="+navigationModel.name+"&dev=0&m=0&t=1";
			intent.setData(Uri.parse(data));
			intent.setPackage("com.autonavi.minimap");

			context.startActivity(intent);

//			StringBuilder loc = new StringBuilder();
//			loc.append("androidamap://viewMap?sourceApplication=顺道天下");
//			loc.append("&poiname=");
//			loc.append(navigationModel.areaName);
//			loc.append("&lat=");
//			loc.append(gd_lat_lon[0]);
//			loc.append("&lon=");
//			loc.append(gd_lat_lon[1]);
//			loc.append("&dev=0");
//			Intent intent = new Intent(loc.toString());
//			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 调用网页端导航功能
	 * 
	 * @param context
	 * @param navigationModel
	 * @return
	 */
	public static boolean doWebNavigation(Context context, NavigationModel navigationModel) {
		try {
			double[] bd_lat_lon = gaoDeToBaidu(navigationModel.toLon, navigationModel.toLat);
			StringBuffer sb = new StringBuffer();
			sb.append("http://api.map.baidu.com/marker?location="+bd_lat_lon[1]+","+bd_lat_lon[0]+"&title="+navigationModel.name+"&content="+navigationModel.toLocationAddress+"&output=html&src=顺道天下");
			Uri uri = Uri.parse(sb.toString());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(intent);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
	    double[] gd_lat_lon = new double[2];
	    double PI = 3.14159265358979324 * 3000.0 / 180.0;
	    double x = bd_lon - 0.0065, y = bd_lat - 0.006;
	    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
	    gd_lat_lon[0] = z * Math.cos(theta);
	    gd_lat_lon[1] = z * Math.sin(theta);
	    return gd_lat_lon;
	 }

	public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
	    double[] bd_lat_lon = new double[2];
	    double PI = 3.14159265358979324 * 3000.0 / 180.0;
	    double x = gd_lon, y = gd_lat;
	    double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
	    double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
	    bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
	    bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
	    return bd_lat_lon;
	}

	public class NavigationModel {
		/*
		 * 经度
		 */
		public double formLat;
		/*
		 * 纬度
		 */
		public double formLon;
		public String myLocationAddress;

		public double toLat;
		public double toLon;
		public String toLocationAddress;

		/*
		 * 城市
		 */
		public String areaName;
		
		public String name;
	}
}
