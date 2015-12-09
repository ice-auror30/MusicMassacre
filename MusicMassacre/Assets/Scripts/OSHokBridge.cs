using UnityEngine;
using System.Collections;
using System.Runtime.InteropServices;
using System;

public class OSHokBridge : MonoBehaviour {

	public static int returnRMS(){
		AndroidJavaClass ajc = new AndroidJavaClass ("com.example.iceauror.soundanalysis");
		return ajc.CallStatic<int>("returnRMS");
	}
	public static int returnTestValue(){
		AndroidJavaClass ajc = new AndroidJavaClass ("com.example.iceauror.soundanalysis");
		return ajc.CallStatic<int>("returnTestValue");	
	}
}
