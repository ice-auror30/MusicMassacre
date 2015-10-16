using UnityEngine;
using System.Collections;

public class GoToLevel : MonoBehaviour {

	public GameObject loadingImage;

	public void LoadLevelOnClick(int level) {
		if (loadingImage)
			loadingImage.SetActive (true);

		Application.LoadLevel (level);
	}
}
