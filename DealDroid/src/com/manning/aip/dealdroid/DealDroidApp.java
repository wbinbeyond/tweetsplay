package com.manning.aip.dealdroid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;
import com.manning.aip.dealdroid.xml.DailyDealsFeedParser;
import com.manning.aip.dealdroid.xml.DailyDealsXmlPullFeedParser;

public class DealDroidApp extends Application {
	private ConnectivityManager cMgr;
	private DailyDealsFeedParser parser;
	private List<Section> sectionList;
	private Map<Long, Bitmap> imageCache;
	private Item currentItem;
	public ConnectivityManager getcMgr() {
		return cMgr;
	}
	public void setcMgr(ConnectivityManager cMgr) {
		this.cMgr = cMgr;
	}
	public DailyDealsFeedParser getParser() {
		return parser;
	}
	public void setParser(DailyDealsFeedParser parser) {
		this.parser = parser;
	}
	public List<Section> getSectionList() {
		return sectionList;
	}
	public void setSectionList(List<Section> sectionList) {
		this.sectionList = sectionList;
	}
	public Map<Long, Bitmap> getImageCache() {
		return imageCache;
	}
	public void setImageCache(Map<Long, Bitmap> imageCache) {
		this.imageCache = imageCache;
	}
	public Item getCurrentItem() {
		return currentItem;
	}
	public void setCurrentItem(Item currentItem) {
		this.currentItem = currentItem;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		this.cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		this.parser = new DailyDealsXmlPullFeedParser();
		this.sectionList = new ArrayList<Section>(6);
		this.imageCache = new HashMap<Long,Bitmap>();		
	}
	
	public void onTerminate(){
		super.onTerminate();
	}
	
	public Bitmap retrieveBitmap(String urlString) {
	      Log.d(Constants.LOG_TAG, "making HTTP trip for image:" + urlString);
		  Bitmap bitmap = null;
		  try {
		     URL url = new URL(urlString);
		     // NOTE, be careful about just doing "url.openStream()"  
		     // it's a shortcut for openConnection().getInputStream() and doesn't set timeouts
		     // (the defaults are "infinite" so it will wait forever if endpoint server is down)
		     // do it properly with a few more lines of code . . .         
		     URLConnection conn = url.openConnection();     
		     conn.setConnectTimeout(3000);
		     conn.setReadTimeout(5000);         
		     bitmap = BitmapFactory.decodeStream(conn.getInputStream());
		  } catch (MalformedURLException e) {
		     Log.e(Constants.LOG_TAG, "Exception loading image, malformed URL", e);
		  } catch (IOException e) {
		     Log.e(Constants.LOG_TAG, "Exception loading image, IO error", e);
	      } 
	      return bitmap;
	}

   public boolean connectionPresent() {
	      NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
	      if ((netInfo != null) && (netInfo.getState() != null)) {
	         return netInfo.getState().equals(State.CONNECTED);
	      } 
	      return false;
   }

	
	
	
}
