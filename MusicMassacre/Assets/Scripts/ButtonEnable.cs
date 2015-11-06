using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class ButtonEnable : MonoBehaviour {

    public GameObject[] inputFieldTexts;
	
	// Update is called once per frame
	void Update () {
        bool active = true;
        for (int i = 0; i < inputFieldTexts.Length; i++) {
            if (inputFieldTexts[i].GetComponent<Text>().text.Length <= 0) active = false;
        }
        gameObject.GetComponent<Button>().interactable = active;
	}
}
