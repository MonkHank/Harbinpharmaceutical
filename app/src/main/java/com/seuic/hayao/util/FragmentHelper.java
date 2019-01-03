package com.seuic.hayao.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;


public class FragmentHelper {
	
	private Map<String, Fragment> mFragments ;
	private Fragment mCurFragment;
	private FragmentManager mManager;
	private static FragmentHelper mHelper;
	private int mFrameId;
	
	private FragmentHelper(){
		
	}
	
	public static FragmentHelper getInstance(){
		if(mHelper == null){
			mHelper = new FragmentHelper();
		}
		return mHelper;
	}
	
	public void initHelper(int fmId , Activity activity){
		mFragments = new HashMap<String, Fragment>();
		mManager = activity.getFragmentManager();
		mFrameId = fmId;
	}
	
	
	public void transcate(Class<? extends Fragment> newFMClass){

		FragmentTransaction transaction = mManager.beginTransaction();

		String newCLSName = newFMClass.getName();
		Fragment newFm = mFragments.get(newCLSName);
		if(newFm == null){
			try {
				newFm = newFMClass.newInstance();
				if(mFragments.size() != 0){
					transaction.add(mFrameId, newFm);
				}
				else{
					transaction.replace(mFrameId, newFm);
				}
				mFragments.put(newCLSName, newFm);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		if(mCurFragment != null){
			transaction.hide(mCurFragment);
			transaction.show(newFm);
		}

		mCurFragment = newFm;
		transaction.commit();
	}
	
	public void clear(){
		mFragments.clear();
		mCurFragment = null;
	}
	
	public Fragment getCurFragment(){
		return mCurFragment;
	}
}
