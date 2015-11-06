using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class ServerStartButtonClick : MonoBehaviour {

	// Use this for initialization
	void Start () {
        gameObject.GetComponent<Button>().onClick.AddListener(delegate { GameObject.Find("MMManager").GetComponent<MMNetworkManager>().OnStartServerClickEvent(); });
	}
	
	// Update is called once per frame
	void Update () {
	
	}
}
