using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class SoundAnalysisPlugin : MonoBehaviour {

	private AndroidJavaObject SoundAnalysis = null;
	private AndroidJavaObject activityContext = null;
	private int test=0;
	public Text testText;
	// Use this for initialization
	void Start () {
		Debug.Log ("RMS" + OSHokBridge.returnRMS ());
		Debug.Log ("Test" + OSHokBridge.returnTestValue ());
	}

	void Update() {
		//testText.text="Teftftfst"+ test;	
	}
}
