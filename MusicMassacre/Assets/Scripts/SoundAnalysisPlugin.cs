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
		if (SoundAnalysis == null) {
			using (AndroidJavaClass activityClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer")) {
				activityContext = activityClass.GetStatic<AndroidJavaObject> ("currentActivity");
			}
		
			using (AndroidJavaClass pluginClass = new AndroidJavaClass("com.example.iceauror.soundanalysis")) {
				if (pluginClass != null) {
					SoundAnalysis = pluginClass.CallStatic<AndroidJavaObject> ("instance");
					SoundAnalysis.Call ("setContext", activityContext);
					activityContext.Call ("runOnUiThread", new AndroidJavaRunnable (() => {
						SoundAnalysis.Call ("analyze");
						test = SoundAnalysis.Call<int>("test");
						//ArrayList peak = new ArrayList ();
						//peak = SoundAnalysis.Call ("getPeak");
						//ArrayList RMS = new ArrayList ();
						//RMS = SoundAnalysis.Call ("getRMS");
					}));
				}
			}
		}
		/*AndroidJavaClass unity = new AndroidJavaClass ("com.unity3d.player.UnityPlayer");
		AndroidJavaObject currentActivity = unity.GetStatic<AndroidJavaObject> ("currentActivity");
		currentActivity.Call("analyze");
		test = currentActivity.Call<int>("test");*/
	}

	void Update() {
		testText.text="Teftftfst"+ test;	
	}
}
