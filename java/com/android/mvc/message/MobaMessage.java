package com.android.mvc.message;

import com.android.mvc.message.MobaMessageManager.MobaMessageType;

/// <summary>
/// class for the message
/// </summary>
public class MobaMessage
{
	//#region field
	protected MobaMessageType mType;
	protected int mID;
	protected int mUnitID; //单位id
	protected Object mParam;
	protected float mDelayTime;
	protected float mDelayElapsedTime;
	public boolean isFinal; //	is the last message, used in callback for view layer.
	//#endregion

	//#region constructor
	public MobaMessage (MobaMessageType type, int id, Object param)
	{
		mType = type;
		mID = id;
		mParam = param;
		mDelayTime = 0.0f;
		mDelayElapsedTime = 0.0f;
	}

	public MobaMessage (MobaMessageType type, int id, Object param, float delayTime)
	{
		mType = type;
		mID = id;
		mParam = param;
		mDelayTime = delayTime;
		mDelayElapsedTime = 0.0f;
	}

	public MobaMessage (MobaMessageType type, int id, Object param, 
			float delayTime, int unit_id, boolean isfinal)
	{
		mType = type;
		mID = id;
		mUnitID = unit_id;
		mParam = param;
		mDelayTime = delayTime;
		mDelayElapsedTime = 0.0f;
		isFinal = isfinal;
	}
	//#endregion

	//#region Delay Check
	/// <summary>
	/// delay or not.
	/// </summary>
	/// <returns></returns>
	public boolean IsDelayExec ()
	{
		boolean ret = false;
		return false;
		/*
		//	REBOL TODO, deltatime
		//mDelayElapsedTime += Time.deltaTime;
		if (mDelayElapsedTime < mDelayTime) {
			ret = true;
		}

		return ret;
		*/
	}
  //#endregion

  //#region get
	public MobaMessageType MessageType() {
		return mType;
	}

	public int ID() {
		return mID;
	}

	public Object Param() {
		return mParam;
	}

	public int UnitID() {
		return mUnitID;
	}
	//#endregion
}