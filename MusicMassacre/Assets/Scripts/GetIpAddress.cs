using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class GetIpAddress : MonoBehaviour {

    private string m_ipAddress;

	// Use this for initialization
	void Start () {
        m_ipAddress = Network.player.ipAddress;
        gameObject.GetComponent<Text>().text = "IP Address: " + m_ipAddress;
	}
	
	// Update is called once per frame
	void Update () {
	
	}
}
