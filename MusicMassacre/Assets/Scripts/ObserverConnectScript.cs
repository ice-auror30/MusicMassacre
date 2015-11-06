using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class ObserverConnectScript : MonoBehaviour {

    // Use this for initialization
    void Start() {
        GameObject netManager = GameObject.Find("MMManager");
        gameObject.GetComponent<Button>().onClick.AddListener(delegate { netManager.GetComponent<MMNetworkManager>().OnConnectServerClickEvent();  } );
	}
	
	// Update is called once per frame
	void Update () {

	}
}
