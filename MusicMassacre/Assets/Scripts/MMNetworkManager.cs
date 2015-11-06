using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using System.Collections;

public class MMNetworkManager : NetworkManager {

    // used to stop duplicate instances of manager
    private MMNetworkManager instance;

    NetworkClient myClient;

    void Awake()
    {
        if(instance == null)
        {
            instance = this;
        } else
        {
            Destroy(this);
        }
        dontDestroyOnLoad = true;
        DontDestroyOnLoad(gameObject);
    }

	// Use this for initialization
	void Start () {
	}
	
	// Update is called once per frame
	void Update () {
	
	}

    public override void OnServerAddPlayer(NetworkConnection conn, short playerControllerId) 
    {
        if(conn.hostId == -1) base.OnServerAddPlayer(conn, playerControllerId);
        Debug.Log("Added player: " + conn.address + ", pci: " + playerControllerId);
        //if(conn.hostId == -1) ClientScene.AddPlayer(playerControllerId);
    }

    public override void OnServerConnect(NetworkConnection conn)
    {
        base.OnServerConnect(conn);
        Debug.Log("On Server Connect: " + conn);
        //if(conn.hostId != -1) NetworkServer.SpawnWithClientAuthority(gameObject, observerPrefab);
        // if (conn.hostId == -1) NetworkServer.SpawnWithClientAuthority(playerPrefab2, gameObject);
    }

    public override void OnStartHost()
    {
        base.OnStartHost();
        Debug.Log("Start Host");
        // var player = (GameObject)GameObject.Instantiate(playerPrefab2, playerSpawnPos, Quaternion.identity);
    }

    public override void OnClientConnect(NetworkConnection conn)
    {
        base.OnClientConnect(conn);
        Debug.Log("On Client Connect: " + conn);
    }

    public void OnStartServerClickEvent()
    {
        serverBindToIP = true;
        serverBindAddress = Network.player.ipAddress;
        instance.StartHost();
        Debug.Log("Server started");
    }

    public void OnConnectServerClickEvent()
    {
        GameObject ipInput = GameObject.Find("Canvas/IP INPUT/ipText");
        Debug.Log(ipInput.GetComponent<Text>().text);
        instance.networkAddress = ipInput.GetComponent<Text>().text;
        instance.StartClient();
    }
}
